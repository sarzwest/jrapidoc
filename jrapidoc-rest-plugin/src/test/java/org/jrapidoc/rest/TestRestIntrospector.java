package org.jrapidoc.rest;

import org.junit.Test;
import org.reflections.Reflections;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by papa on 23.3.15.
 */
public class TestRestIntrospector {

    @Test
    public void test(){
        RestIntrospector restIntrospector = new RestIntrospector();
        Reflections reflections = restIntrospector.getUnionOfIncludedPaths(new ArrayList<String>(Arrays.asList(new String[]{"org.jrapidoc.test"})), Thread.currentThread().getContextClassLoader());
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(Path.class);
        for (Class<?> aClass : resourceClasses) {
            System.out.println(aClass.getCanonicalName());
        }
    }

    @Test
    public void test2(){
        RestIntrospector restIntrospector = new RestIntrospector();
        Reflections reflections = restIntrospector.getUnionOfIncludedPaths(new ArrayList<String>(Arrays.asList(new String[]{"org.jrapidoc.test"})), Thread.currentThread().getContextClassLoader());
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(Path.class);

        Set<Class<?>> resourceClassesFiltered = restIntrospector.removeExcludedResourceClasses(new ArrayList<String>(Arrays.asList(new String[]{"org.jrapidoc.test.TestResource2"})), resourceClasses);
        for (Class<?> aClass : resourceClassesFiltered) {
            System.out.println(aClass.getCanonicalName());
        }
    }
}
