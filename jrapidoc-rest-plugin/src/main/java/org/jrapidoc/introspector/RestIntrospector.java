package org.jrapidoc.introspector;

import org.jboss.resteasy.spi.metadata.ResourceBuilder;
import org.jboss.resteasy.spi.metadata.ResourceClass;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.ResourceListing;
import org.jrapidoc.model.handler.ModelHandler;
import org.jrapidoc.model.type.provider.TypeProvider;
import org.jrapidoc.model.type.provider.TypeProviderFactory;
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
public class RestIntrospector extends AbstractIntrospector {

    @Override
    public void run(URL[] urlsForClassloader, List<String> include, List<String> exclude, String basePath, String typeProviderClass, File output, List<String> modelHandlerClasses) throws Exception {
        Logger.debug("Introspection started");
        createOutputDir(output);
        List<ModelHandler> modelHandlers = getModelHandlers(modelHandlerClasses);
        URLClassLoader loader = getProjectUrlClassLoader(urlsForClassloader);
        Set<Class<?>> resourceClasses = getScannedClasses(include, exclude, loader, Path.class);
        Set<ResourceClass> resourceClassesMeta = doPreIntrospection(resourceClasses);
        ResourceClassProcessor resourceClassProcessor = getResourceClassProcessor(typeProviderClass);
        ResourceListing listing = createModel(basePath, resourceClassesMeta, resourceClassProcessor);
        //TODO add types to model
        processHandlers(modelHandlers, listing);
        writeModelToFile(listing, output);
        Logger.debug("Introspection finished");
    }

    ResourceListing createModel(String basePath, Set<ResourceClass> resourceClassesMeta, ResourceClassProcessor resourceClassProcessor) {
        return resourceClassProcessor.createListing(resourceClassesMeta, basePath);
    }

    ResourceClassProcessor getResourceClassProcessor(String typeProviderClass) {
        TypeProvider typeProvider = getTypeProvider(typeProviderClass);
        return new ResourceClassProcessor(typeProvider);
    }

    Set<ResourceClass> doPreIntrospection(Set<Class<?>> resourceClasses) {
        Set<ResourceClass> resourceClassesMeta = new HashSet<ResourceClass>();
        for (Class<?> rootResourceClass : resourceClasses) {
            ResourceClass resourceClass = ResourceBuilder.rootResourceFromAnnotations(rootResourceClass);
            resourceClassesMeta.add(resourceClass);
        }
        return resourceClassesMeta;
    }
}
