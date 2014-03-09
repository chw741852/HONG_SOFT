package com.hong.core.security.controller;

import com.hong.core.generic.service.IGenericService;

import java.util.List;
import java.util.Map;

/**
 * Created by hong on 14-2-28 下午4:09.
 */
public class ResourceControllerHelper {
    private IGenericService genericService;

    public ResourceControllerHelper(IGenericService genericService) {
        this.genericService = genericService;
    }

    public void findChildren(String id, Map map) {
        String sql = "select id, name as text from sys_resource where parentId='" + map.get("id").toString()
                + "' and id<>'" + id + "'";
        List<Map> childrenMap = genericService.executeSqlToRecordMap(sql);
        if (childrenMap != null && childrenMap.size() > 0) {
            map.put("children", childrenMap);
            for (Map child:childrenMap) {
                this.findChildren(id, child);
            }
        }
    }
}
