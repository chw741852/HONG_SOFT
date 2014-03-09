package com.hong.manager.web.controller;

import com.alibaba.fastjson.JSON;
import com.hong.core.generic.controller.impl.BaseControllerImpl;
import com.hong.core.util.Tools;
import com.hong.manager.sys.queryModule.domain.SysQueryModule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hong on 14-2-11 下午3:05.
 */

@Controller
@RequestMapping("/manager/web")
public class PublicSearchController extends BaseControllerImpl {
    private final String BASEPATH = "/manager/web";

    @RequestMapping("/initPublicListInfo")
    public ModelAndView initPublicListInfo(long queryModuleId, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(BASEPATH + "/initPublicListInfo");
        SysQueryModule queryModule = (SysQueryModule)genericService.lookUp(SysQueryModule.class, queryModuleId);

        mv.addObject("nav1", queryModule.getPagePosition().split("\\|")[0]);
        mv.addObject("nav2", queryModule.getPagePosition().split("\\|")[1]);
        mv.addObject("dbClickRowLinkId", queryModule.getDbClickRowLinkId());

        PublicSearchControllerHelper helper = new PublicSearchControllerHelper(genericService);
        helper.generateQueryHtml(queryModule, mv);
        helper.generateDataGridHtml(queryModule, mv, request.getContextPath());

        return mv;
    }

    /**
     * 查找返回 datagrid的JSON数据格式
     * @param queryModuleId 查询生成器ID
     * @param rows  每页显示数
     * @param page  页码
     * @param request
     * @param response
     */
    @RequestMapping("/queryListInfo")
    public void queryListInfo(long queryModuleId, int rows, int page,
            HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        PublicSearchControllerHelper helper = new PublicSearchControllerHelper(genericService);

        if (rows == 0)  rows = 10;
        if (page == 0)  page = 1;

        SysQueryModule queryModule = (SysQueryModule)genericService.lookUp(SysQueryModule.class, queryModuleId);
        String[] sqls = helper.generateListInfoSql(queryModule, request);

//        System.out.println(sqls[0]);
        List<Map> result = genericService.executeSqlToRecordMap(sqls[0], page, rows);
        long count = genericService.getSqlRecordCount(sqls[1]);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", result);
        map.put("total", count);

        String json = JSON.toJSONString(map);
        printJson(json, response);
    }

    @RequestMapping("/printListInfo")
    public ModelAndView printListInfo(long queryModuleId) {
        return null;
    }
}
