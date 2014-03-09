package com.hong.core.security.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.hong.core.generic.controller.impl.BaseControllerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-12-18
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class SecurityController extends BaseControllerImpl {
    @Autowired
    private Producer captchaProducer;

    @RequestMapping("/login")
    public String login() {
        return "/login";
    }

    @RequestMapping("/home")
    public String home(HttpServletRequest request) {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (o instanceof UserDetails) {
            request.getSession().setAttribute("User", o);
        }

        return "redirect:/manager/home";
    }

    @RequestMapping("/error/accessDenied")
    public String accessDenied() {
        return "/error/accessDenied";
    }

    @RequestMapping("/sessionTimeout")
    public String sessionTimeout() {
        return "/error/sessionTimeout";
    }

    @RequestMapping("/anonymous/kaptchaImg")
    public void kaptchaImg(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
//        String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        System.out.println("验证码是：" + code);

        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();

        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = null;

        // write the data out
        try {
            out = response.getOutputStream();
            ImageIO.write(bi, "jpeg", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/anonymous/checkCaptcha")
    public void checkCaptcha(String captcha, HttpSession session, HttpServletResponse response) {
        String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        boolean flag = false;
        if (captcha.equals(code)) {
            flag = true;
        }

        try {
            response.getWriter().print(flag);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
