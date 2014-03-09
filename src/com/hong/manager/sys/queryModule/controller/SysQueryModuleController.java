package com.hong.manager.sys.queryModule.controller;

import com.hong.core.generic.controller.impl.BaseControllerImpl;
import com.hong.core.util.DateUtil;
import com.hong.core.util.Tools;
import com.hong.manager.sys.queryModule.domain.SysField;
import com.hong.manager.sys.queryModule.domain.SysListLink;
import com.hong.manager.sys.queryModule.domain.SysQueryModule;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 14-1-8
 * Time: 下午12:41
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/manager/sys/queryModule")
public class SysQueryModuleController extends BaseControllerImpl {
    private final String BASEPATH = "/manager/sys/queryModule";

    @RequestMapping("/list")
    public String list(String message, final RedirectAttributes redirectAttributes) {
        if (message != null)    message = Tools.escapeCodeStr(message);
        redirectAttributes.addAttribute("queryModuleId", 32768);
        redirectAttributes.addFlashAttribute("flash_message", message);

        return "redirect:/manager/web/initPublicListInfo";
    }

    @RequestMapping("/add")
    public String add() {

        return BASEPATH + "/add";
    }

    @RequestMapping("/save")
    public String save(SysQueryModule sysQueryModule) {
        genericService.saveObject(sysQueryModule);
        return "redirect:" + BASEPATH + "/edit?id=" + sysQueryModule.getId();
    }

    @RequestMapping("/edit")
    public ModelAndView edit(long id) {
        ModelAndView mv = new ModelAndView(BASEPATH + "/edit");

        SysQueryModule sysQueryModule = (SysQueryModule)genericService.lookUp(SysQueryModule.class, id);
        List<Map<String, Object>> listLinkMap = new ArrayList<Map<String, Object>>();
        for (SysListLink listLink:sysQueryModule.getSysListLinks()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", listLink.getId());
            map.put("name", listLink.getName());
            listLinkMap.add(map);
        }
        mv.addObject("sysQueryModule", sysQueryModule);
        mv.addObject("listLinkList", listLinkMap);

        return mv;
    }

    @RequestMapping("/update")
    public String update(SysQueryModule sysQueryModule) {
        genericService.updateObject(sysQueryModule);
        return "redirect:" + BASEPATH + "/edit?id=" + sysQueryModule.getId();
    }

    @RequestMapping("/delete")
    public ModelAndView delete(long id) {
        ModelAndView mv = new ModelAndView("redirect:" + BASEPATH + "/list");

        SysQueryModule sysQueryModule = (SysQueryModule)genericService.lookUp(SysQueryModule.class, id);
        if (sysQueryModule == null) {
            mv.addObject("message", "未找到ID为 " + id + " 的查询生成器");
        } else {
            if (genericService.deleteObject(sysQueryModule) == true) {
                mv.addObject("message", "查询生成器 " + sysQueryModule.getName() + " 删除成功");
            } else {
                mv.addObject("message", "查询生成器 " + sysQueryModule.getName() + " 删除失败");
            }
        }

        return mv;
    }

    @RequestMapping("/copy")
    public ModelAndView copy(long id) {
        ModelAndView mv = new ModelAndView(BASEPATH + "/edit");

        SysQueryModule sysQueryModule = (SysQueryModule)genericService.lookUp(SysQueryModule.class, id);
        if (sysQueryModule == null) {
            mv.setViewName("redirect:" + BASEPATH + "/list");
            mv.addObject("message", "未找到ID为 " + id + " 的查询生成器");
        } else {
            SysQueryModule newQueryModule = new SysQueryModule();
            BeanUtils.copyProperties(sysQueryModule, newQueryModule);
            newQueryModule.setId(null);
            newQueryModule.setVersion(null);
            newQueryModule.setDbClickRowLinkId(null);
            newQueryModule.setSysFields(null);
            newQueryModule.setSysListLinks(null);
            newQueryModule.setCreatedDate(DateUtil.getDate());
            newQueryModule.setCreatedTime(DateUtil.getTime());

            genericService.saveObject(newQueryModule);

            // 复制字段
            for (SysField sysField:sysQueryModule.getSysFields()) {
                SysField newSysField = new SysField();
                BeanUtils.copyProperties(sysField, newSysField);
                newSysField.setId(null);
                newSysField.setVersion(null);
                newSysField.setSysQueryModule(newQueryModule);

                genericService.saveObject(newSysField);
            }

            List<Map<String, Object>> listLinkMap = new ArrayList<Map<String, Object>>();
            // 复制查询列表链接
            for (SysListLink sysListLink:sysQueryModule.getSysListLinks()) {
                SysListLink newSysListLink = new SysListLink();
                BeanUtils.copyProperties(sysListLink, newSysListLink);
                newSysListLink.setId(null);
                newSysListLink.setVersion(null);
                newSysListLink.setSysQueryModule(newQueryModule);

                genericService.saveObject(newSysListLink);

                if (sysQueryModule.getDbClickRowLinkId() != null
                        && sysQueryModule.getDbClickRowLinkId().longValue() == sysListLink.getId().longValue()) {
                    newQueryModule.setDbClickRowLinkId(newSysListLink.getId());
                    genericService.updateObject(newQueryModule);
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", newSysListLink.getId());
                map.put("name", newSysListLink.getName());
                listLinkMap.add(map);
            }
            mv.addObject("listLinkList", listLinkMap);
            mv.addObject("sysQueryModule", newQueryModule);
        }

        return mv;
    }
}
