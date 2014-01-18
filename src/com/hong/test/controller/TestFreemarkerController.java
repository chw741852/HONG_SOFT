package com.hong.test.controller;

import com.alibaba.fastjson.JSON;
import com.hong.core.generic.service.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-11-8
 * Time: 上午9:53
 * To change this template use File | Settings | File Templates.
 */
@Controller("testFreemarker")
@RequestMapping(value = "/test")
public class TestFreemarkerController {
    @Autowired
    private IGenericService genericService;

    @RequestMapping(value = "/freemarker", method = RequestMethod.GET)
    public String test(ModelMap model) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "cai");
        map.put("age", "23");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("name", "蔡宏伟");
        map.put("age", "25");
        list.add(map);

        model.addAttribute("employee", list);

        for (Map<String, String> m:list) {
            System.out.println(m.get("name") + ", " + m.get("age"));
        }

        return "test";
    }

    @RequestMapping(value = "/easyui")
    public void testEasyui(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String sql = "select id, name, authority, sequence," +
                " case authority when '1' then '需要验证' else '基本权限' end as authorityText," +
                " case isMenu when 1 then '是' else '否' end as isMenu," +
                " case isDisplay when 1 then '是' else '否' end as isDisplay" +
                " from sys_module where parentId is null order by sequence, id asc";

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
