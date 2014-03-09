package com.hong.manager.sys.dictionary.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hong.core.generic.controller.impl.BaseControllerImpl;
import com.hong.manager.sys.dictionary.domain.SysCode;
import com.hong.manager.sys.dictionary.domain.SysDictionary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 14-1-10
 * Time: 下午1:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/manager/sys/dictionary")
public class DictionaryController extends BaseControllerImpl {
    private final String BASEPATH = "/manager/sys/dictionary";

    @RequestMapping(value = "/browse")
    public ModelAndView browse() {
        ModelAndView mv = new ModelAndView(BASEPATH + "/browse");

        String sql = "select id, isnull(parentId, 0) pId, name from sys_dictionary order by sequence, id";
        List<Map> dictList = genericService.executeSqlToRecordMap(sql);
        mv.addObject("nodes", JSON.toJSONString(dictList, SerializerFeature.UseSingleQuotes));

        return mv;
    }

    @RequestMapping(value = "/ajaxFindDict")
    public void ajaxFindDict(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String id = request.getParameter("id");
        String sql = "select id, name as text from sys_dictionary where parentId is null and id<>'" + id + "'";
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        DictionaryControllerHelper helper = new DictionaryControllerHelper(genericService);
        for(Map map:result) {
            helper.findChildren(id, map);
        }

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping(value = "/saveOrUpdate")
    public void saveOrUpdate(HttpServletResponse response, SysDictionary dictionary) {
        response.setContentType("text/html;charset=UTF-8");

        SysDictionary parent = (SysDictionary)genericService.lookUp(SysDictionary.class, dictionary.getParentId());
        dictionary.setParent(parent);

        if (dictionary.getId() != null && dictionary.getId() > 0) {
            if (dictionary.getKey() == null || dictionary.getKey().trim().equals("")) {
                dictionary.setKey(dictionary.getId().toString());
            }
            genericService.updateObject(dictionary);
        } else {
            genericService.saveObject(dictionary);
            if (dictionary.getKey() == null || dictionary.getKey().trim().equals("")) {
                dictionary.setKey(dictionary.getId().toString());
            }
            genericService.updateObject(dictionary);
        }

        Map<String, Object> result = new HashMap<String, Object>();

        if (dictionary != null && dictionary.getId() > 0) {
            result.put("message", "保存成功！");
            result.put("bean", dictionary);
        } else {
            result.put("message", "保存失败！");
        }

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping(value = "/ajaxView")
    public void ajaxView(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getParameter("id");

        String sql = "select a.id, a.sequence, a.name, a.\"key\", b.name as parentNode" +
                " from sys_dictionary a left join sys_dictionary b on a.parentId = b.id" +
                " where a.id=" + id;
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping(value = "/loadCodeRow")
    public void loadCodeRow(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String dictId = request.getParameter("dictId");
        String sql = "select id, number, name from sys_code where sysDictionaryId=" + dictId;
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping(value = "/loadChildRow")
    public void loadChildRow(HttpServletRequest request, HttpServletResponse response) {

    }

    @RequestMapping(value = "/findComboTreeDict")
    public void findComboTreeDict(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String id = request.getParameter("id");
        String sql = "select id, name as text from sys_dictionary where parentId is null and id<>'" + id + "'";
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        DictionaryControllerHelper helper = new DictionaryControllerHelper(genericService);
        for (Map map:result) {
            helper.findChildren(id, map);
        }

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping(value = "/checkKey")
    public void checkKey(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, Object> result = new HashMap<String, Object>();
        String id = request.getParameter("id");
        String key = request.getParameter("key");
        String sql = "select count(*) from sys_dictionary where \"key\"='" + key + "'";
        if (id != null && !"".equals(id.trim()) && Integer.parseInt(id) > 0) {
            sql = sql + " and id<>" + id;
        }
        long count = genericService.getSqlRecordCount(sql);
        if (count > 0) {
            result.put("flag", false);
            result.put("msg", "键值已存在！");
        } else {
            result.put("flag", true);
        }

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping(value = "/edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String id = request.getParameter("id");
        SysDictionary dictionary = (SysDictionary)genericService.lookUp(SysDictionary.class, Long.parseLong(id));
        if (dictionary == null ) {
            dictionary = new SysDictionary();
        }

        String json = JSON.toJSONString(dictionary);
        printJson(json, response);
    }

    @RequestMapping(value = "/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        DictionaryControllerHelper helper = new DictionaryControllerHelper(genericService);
        boolean flag = false;
        String id = request.getParameter("id");
        if (id != null && Long.parseLong(id) > 0) {
            SysDictionary dictionary = (SysDictionary)genericService.lookUp(SysDictionary.class, Long.parseLong(id));
            flag = helper.deleteObjects(dictionary);
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

    @RequestMapping(value = "/loadChildNode")
    public void loadChildNode(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String id = request.getParameter("id");
        StringBuffer sql = new StringBuffer("select id, name, \"key\", sequence from sys_dictionary");
        if (id != null && Long.parseLong(id) > 0) {
            sql.append(" where parentId=" + id);
        } else {
            sql.append(" where parentId is null");
        }
        sql.append(" order by sequence, id asc");

        List<Map> result = genericService.executeSqlToRecordMap(sql.toString());
        String json = JSON.toJSONString(result);

        printJson(json, response);
    }

    @RequestMapping(value = "/saveOrUpdateCode")
    public void saveOrUpdateCode(HttpServletRequest request, HttpServletResponse response, SysCode sysCode) {
        response.setContentType("text/html;charset=UTF-8");

        boolean flag = false;
        String dictId = request.getParameter("dictId");
        if (dictId != null && !dictId.trim().equals("") && Long.parseLong(dictId) > 0) {
            SysDictionary dictionary = (SysDictionary)genericService.lookUp(SysDictionary.class, Long.parseLong(dictId));
            sysCode.setSysDictionary(dictionary);
            if (sysCode.getId() == null || sysCode.getId() <= 0) {
                if (genericService.saveObject(sysCode) != null) {
                    flag = true;
                }
            } else {
                SysCode code = (SysCode)genericService.lookUp(SysCode.class, sysCode.getId());
                code.setName(sysCode.getName());
                code.setNumber(sysCode.getNumber());
                sysCode = (SysCode)genericService.updateObject(code);
                if (sysCode != null) {
                    flag = true;
                }
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", flag);
        result.put("code", sysCode);

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping(value = "/deleteCode")
    public void deleteCode(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        boolean flag = false;
        String id = request.getParameter("id");
        if (id != null && !id.trim().equals("") && Long.parseLong(id) > 0) {
            SysCode sysCode = (SysCode)genericService.lookUp(SysCode.class, Long.parseLong(id));
            flag = genericService.deleteObject(sysCode);
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

    @RequestMapping("/zTreeDrop")
    public void zTreeDrop(long id, long targetId, String type, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> map = new HashMap<String, String>();
        SysDictionary child = (SysDictionary)genericService.lookUp(SysDictionary.class, id);
        SysDictionary parent = (SysDictionary)genericService.lookUp(SysDictionary.class, targetId);
        if ("inner".equals(type.trim())) {
            child.setParent(parent);
            if (genericService.updateObject(child) == null) {
                map.put("errorMsg", "保存失败");
            }
        }

        String json = JSON.toJSONString(map);
        printJson(json, response);
    }
}
