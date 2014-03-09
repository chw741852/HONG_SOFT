package com.hong.core.security.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hong.core.generic.controller.impl.BaseControllerImpl;
import com.hong.core.security.domain.Resource;
import com.hong.core.security.support.ISecurityManager;
import com.hong.manager.sys.dictionary.domain.SysCode;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hong on 14-2-25 下午1:49.
 */
@Controller
@RequestMapping("/core/security/resource")
public class ResourceController extends BaseControllerImpl {
    @javax.annotation.Resource(name = "userDetailsService")
    private ISecurityManager securityManager;

    private final String BASEPATH = "/core/security/resource";

    @RequestMapping("/browse")
    public ModelAndView browse() {
        ModelAndView mv = new ModelAndView(BASEPATH + "/browse");

        String sql = "select id, isnull(parentId,0) pId, name from sys_resource order by sequence, id asc";
        List<Map> moduleList = genericService.executeSqlToRecordMap(sql);
        mv.addObject("nodes", JSON.toJSONString(moduleList, SerializerFeature.UseSingleQuotes));

        Map<String, String> sysCodeMap = new HashMap<String, String>();
        String url = "from " + SysCode.class.getName() + " a where a.sysDictionary.key=";
        sysCodeMap.put("yesOrNo", url + "'2'");
        initSysCode(mv, sysCodeMap);

        return mv;
    }

    @RequestMapping("/ajaxFindResource")
    public void ajaxFindResource(String id, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String sql = "select id, name as text from sys_resource where parentId is null and id<>'" + id + "'";
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        ResourceControllerHelper helper = new ResourceControllerHelper(genericService);
        for (Map map:result) {
            helper.findChildren(id, map);
        }

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping("/edit")
    public void edit(Long id, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        Resource resource = (Resource)genericService.lookUp(Resource.class, id);
        if (resource == null) {
            resource = new Resource();
        }
        if (resource.getParent() == null) {
            resource.setParent(new Resource());
        } else {
            resource.setParentId(resource.getParent().getId());
        }

        String json = JSON.toJSONString(resource);
        printJson(json, response);
    }

    @RequestMapping("/loadChildNode")
    public void loadChildNode(Long id, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        StringBuffer sql = new StringBuffer("select id, name, url," +
                " case type when '1' then 'action' else 'url' end type," +
                " case enabled when '1' then '是' else '否' end enabled" +
                " from sys_resource ");
        if (id == null || id <= 0) {
            sql.append(" where parentId is null");
        } else {
            sql.append(" where parentId=" + id);
        }

        List<Map> result = genericService.executeSqlToRecordMap(sql.toString());
        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping("/ajaxView")
    public void ajaxView(Long id, HttpServletResponse response) {
        edit(id, response);
    }

    @RequestMapping("/saveOrUpdate")
    public void saveOrUpdate(Resource resource, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String message;
        Resource parent = (Resource)genericService.lookUp(Resource.class, resource.getParentId());
        resource.setParent(parent);
        if (resource.getId() == null || resource.getId() <= 0) {
            genericService.saveObject(resource);
        } else {
            genericService.updateObject(resource);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        if (resource == null) {
            message = "保存失败";
        } else {
            message = "保存成功";
            if (resource.getParent() == null) {
                resource.setParent(new Resource());
            }
            map.put("bean", resource);

            Map<String, Collection<ConfigAttribute>> resourceMap = securityManager.loadResource();
            request.getServletContext().setAttribute("resourceMap", resourceMap);
        }
        map.put("message", message);

        String json = JSON.toJSONString(map);
        printJson(json, response);
    }

    @RequestMapping("/zTreeDrop")
    public void zTreeDrop(Long id, Long targetId, String type, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> map = new HashMap<String, String>();
        Resource child = (Resource)genericService.lookUp(Resource.class, id);
        Resource parent = (Resource)genericService.lookUp(Resource.class, targetId);

        if ("inner".equals(type.trim())) {
            child.setParent(parent);
            if (genericService.updateObject(child) == null) {
                map.put("errorMsg", "保存失败");
            }
        }

        String json = JSON.toJSONString(map);
        printJson(json, response);
    }

    @RequestMapping("/delete")
    public void delete(Long id, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> map = new HashMap<String, String>();
        Resource resource = (Resource)genericService.lookUp(Resource.class, id);
        if (resource == null) {
            map.put("errorMsg", "未找到ID为 " + id + " 的资源");
        } else {
            String sql = "select name from sys_authority where id in (" +
                    " select authority_id from sys_authority_resource where resource_id=" + id + ")";
            List<String[]> result = genericService.executeSql(sql);
            if (result != null && result.size() > 0) {
                StringBuffer names = new StringBuffer();
                for (String[] str:result) {
                    names.append(str[0] + ", ");
                }

                map.put("errorMsg", "资源正在被 权限'" + names.substring(0, names.lastIndexOf(",")) + "' 使用，删除失败");
            } else if (genericService.deleteObject(resource) == false) {
                map.put("errorMsg", "删除失败");
            }
        }

        String json = JSON.toJSONString(map);
        printJson(json, response);
    }
}
