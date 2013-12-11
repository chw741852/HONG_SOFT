package com.hong.manager.module.controller;

import com.hong.core.generic.service.IGenericService;
import com.hong.manager.module.domain.Module;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-11-29
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */
public class ModuleControllerHelper {
    private IGenericService genericService;

    public ModuleControllerHelper(IGenericService genericService) {
        this.genericService = genericService;
    }

    public void findChildren(String id, Map map) {
        String sql = "select id, name as text from sys_module where parentId='" + map.get("id").toString()
                + "' and id<>'" + id + "'";
        List<Map> childrenMap = genericService.executeSqlToRecordMap(sql);
            if (childrenMap != null && childrenMap.size() > 0) {
            map.put("children", childrenMap);
            for (Map child:childrenMap) {
                this.findChildren(id, child);
            }
        }
    }

    public boolean deleteChildren(Module module) {
        String hql = "from " + Module.class.getName() + " a where a.parentId=" + module.getId();
        List<Module> childNodes = genericService.executeObjectSql(hql);
        if (childNodes != null && childNodes.size() > 0) {
            for(Module child:childNodes) {
                if(deleteChildren(child) == false) {
                    return false;
                }
            }
        }
        return genericService.deleteObject(module);
    }
}
