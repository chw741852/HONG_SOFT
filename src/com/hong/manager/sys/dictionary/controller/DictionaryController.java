package com.hong.manager.sys.dictionary.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hong.core.generic.domain.IdEntity;
import com.hong.core.generic.service.IGenericService;
import com.hong.manager.sys.dictionary.domain.SysDictionary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DictionaryController extends IdEntity {
    @Autowired
    private IGenericService genericService;

    private static final Log log = LogFactory.getLog(DictionaryController.class);
    private final String BASEPATH = "/manager/sys/dictionary";
    private final DictionaryControllerHelper helper = new DictionaryControllerHelper(genericService);

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

        for(Map map:result) {
            helper.findChildren(id, map);
        }

        String json = JSON.toJSONString(result);
        try {
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/saveOrUpdate")
    public void saveOrUpdate(HttpServletResponse response, SysDictionary dictionary) {
        response.setContentType("text/html;charset=UTF-8");

        SysDictionary parent = (SysDictionary)genericService.lookUp(SysDictionary.class, dictionary.getParentId());
        dictionary.setParent(parent);
        if (dictionary.getId() != null && dictionary.getId() > 0) {
            genericService.updateObject(dictionary);
        } else {
            genericService.saveObject(dictionary);
        }

        Map<String, Object> result = new HashMap<String, Object>();

        if (dictionary != null && dictionary.getId() > 0) {
            result.put("message", "保存成功！");
            result.put("bean", dictionary);

        } else {
            result.put("message", "保存失败！");
        }

        String json = JSON.toJSONString(result);
        try {
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
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
        try {
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/loadCodeRow")
    public void loadCodeRow(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String dictId = request.getParameter("dictId");
        String sql = "select id, number, name from sys_code where sysDictionaryId=" + dictId;
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        String json = JSON.toJSONString(result);
        try {
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
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

        for (Map map:result) {
            helper.findChildren(id, map);
        }

        String json = JSON.toJSONString(result);
        try {
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/checkKey")
    public void checkKey(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String key = request.getParameter("key");
        String sql = "select count(*) from sys_dictionary where key='" + key + "'";
        long count = genericService.getSqlRecordCount(sql);
    }
}
