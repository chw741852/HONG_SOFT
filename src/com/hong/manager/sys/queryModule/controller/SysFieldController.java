package com.hong.manager.sys.queryModule.controller;

import com.alibaba.fastjson.JSON;
import com.hong.core.generic.controller.impl.BaseControllerImpl;
import com.hong.manager.sys.dictionary.domain.SysCode;
import com.hong.manager.sys.queryModule.domain.SysField;
import com.hong.manager.sys.queryModule.domain.SysQueryModule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hong on 14-2-14 下午3:28.
 */

@Controller
@RequestMapping("/manager/sys/field")
public class SysFieldController extends BaseControllerImpl {
    private final String BASEPATH = "/manager/sys/queryModule";

    @RequestMapping("/browse")
    public ModelAndView browse(long queryModuleId) {
        ModelAndView mv = new ModelAndView(BASEPATH + "/browseField");
        SysQueryModule queryModule = (SysQueryModule)genericService.lookUp(SysQueryModule.class, queryModuleId);
        mv.addObject("queryModule", queryModule);

        Map<String, String> sysCodeMap = new HashMap<String, String>();
        String url = "from " + SysCode.class.getName() + " a where a.sysDictionary.key=";
        sysCodeMap.put("yesOrNo", url + "'2'");
        sysCodeMap.put("fieldOpTypeList", url + "'4'");
        sysCodeMap.put("dataTypeList", url + "'5'");
        sysCodeMap.put("displayTypeList", url + "'6'");
        sysCodeMap.put("orderByList", url + "'7'");
//        sysCodeMap.put("controlKindList", url + "'8'");
//        sysCodeMap.put("dataSourceTypeList", url + "'229383'");
        initSysCode(mv, sysCodeMap);

        return mv;
    }

    @RequestMapping("/queryFieldList")
    public void queryFieldList(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        String queryModuleId = request.getParameter("queryModuleId");
        if (queryModuleId == null || queryModuleId.trim().equals("")) {
            queryModuleId = "0";
        }

        String sql = "select id, displayName, fieldName, tableName, tableAliasName, fieldAliasName," +
            " case isDisplay when '1' then '是' else '否' end isDisplay, querySequence, displaySequence" +
            " from sys_field where sysQueryModuleId=" + queryModuleId;
        List<Map> result = genericService.executeSqlToRecordMap(sql);

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping("/saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response, SysField sysField) {
        response.setContentType("text/html;charset=UTF-8");
        String queryModuleId = request.getParameter("queryModuleId");
        Map<String, Object> result = new HashMap<String, Object>();
        if (queryModuleId == null || queryModuleId.trim().equals("") || Long.parseLong(queryModuleId) < 1) {
            result.put("errorMsg", "未找到ID为" + queryModuleId + "的查询模块");
        } else {
            SysQueryModule sysQueryModule = (SysQueryModule)genericService.lookUp(SysQueryModule.class, Long.parseLong(queryModuleId));
            if (sysQueryModule == null) {
                result.put("errorMsg", "未找到ID为" + queryModuleId + "的查询模块");
            } else {
                sysField.setSysQueryModule(sysQueryModule);
                if (sysField.getId() == null || sysField.getId() < 1) {
                    if (genericService.saveObject(sysField) == null) {
                        result.put("errorMsg", "保存失败");
                    }
                } else {
                    if (genericService.updateObject(sysField) == null) {
                        result.put("errorMsg", "保存失败");
                    }
                }
            }
        }

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping("/edit")
    public void edit(long id, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        SysField sysField = (SysField)genericService.lookUp(SysField.class, id);
        String json = JSON.toJSONString(sysField);
        printJson(json, response);
    }

    @RequestMapping("/delete")
    public void delete(long id, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> result = new HashMap<String, String>();
        SysField sysField = (SysField)genericService.lookUp(SysField.class, id);
        if (!genericService.deleteObject(sysField)) {
            result.put("errorMsg", "删除失败");
        }

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }

    @RequestMapping("/checkTableName")
    public void checkTableName(String tableName, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> map = new HashMap<String, String>();
        List<String> list = genericService.getTablesInSchema("hong_dev", tableName);
        if (list.size() <= 0) {
            map.put("errorMsg", "表" + tableName + " 不存在");
        }

        String json = JSON.toJSONString(map);
        printJson(json, response);
    }

    @RequestMapping("/loadFieldName")
    public void loadFieldName(String tableName, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<String> list = genericService.getColumnsInTable(tableName, "%");

        for(String s:list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("label", s);
            map.put("text", s);
            result.add(map);
        }

        String json = JSON.toJSONString(result);
        printJson(json, response);
    }
}