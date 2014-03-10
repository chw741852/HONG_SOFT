package com.hong.manager.web.controller;

import com.alibaba.fastjson.JSON;
import com.hong.core.generic.service.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-10-25
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
@Controller("managerHomeController")
public class HomeController {
    @Autowired
    private IGenericService genericService;

    @RequestMapping(value = "/manager/home")
    public String home() {
        return "/manager/home";
    }

    @RequestMapping(value = "/manager/home/loadZTreeNodes", method = RequestMethod.GET)
    public void loadZTreeNodes(HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String sql = "select id, isnull(parentId,0) pId, name, actionUrl action" +
                " from sys_module order by sequence, id asc";
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        try {
            response.getWriter().write(JSON.toJSONString(result));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}