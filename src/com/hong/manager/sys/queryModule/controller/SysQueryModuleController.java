package com.hong.manager.sys.queryModule.controller;

import com.hong.manager.sys.base.controller.impl.BaseControllerImpl;
import com.hong.manager.sys.queryModule.domain.SysListLink;
import com.hong.manager.sys.queryModule.domain.SysQueryModule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
}
