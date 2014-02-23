package com.hong.manager.sys.queryModule.controller;

import com.alibaba.fastjson.JSON;
import com.hong.manager.sys.base.controller.impl.BaseControllerImpl;
import com.hong.manager.sys.dictionary.domain.SysCode;
import com.hong.manager.sys.queryModule.domain.SysListLink;
import com.hong.manager.sys.queryModule.domain.SysQueryModule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hong on 14-2-18 下午2:46.
 */
@Controller
@RequestMapping("/manager/sys/listLink")
public class SysListLinkController extends BaseControllerImpl {
    private final String BASEPATH = "/manager/sys/queryModule";

    @RequestMapping("/browse")
    public ModelAndView browse(long queryModuleId) {
        ModelAndView mv = new ModelAndView(BASEPATH + "/browseListLink");
        SysQueryModule sysQueryModule = (SysQueryModule)genericService.lookUp(SysQueryModule.class, queryModuleId);
        mv.addObject("queryModule", sysQueryModule);

        Map<String, String> sysCodeMap = new HashMap<String, String>();
        String url = "from " + SysCode.class.getName() + " a where a.sysDictionary.key=";
        sysCodeMap.put("yesOrNo", url + "'2'");
//        sysCodeMap.put("linkTypeList", url + "'11' order by a.number asc");
//        sysCodeMap.put("opTypeList", url + "'12'");
        initSysCode(mv, sysCodeMap);

        return mv;
    }

    @RequestMapping("/queryListLink")
    public void queryListLink(long queryModuleId, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String sql = "select id, name, url, sequence, case display when '1' then '是' else '否' end display" +
                " from sys_list_link where sysQueryModuleId=" + queryModuleId;
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        String json = JSON.toJSONString(result);
        printJson(response, json);
    }

    @RequestMapping("/saveOrUpdate")
    public void saveOrUpdate(long queryModuleId, SysListLink sysListLink, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> map = new HashMap<String, String>();
        SysQueryModule sysQueryModule = (SysQueryModule)genericService.lookUp(SysQueryModule.class, queryModuleId);
        if (sysQueryModule == null) {
            map.put("errorMsg", "未找到ID为" + queryModuleId + "的查询模块");
        } else {
            sysListLink.setSysQueryModule(sysQueryModule);
            if (sysListLink.getId() == null || sysListLink.getId() < 1) {
                if (genericService.saveObject(sysListLink) == null) {
                    map.put("errorMsg", "保存失败");
                }
            } else {
                if (genericService.updateObject(sysListLink) == null) {
                    map.put("errorMsg", "保存失败");
                }
            }
        }

        String json = JSON.toJSONString(map);
        printJson(response, json);
    }

    @RequestMapping("/edit")
    public void edit(long id, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        SysListLink sysListLink = (SysListLink)genericService.lookUp(SysListLink.class, id);
        String json = JSON.toJSONString(sysListLink);
        printJson(response, json);
    }

    @RequestMapping("/delete")
    public void delete(long id, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> map = new HashMap<String, String>();
        SysListLink sysListLink = (SysListLink)genericService.lookUp(SysListLink.class, id);
        if (genericService.deleteObject(sysListLink) == false) {
            map.put("errorMsg", "删除失败");
        }

        String json = JSON.toJSONString(map);
        printJson(response, json);
    }
}
