package com.hong.manager.sys.base.controller;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by hong on 14-2-16 下午2:59.
 */
public interface BaseController {
    public void printJson(HttpServletResponse response, String json);

    /**
     * 初始化字典表
     * @param map 键与SQL语句
     */
    public void initSysCode(ModelAndView mv, Map<String, String> map);
}
