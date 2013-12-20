package com.hong.test.junit;

import com.hong.core.generic.service.IGenericService;
import com.hong.core.security.domain.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-11-11
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:../config/spring/spring-base.xml",
        "classpath:../config/spring/spring-servlet.xml"
})
public class GenericServiceTest {
    @Resource(name = "genericService")
    private IGenericService publicService;

    @Test
    public void testSave() {
        Role role = new Role();
        role.setEnabled(true);
        role.setName("ROLE_USER");
        role.setSequence(1);
        publicService.saveObject(role);
    }
}
