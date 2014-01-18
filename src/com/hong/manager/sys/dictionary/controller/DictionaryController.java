package com.hong.manager.sys.dictionary.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hong.core.generic.domain.IdEntity;
import com.hong.core.generic.service.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 14-1-10
 * Time: 下午1:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/manager/sys/dictionary")
public class DictionaryController extends IdEntity {
    private final String BASEPATH = "/manager/sys/dictionary";

    @Autowired
    private IGenericService genericService;

    @RequestMapping(value = "/browse")
    public ModelAndView browse() {
        ModelAndView mv = new ModelAndView(BASEPATH + "/browse");

        String sql = "select id, isnull(parentId, 0) pId, name from sys_dictionary order by sequence, id";
        List<Map> dictList = genericService.executeSqlToRecordMap(sql);
        mv.addObject("nodes", JSON.toJSONString(dictList, SerializerFeature.UseSingleQuotes));

        return mv;
    }

    @RequestMapping(value = "/ajaxFindModule")
    public void ajaxFindModule(HttpServletRequest request) {

    }
}
