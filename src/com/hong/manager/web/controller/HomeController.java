package com.hong.manager.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-10-25
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
@Controller("managerHomeController")
public class HomeController {
    @RequestMapping(value = "/manager/home", method = RequestMethod.GET)
    public String home() {
        return "/manager/home";
    }
}