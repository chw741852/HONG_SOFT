package com.hong.manager.sys.module.controller;

import com.hong.core.generic.service.IGenericService;
import com.hong.manager.sys.module.domain.SysModule;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-11-29
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */
public class SysModuleControllerHelper {
    private IGenericService genericService;

    public SysModuleControllerHelper(IGenericService genericService) {
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

    public boolean deleteObjects(SysModule parent) {
        if (parent.getChildren().size() > 0) {
            for (SysModule child:parent.getChildren()) {
                if (!deleteObjects(child))
                    return false;
            }
            return genericService.deleteObject(parent);
        } else {
            return genericService.deleteObject(parent);
        }
    }
}
