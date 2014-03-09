package com.hong.core.generic.controller.impl;

import com.hong.core.generic.service.IGenericService;
import com.hong.core.generic.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by hong on 14-2-16 下午3:02.
 */
@Component
public class BaseControllerImpl implements BaseController {
    @Autowired
    protected IGenericService genericService;

    @Override
    public void printJson(String json, HttpServletResponse response) {
        try {
            response.getWriter().write(json);
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

    @Override
    public void initSysCode(ModelAndView mv, Map<String, String> map) {
        for(Map.Entry<String, String> entry:map.entrySet()) {
            mv.addObject(entry.getKey(), genericService.executeObjectSql(entry.getValue()));
        }
    }
}
