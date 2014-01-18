package com.hong.manager.sys.module.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hong.core.generic.service.IGenericService;
import com.hong.manager.sys.module.domain.SysModule;
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
public class SysModuleController {
    private static final Logger logger = Logger.getLogger(SysModuleController.class);

    @Autowired
    private IGenericService genericService;

    @RequestMapping(value = "/manager/sys/module/browse")
    public ModelAndView browseModule() {
        ModelAndView mv = new ModelAndView("/manager/sys/module/browse");

        String sql = "select id, isnull(parentId,0) pId, name from sys_module order by sequence, id asc";
        List<Map> moduleList = genericService.executeSqlToRecordMap(sql);

        mv.addObject("nodes", JSON.toJSONString(moduleList, SerializerFeature.UseSingleQuotes));

//        System.out.println(JSON.toJSONString(moduleList));

        return mv;
    }

    // 未用到
    @RequestMapping(value = "/manager/sys/module/quickCreate")
    public void add(String name, Long pId, HttpServletResponse response, HttpServletRequest request) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SysModule sysModule = new SysModule();
        sysModule.setName(name);
        sysModule.setParentId(pId);
        genericService.saveObject(sysModule);

        try {
            response.getWriter().write(JSON.toJSONString(sysModule));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/manager/sys/module/view")
    public ModelAndView view(Long id) {
        ModelAndView mv = new ModelAndView("/manager/sys/module/view");
        if (id != null && id > 0) {
            SysModule sysModule = (SysModule)genericService.lookUp(SysModule.class, id);
            mv.addObject("module", sysModule);
        }
        return mv;
    }

    @RequestMapping(value = "/manager/sys/module/add")
    public ModelAndView add() {       // 暂未启用，以后可能也不会用
        ModelAndView mv = new ModelAndView("/manager/sys/module/add");

        return mv;
    }

    @RequestMapping(value = "/manager/sys/module/saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response, SysModule sysModule) {
        Map<String, Object> result = new HashMap<String, Object>();
        response.setContentType("text/html;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result.put("exception", e.getMessage());
            logger.warn(e.getMessage());
        }

        SysModule parent = (SysModule)genericService.lookUp(SysModule.class, sysModule.getParentId());
        sysModule.setParent(parent);
        if (sysModule.getId() != null && sysModule.getId() > 0) {
            genericService.updateObject(sysModule);
        } else {
            genericService.saveObject(sysModule);
        }

        if (sysModule != null && sysModule.getId() > 0) {
            result.put("message", "保存成功！");
            result.put("bean", sysModule);
        } else {
            result.put("message", "保存失败！");
        }

        try {
            response.getWriter().print(JSON.toJSONString(result));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
    }

    @RequestMapping(value = "/manager/sys/module/ajaxFindModule")
    public void ajaxFindModule(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }

        String id = request.getParameter("id");

        String sql = "select id, name as text from sys_module where parentId is null and id<>'" + id + "'";
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        SysModuleControllerHelper helper = new SysModuleControllerHelper(genericService);
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
            logger.warn(e.getMessage());
        }
    }

    @RequestMapping(value = "/manager/sys/module/loadChildNode")
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
            logger.warn(e.getMessage());
        }
    }

    @RequestMapping(value = "/manager/sys/module/ajaxView")
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
            logger.warn(e.getMessage());
        }
    }

    @RequestMapping("/manager/sys/module/edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String id = request.getParameter("id");
        SysModule sysModule = (SysModule)genericService.lookUp(SysModule.class, Long.parseLong(id));
        if (sysModule == null) {
            sysModule = new SysModule();
        }
        try {
            response.getWriter().write(JSON.toJSONString(sysModule));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
    }

    @RequestMapping(value = "/manager/sys/module/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        boolean flag = false;
        String id = request.getParameter("id");
        SysModule sysModule = (SysModule)genericService.lookUp(SysModule.class, Long.parseLong(id));

        SysModuleControllerHelper helper = new SysModuleControllerHelper(genericService);
        flag = helper.deleteChildren(sysModule);

        try {
            response.getWriter().print(flag);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
    }
}