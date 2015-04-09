package org.jrapidoc.soap;

import org.apache.commons.lang3.StringUtils;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.ResourceListing;
import org.jrapidoc.model.generator.ModelGenerator;
import org.jrapidoc.model.type.JaxbTypeProvider;
import org.jrapidoc.model.type.TypeProvider;
import org.jrapidoc.model.type.TypeProviderFactory;
import org.jrapidoc.processor.SEIProcessor;
import org.reflections.Reflections;

import javax.jws.WebService;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by papa on 7.4.15.
 */
public class SoapIntrospector {

    public void introspect(URL[] urlsForClassloader, List<String> include, List<String> exclude, String basePath, String typeProviderClass, File output) throws ClassNotFoundException {
        Logger.debug("Introspection started");
        URLClassLoader loader = new URLClassLoader(urlsForClassloader,
                Thread.currentThread().getContextClassLoader());
        // ... and now you can pass the above classloader to Reflections
        Reflections ref = getUnionOfIncludedPaths(include, loader);
        Set<Class<?>> seiClassesAll = ref.getTypesAnnotatedWith(WebService.class);
        Logger.debug(MessageFormat.format("Service endpoint interface classes on path: {0}", new Object[]{seiClassesAll}));
        Set<Class<?>> seiClasses = removeInterfaces(seiClassesAll);
        seiClasses = removeExcludedResourceClasses(exclude, seiClasses);

        TypeProvider typeProvider = TypeProviderFactory.createTypeProvider((StringUtils.isEmpty(typeProviderClass) ? JaxbTypeProvider.class.getCanonicalName() : typeProviderClass));
        SEIProcessor seiProcessor = new SEIProcessor(typeProvider, loader);
        ResourceListing listing = seiProcessor.createListing(seiClasses, basePath);
        output.getParentFile().mkdirs();
        ModelGenerator.generateModel(listing, output);
        Logger.debug("Introspection finished");
    }

    Set<Class<?>> removeInterfaces(Set<Class<?>> seiClasses) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for(Class<?> clazz:seiClasses){
            if(!clazz.isInterface()){
                classes.add(clazz);
            }
        }
        return classes;
    }

    Set<Class<?>> removeExcludedResourceClasses(List<String> exclude, Set<Class<?>> resourceClasses) {
        Set<Class<?>> resourceClassesFiltered = new HashSet<Class<?>>(resourceClasses);
        for (String excludePath : exclude) {
            for (Class<?> resourceClass : resourceClasses) {
                if (resourceClass.getCanonicalName().startsWith(excludePath)) {
                    Logger.debug(MessageFormat.format("Removing class {0} from scan (it is in exclude property)", new Object[]{resourceClass.getCanonicalName()}));
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
                Logger.debug(MessageFormat.format("Adding path {0} to scan", new Object[]{includePath}));
                ref.merge(new Reflections(includePath, loader));
            }
        }
        return ref;
    }
}
