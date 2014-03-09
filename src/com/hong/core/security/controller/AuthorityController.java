package com.hong.core.security.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hong.core.generic.controller.impl.BaseControllerImpl;
import com.hong.core.security.domain.Authority;
import com.hong.core.security.domain.Resource;
import com.hong.core.security.support.ISecurityManager;
import com.hong.core.util.Tools;
import com.hong.manager.sys.dictionary.domain.SysCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by hong on 14-2-25 下午5:01.
 */
@Controller
@RequestMapping("/core/security/authority")
public class AuthorityController extends BaseControllerImpl {
    @javax.annotation.Resource(name = "userDetailsService")
    private ISecurityManager securityManager;

    private final String BASEPATH = "/core/security/authority";

    @RequestMapping("/list")
    public String list(String message, final RedirectAttributes redirectAttributes) {
        if (message != null)    message = Tools.escapeCodeStr(message);
        redirectAttributes.addAttribute("queryModuleId", 229383);
        redirectAttributes.addFlashAttribute("flash_message", message);

        return "redirect:/manager/web/initPublicListInfo";
    }

    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mv = new ModelAndView(BASEPATH + "/add");

        Map<String, String> sysCodeMap = new HashMap<String, String>();
        String url = "from " + SysCode.class.getName() + " a where a.sysDictionary.key=";
        sysCodeMap.put("yesOrNo", url + "2");
        initSysCode(mv, sysCodeMap);

        return mv;
    }

    @RequestMapping("/save")
    public ModelAndView save(Authority authority) {
        ModelAndView mv = new ModelAndView("redirect:" + BASEPATH + "/list");

        if (genericService.saveObject(authority) != null) {
            mv.addObject("message", "权限 " + authority.getName() + " 新建成功");
        } else {
            mv.addObject("message", "新建失败");
        }

        return mv;
    }

    @RequestMapping("/edit")
    public ModelAndView edit(long id) {
        ModelAndView mv = new ModelAndView(BASEPATH + "/edit");

        Authority authority = (Authority)genericService.lookUp(Authority.class, id);
        if (authority == null) {
            mv.setViewName("redirect:" + BASEPATH + "/list");
            mv.addObject("message", "未找到ID为 " + id + " 的权限");
        } else {
            mv.addObject("authority", authority);

            Map<String, String> sysCodeMap = new HashMap<String, String>();
            String url = "from " + SysCode.class.getName() + " a where a.sysDictionary.key=";
            sysCodeMap.put("yesOrNo", url + "2");
            initSysCode(mv, sysCodeMap);
        }

        return mv;
    }

    @RequestMapping("/update")
    public ModelAndView update(Authority authority) {
        ModelAndView mv = new ModelAndView("redirect:" + BASEPATH + "/list");

        if (genericService.updateObject(authority) != null) {
            mv.addObject("message", "权限 " + authority.getName() + " 修改成功");
        } else {
            mv.addObject("message", "修改失败");
        }

        return mv;
    }

    @RequestMapping("/lookup")
    public ModelAndView lookup(long id) {
        ModelAndView mv = edit(id);

        if (mv.getViewName().indexOf("redirect:") < 0) {
            mv.setViewName(BASEPATH + "/lookup");
            String sql = "select a.name, a.url from sys_resource a inner join sys_authority_resource b" +
                    " on a.id=b.resource_id inner join sys_authority c on b.authority_id=c.id where c.id=" + id;
            List<Map> result = genericService.executeSqlToRecordMap(sql);
            String json = JSON.toJSONString(result, SerializerFeature.UseSingleQuotes);
            mv.addObject("resources", json);
        }

        return mv;
    }

    @RequestMapping("/delete")
    public ModelAndView delete(long id, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("redirect:" + BASEPATH + "/list");

        Authority authority = (Authority)genericService.lookUp(Authority.class, id);
        if (authority == null) {
            mv.addObject("message", "未找到ID为 " + id + " 的权限");
        } else {
            String sql = "select name from sys_role where id in (" +
                    " select role_id from sys_role_authority where authority_id=" + id + ")";
            List<String[]> result = genericService.executeSql(sql);
            if (result != null && result.size() > 0) {
                StringBuffer names = new StringBuffer();
                for (String[] str:result) {
                    names.append(str[0] + ", ");
                }
                mv.addObject("message", "权限 " + authority.getName() + " 正在被 角色'" +
                        names.substring(0, names.lastIndexOf(",")) + "' 使用，删除失败");
            } else if (genericService.deleteObject(authority) == false) {
                mv.addObject("message", "权限 " + authority.getName() + " 删除失败");
            } else {
                mv.addObject("message", "权限 " + authority.getName() + " 删除成功");

                request.getServletContext().setAttribute("resourceMap", securityManager.loadResource());
            }
        }

        return mv;
    }

    @RequestMapping("/deployResource")
    public ModelAndView deployResource(long id) {
        ModelAndView mv = new ModelAndView(BASEPATH + "/deployResource");

        Authority authority = (Authority)genericService.lookUp(Authority.class, id);
        if (authority == null) {
            mv.setViewName("redirect:" + BASEPATH + "/list");
            mv.addObject("message", "未找到ID为 " + id + " 的权限");
        } else {
            String sql = "select a.id, a.name, isnull(a.parentId, 0) pId," +
                    " case when a.id=b.resource_id then 'true' else 'false' end checked" +
                    " from sys_resource a left join sys_authority_resource b" +
                    " on a.id=b.resource_id and b.authority_id=" + id + " where a.enabled='1'";
            List<Map> result = genericService.executeSqlToRecordMap(sql);
            String json = JSON.toJSONString(result);
            mv.addObject("zNodes", json);
            mv.addObject("authority", authority);
        }

        return mv;
    }

    @RequestMapping("/saveResource")
    public ModelAndView saveResource(String resourceIds, long id, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("redirect:" + BASEPATH + "/list");

        Authority authority = (Authority)genericService.lookUp(Authority.class, id);
        Set<Resource> resources = new HashSet<Resource>();
        if (!"".equals(resourceIds.trim())) {
            for (String resourceId:resourceIds.split(",")) {
                Resource resource = (Resource)genericService.lookUp(Resource.class, Long.parseLong(resourceId));
                resources.add(resource);
            }
        }
        authority.setResources(resources);

        if (genericService.updateObject(authority) != null) {
            mv.addObject("message", "权限 " + authority.getName() + " 分配资源成功");

            request.getServletContext().setAttribute("resourceMap", securityManager.loadResource());
        } else {
            mv.addObject("message", "权限 " + authority.getName() + " 分配资源失败");
        }

        return mv;
    }
}
