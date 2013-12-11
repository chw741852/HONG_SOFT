package com.hong.test.junit;

import com.hong.core.generic.service.IGenericService;
import com.hong.core.query.domain.SysQueryTables;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-11-11
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:../config/spring/spring-base.xml",
        "classpath*:../config/spring/spring-servlet.xml"
})
public class GenericServiceTest {
    @Autowired
    private IGenericService genericService;

    @Test
    public void testSave() {
        SysQueryTables obj = new SysQueryTables();
        obj.setTableName("SYS_QUERY_TABLES");
        obj.setTableLabel("queryTable");
        genericService.saveObject(obj);
        System.out.print(obj.getId());
    }
}
