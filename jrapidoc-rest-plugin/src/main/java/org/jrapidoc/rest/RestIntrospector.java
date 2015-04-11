package org.jrapidoc.rest;

import org.jboss.resteasy.spi.metadata.ResourceBuilder;
import org.jboss.resteasy.spi.metadata.ResourceClass;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.ResourceListing;
import org.jrapidoc.model.generator.ModelGenerator;
import org.jrapidoc.model.type.provider.TypeProvider;
import org.jrapidoc.model.type.provider.TypeProviderFactory;
import org.jrapidoc.processor.ResourceClassProcessor;
import org.reflections.Reflections;

import javax.ws.rs.Path;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by papa on 23.3.15.
 */
public class RestIntrospector {

    public void introspect(URL[] urlsForClassloader, List<String> include, List<String> exclude, String basePath, String typeProviderClass, File output) throws Exception {
        Logger.debug("Introspection started");
        URLClassLoader loader = new URLClassLoader(urlsForClassloader,
                Thread.currentThread().getContextClassLoader());
        // ... and now you can pass the above classloader to Reflections
        Reflections ref = getUnionOfIncludedPaths(include, loader);
        Set<Class<?>> resourceClassesAll = ref.getTypesAnnotatedWith(Path.class);
        Logger.debug("Root resource classes on path: {0}", resourceClassesAll.toString());
        Set<Class<?>> resourceClasses = removeExcludedResourceClasses(exclude, resourceClassesAll);
        Set<ResourceClass> resourceClassesMeta = new HashSet<ResourceClass>();
        for (Class<?> rootResourceClass : resourceClasses) {
            ResourceClass resourceClass = ResourceBuilder.rootResourceFromAnnotations(rootResourceClass);
            resourceClassesMeta.add(resourceClass);
        }
        TypeProvider typeProvider = TypeProviderFactory.createTypeProvider(typeProviderClass);
        ResourceClassProcessor resourceClassProcessor = new ResourceClassProcessor(typeProvider);
        ResourceListing listing = resourceClassProcessor.createListing(resourceClassesMeta, basePath);
        //TODO add types to model
        if (!output.getParentFile().canWrite()) {
            if (!output.getParentFile().mkdirs()) {
                Logger.error("Directory {0} could not be created", output.getParentFile().getAbsolutePath());
                throw new Exception("Directory could not be created");
            }
        }
        ModelGenerator.generateModel(listing, output);
        Logger.debug("Introspection finished");
    }

    Set<Class<?>> removeExcludedResourceClasses(List<String> exclude, Set<Class<?>> resourceClasses) {
        Set<Class<?>> resourceClassesFiltered = new HashSet<Class<?>>(resourceClasses);
        for (String excludePath : exclude) {
            for (Class<?> resourceClass : resourceClasses) {
                if (resourceClass.getCanonicalName().startsWith(excludePath)) {
                    Logger.debug("Removing class {0} from scan (it is in exclude property)", resourceClass.getCanonicalName());
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
