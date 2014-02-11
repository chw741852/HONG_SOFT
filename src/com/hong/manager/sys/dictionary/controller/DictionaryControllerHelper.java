package com.hong.manager.sys.dictionary.controller;

import com.hong.core.generic.service.IGenericService;
import com.hong.manager.sys.dictionary.domain.SysDictionary;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hong on 14-1-18 下午6:33.
 */
public class DictionaryControllerHelper {
    private IGenericService genericService;

    public DictionaryControllerHelper(IGenericService genericService) {
        this.genericService = genericService;
    }

    public void findChildren(String id, Map map) {
        String sql = "select id, name as text from sys_dictionary where parentId='" + map.get("id").toString()
                + "' and id<>'" + id + "'";
        List<Map> childrenMap = genericService.executeSqlToRecordMap(sql);
        if (childrenMap != null && childrenMap.size() > 0) {
            map.put("children", childrenMap);
            for (Map child:childrenMap) {
                this.findChildren(id, child);
            }
        }
    }

    public boolean deleteObjects(SysDictionary parent) {
        if (parent.getChildren().size() > 0) {
            for (SysDictionary child:parent.getChildren()) {
                if (!deleteObjects(child))
                    return false;
            }
            return genericService.deleteObject(parent);
        } else {
            return genericService.deleteObject(parent);
        }
    }
}
