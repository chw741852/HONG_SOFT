package com.hong.manager.module.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hong.core.generic.service.IGenericService;
import com.hong.manager.module.domain.Module;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-10-29
 * Time: 上午11:04
 * To change this template use File | Settings | File Templates.
 * TODO 左边树形菜单暂不支持拖动，右边的子节点列表的增删改查功能为实现
 */

@Controller
public class ModuleController {
//    private static final Logger logger = Logger.getLogger(ModuleController.class);

    @Autowired
    private IGenericService genericService;

    @RequestMapping(value = "/manager/module/browse")
    public ModelAndView browseModule() {
        ModelAndView mv = new ModelAndView("/manager/module/browse");

        String sql = "select id, isnull(parentId,0) pId, name from sys_module order by sequence, id asc";
        List<Map> moduleList = genericService.executeSqlToRecordMap(sql);

        mv.addObject("nodes", JSON.toJSONString(moduleList, SerializerFeature.UseSingleQuotes));

//        System.out.println(JSON.toJSONString(moduleList));

        return mv;
    }

    // 未用到
    @RequestMapping(value = "/manager/module/quickCreate")
    public void add(String name, Long pId, HttpServletResponse response, HttpServletRequest request) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Module module = new Module();
        module.setName(name);
        module.setParentId(pId);
        genericService.saveObject(module);

        try {
            response.getWriter().write(JSON.toJSONString(module));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/manager/module/view")
    public ModelAndView view(Long id) {
        ModelAndView mv = new ModelAndView("/manager/module/view");
        if (id != null && id > 0) {
            Module module = (Module)genericService.lookUp(Module.class, id);
            mv.addObject("module", module);
        }
        return mv;
    }

    @RequestMapping(value = "/manager/module/add")
    public ModelAndView add() {       // 暂未启用，以后可能也不会用
        ModelAndView mv = new ModelAndView("/manager/module/add");

        return mv;
    }

    @RequestMapping(value = "/manager/module/saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response, Module module) {
        Map<String, Object> result = new HashMap<String, Object>();
        response.setContentType("text/html;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result.put("exception", e.getMessage());
        }

        Module parent = (Module)genericService.lookUp(Module.class, module.getParentId());
        module.setParent(parent);
        if (module.getId() != null && module.getId() > 0) {
            genericService.updateObject(module);
        } else {
            genericService.saveObject(module);
        }

        if (module != null && module.getId() > 0) {
            result.put("message", "保存成功！");
            result.put("bean", module);
        } else {
            result.put("message", "保存失败！");
        }

        try {
            response.getWriter().print(JSON.toJSONString(result));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/manager/module/ajaxFindModule")
    public void ajaxFindModule(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String id = request.getParameter("id");

        String sql = "select id, name as text from sys_module where parentId is null and id<>'" + id + "'";
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        ModuleControllerHelper helper = new ModuleControllerHelper(genericService);
        for (Map map:result) {
            helper.findChildren(id, map);
        }
//        System.out.println(JSON.toJSONString(result));
        try {
            response.getWriter().print(JSON.toJSONString(result));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/manager/module/loadChildNode")
    public void loadChildNode(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String id = request.getParameter("id");
        String sql;
        if (id != null && Long.parseLong(id) > 0) {
            sql = "select id, name, authority, sequence, actionUrl," +
                    " case authority when '1' then '需要验证' when '2' then '基本权限' end as authority," +
                    " case isMenu when 1 then '是' else '否' end as isMenu," +
                    " case isDisplay when 1 then '是' else '否' end as isDisplay," +
                    " case isSys when 1 then '是' else '否' end as isSys" +
                    " from sys_module where parentId=" + id + " order by sequence, id asc";
        } else {
            sql = "select id, name, authority, sequence, actionUrl," +
                    " case authority when '1' then '需要验证' else '基本权限' end as authorityText," +
                    " case isMenu when 1 then '是' else '否' end as isMenu," +
                    " case isDisplay when 1 then '是' else '否' end as isDisplay," +
                    " case isSys when 1 then '是' else '否' end as isSys" +
                    " from sys_module where parentId is null order by sequence, id asc";
        }
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        try {
            response.getWriter().write(JSON.toJSONString(result));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/manager/module/ajaxView")
    public void ajaxView(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getParameter("id");

        String sql = "select a.id, a.sequence, a.name, b.name as parentNode, a.actionUrl," +
                " case a.authority when '1' then '需要验证' else '基本权限' end as authority," +
                " case a.isMenu when '1' then '是' else '否' end as menu," +
                " case a.isDisplay when '1' then '是' else '否' end as display," +
                " case a.isSys when '1' then '是' else '否' end as sys" +
                " from sys_module a left join sys_module b on a.parentId = b.id" +
                " where a.id=" + id;
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        try {
            response.getWriter().write(JSON.toJSONString(result));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/manager/module/edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String id = request.getParameter("id");
        Module module = (Module)genericService.lookUp(Module.class, Long.parseLong(id));
        if (module == null) {
            module = new Module();
        }
        try {
            response.getWriter().write(JSON.toJSONString(module));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/manager/module/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        boolean flag = false;
        String id = request.getParameter("id");
        Module module = (Module)genericService.lookUp(Module.class, Long.parseLong(id));

        ModuleControllerHelper helper = new ModuleControllerHelper(genericService);
        flag = helper.deleteChildren(module);

        try {
            response.getWriter().print(flag);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
