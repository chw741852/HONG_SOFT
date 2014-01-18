package com.hong.core.exception;


import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by hong on 14-1-18 下午8:41.
 */
public class GlobelException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        request.setAttribute("exception", e);
//        if (e instanceof SQLException) {
//            return new ModelAndView("/error/sqlException");
//        }
        return new ModelAndView("/error/exception");
    }
}
