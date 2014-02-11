package com.hong.core.exception;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hong on 14-1-18 下午8:41.
 */
public class GlobalException implements HandlerExceptionResolver {
    private static final Log log = LogFactory.getLog(GlobalException.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        request.setAttribute("exception", e);
//        if (e instanceof SQLException) {
//            return new ModelAndView("/error/sqlException");
//        }
        return new ModelAndView("/error/exception");
    }
}
