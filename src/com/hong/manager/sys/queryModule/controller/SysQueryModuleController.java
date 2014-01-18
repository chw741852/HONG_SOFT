package com.hong.manager.sys.queryModule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 14-1-8
 * Time: 下午12:41
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/manager/sys/queryModule")
public class SysQueryModuleController {
    @RequestMapping("/add")
    public String add() {

        return "/manager/sys/queryModule/add";
    }
}
