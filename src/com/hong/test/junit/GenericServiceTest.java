package com.hong.test.junit;

import com.hong.core.generic.service.IGenericService;
import com.hong.core.security.domain.Authority;
import com.hong.core.security.domain.Resource;
import com.hong.core.security.domain.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-11-11
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:web/WEB-INF/config/spring/spring-base.xml",
        "file:web/WEB-INF/config/spring/spring-servlet.xml"
})
/*
 * 需要将spring-base.xml中的propertyConfig路径改为‘file:web/..’形式才可以运行
 */
public class GenericServiceTest {
    @Autowired
    private IGenericService genericService;

    @Test
    public void testSave() {
        Role role = new Role();
        role.setEnabled(true);
        role.setName("ROLE_USER");
        role.setSequence(1);
        genericService.saveObject(role);
    }

    @Test
    public void testAuthorityResource() {
        Authority authority = (Authority) genericService.lookUp(Authority.class, 1);
        Resource resource = (Resource)genericService.lookUp(Resource.class, 1);

        Set<Resource> resources = new TreeSet<Resource>();
        resources.add(resource);

        authority.setResources(resources);
        genericService.updateObject(authority);
    }
}
