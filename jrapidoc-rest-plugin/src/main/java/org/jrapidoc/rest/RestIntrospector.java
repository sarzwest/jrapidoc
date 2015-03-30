package org.jrapidoc.rest;

import org.jrapidoc.processor.ResourceClassProcessor;
import org.jboss.resteasy.spi.metadata.ResourceBuilder;
import org.jboss.resteasy.spi.metadata.ResourceClass;
import org.jrapidoc.model.type.TypeProvider;
import org.jrapidoc.model.type.TypeProviderFactory;
import org.reflections.Reflections;

import javax.ws.rs.Path;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by papa on 23.3.15.
 */
public class RestIntrospector {

    public void introspect(URL[] urlsForClassloader, List<String> include, List<String> exclude, String basePath, String typeProviderClass) {
        URLClassLoader loader = new URLClassLoader(urlsForClassloader,
                Thread.currentThread().getContextClassLoader());
        // ... and now you can pass the above classloader to Reflections
        Reflections ref = getUnionOfIncludedPaths(include, loader);
        Set<Class<?>> resourceClassesAll = ref.getTypesAnnotatedWith(Path.class);
        Set<Class<?>> resourceClasses = removeExcludedResourceClasses(exclude, resourceClassesAll);
        Set<ResourceClass> resourceClassesMeta = new HashSet<ResourceClass>();
        for (Class<?> rootResourceClass : resourceClasses) {
            ResourceClass resourceClass = ResourceBuilder.rootResourceFromAnnotations(rootResourceClass);
            resourceClassesMeta.add(resourceClass);
        }
        TypeProvider typeProvider = TypeProviderFactory.createTypeProvider(typeProviderClass);
        ResourceClassProcessor resourceClassProcessor = new ResourceClassProcessor(typeProvider);
        resourceClassProcessor.createListing(resourceClassesMeta, basePath);
    }

    Set<Class<?>> removeExcludedResourceClasses(List<String> exclude, Set<Class<?>> resourceClasses) {
        Set<Class<?>> resourceClassesFiltered = new HashSet<Class<?>>(resourceClasses);
        for (String excludePath:exclude){
            for (Class<?> resourceClass: resourceClasses){
                if(resourceClass.getCanonicalName().startsWith(excludePath)){
                    resourceClassesFiltered.remove(resourceClass);
                }
            }
        }
        return resourceClassesFiltered;
    }

    Reflections getUnionOfIncludedPaths(List<String> include, ClassLoader loader) {
        Reflections ref;
        if (include.isEmpty()) {
            ref = new Reflections("", loader);
        } else {
            ref = new Reflections(include.remove(0), loader);
            for (String includePath : include) {
                ref.merge(new Reflections(includePath, loader));
            }
        }
        return ref;
    }
}
