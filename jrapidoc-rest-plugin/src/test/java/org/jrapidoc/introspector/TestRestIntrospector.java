package org.jrapidoc.introspector;

import airservice.resources.TestResource;
import org.jrapidoc.RestUtil;
import org.jrapidoc.exception.JrapidocFailureException;
import org.jrapidoc.model.APIModel;
import org.jrapidoc.model.Method;
import org.jrapidoc.model.Service;
import org.jrapidoc.model.ServiceGroup;
import org.jrapidoc.plugin.ConfigGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.3.15.
 */
public class TestRestIntrospector {

    public static final String DEFAULT_GROUP_BASE_URL = "http://localhost:9080/airservice/rest";
    RestIntrospector restIntrospector;
    ConfigGroup configGroup;
    List<ConfigGroup> configGroups;
    APIModel apiModel;


    @Before
    public void init() throws JrapidocFailureException {
        restIntrospector = new RestIntrospector();
        createConfigGroup();
        createConfigGroups();
        apiModel = restIntrospector.createModel(null, configGroups, Thread.currentThread().getContextClassLoader(), null);
    }

    void createConfigGroups() {
        configGroups = new ArrayList<ConfigGroup>();
        configGroups.add(configGroup);
    }

    void createConfigGroup() {
        configGroup = new ConfigGroup();
        configGroup.setBaseUrl(DEFAULT_GROUP_BASE_URL);
        configGroup.setIncludes(new ArrayList<String>(Arrays.asList(new String[]{"airservice.resources"})));
    }

    @Test
    public void test(){
        Reflections reflections = restIntrospector.getUnionOfIncludedPaths(new ArrayList<String>(Arrays.asList(new String[]{"airservice.resources"})), Thread.currentThread().getContextClassLoader());
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(Path.class);
        Assert.assertEquals(true, resourceClasses.contains(TestResource.class));
    }

    @Test
    public void test2(){
        Reflections reflections = restIntrospector.getUnionOfIncludedPaths(new ArrayList<String>(Arrays.asList(new String[]{"airservice.resources"})), Thread.currentThread().getContextClassLoader());
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(Path.class);
        resourceClasses = restIntrospector.removeExcludedResourceClasses(new ArrayList<String>(Arrays.asList(new String[]{"airservice.resources.TestResource"})), resourceClasses);
        Assert.assertEquals(false, resourceClasses.contains(TestResource.class));
    }

    @Test
    public void testPathExampleOnResourceClass() throws JrapidocFailureException {
        ServiceGroup serviceGroup = apiModel.getServiceGroups().get(DEFAULT_GROUP_BASE_URL);
        Service service = serviceGroup.getServices().get("test/{pathparam}");
        Assert.assertEquals("test/5", service.getPathExample());
    }

    @Test
    public void testPathExampleOnResourceClass1() throws JrapidocFailureException {
        ServiceGroup serviceGroup = apiModel.getServiceGroups().get(DEFAULT_GROUP_BASE_URL);
        Service service = serviceGroup.getServices().get("type");
        Assert.assertEquals(null, service.getPathExample());
    }

    @Test
    public void testPathExampleOnResourceMethod() throws JrapidocFailureException {
        ServiceGroup serviceGroup = apiModel.getServiceGroups().get(DEFAULT_GROUP_BASE_URL);
        Service service = serviceGroup.getServices().get("test/{pathparam}");
        Method method = service.getMethods().get(RestUtil.trimSlash("test/{pathparam}/pathExample/[a-z]{3}") + " - GET");
        Assert.assertEquals("test/5/pathExample/aaa", method.getPathExample());
    }
}
