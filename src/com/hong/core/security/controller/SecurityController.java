package com.hong.core.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-12-18
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class SecurityController {
    @RequestMapping(value = "/login")
    public String login() {
        return "/login";
    }
}
