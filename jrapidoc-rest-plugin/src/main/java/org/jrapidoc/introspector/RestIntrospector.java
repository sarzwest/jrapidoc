package org.jrapidoc.introspector;

import org.jboss.resteasy.spi.metadata.ResourceBuilder;
import org.jboss.resteasy.spi.metadata.ResourceClass;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.APIModel;
import org.jrapidoc.model.handler.ModelHandler;
import org.jrapidoc.model.type.provider.TypeProvider;

import javax.ws.rs.Path;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
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
        APIModel apiModel = createModel(basePath, resourceClasses, typeProviderClass);
        processHandlers(modelHandlers, apiModel);
        writeModelToFile(apiModel, output);
        Logger.debug("Introspection finished");
    }

    APIModel createModel(String basePath, Set<Class<?>> resourceClasses, String typeProviderClass) {
        TypeProvider typeProvider = getTypeProvider(typeProviderClass);
        ResourceClassProcessor resourceClassProcessor = getResourceClassProcessor(typeProvider);
        APIModel.APIModelBuilder APIModelBuilder = new APIModel.APIModelBuilder();
        APIModelBuilder.baseUrl(basePath);
        Set<ResourceClass> resourceClassesMeta = doPreIntrospection(resourceClasses);
        resourceClassProcessor.createApiModel(resourceClassesMeta, APIModelBuilder);
        APIModelBuilder.types(typeProvider.getUsedTypes());
        return APIModelBuilder.build();
    }

    ResourceClassProcessor getResourceClassProcessor(TypeProvider typeProvider) {
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
