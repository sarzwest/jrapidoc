package org.jrapidoc.introspector;

import airservice.resources.TestResource;
import org.jrapidoc.exception.JrapidocFailureException;
import org.jrapidoc.introspector.RestIntrospector;
import org.jrapidoc.model.APIModel;
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

    RestIntrospector restIntrospector;
    ConfigGroup configGroup;
    List<ConfigGroup> configGroups;

    @Before
    public void init(){
        restIntrospector = new RestIntrospector();
        createConfigGroup();
        createConfigGroups();
    }

    void createConfigGroups() {
        configGroups = new ArrayList<ConfigGroup>();
        configGroups.add(configGroup);
    }

    void createConfigGroup() {
        configGroup = new ConfigGroup();
        configGroup.setBaseUrl("localhost/airservice");
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
    public void test3() throws JrapidocFailureException {
        APIModel apiModel = restIntrospector.createModel(null, configGroups, Thread.currentThread().getContextClassLoader(), null);
//        apiModel.
    }
}
