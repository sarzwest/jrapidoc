package org.jrapidoc.introspector;

import org.jboss.resteasy.spi.metadata.ResourceBuilder;
import org.jboss.resteasy.spi.metadata.ResourceClass;
import org.jrapidoc.exception.JrapidocExecutionException;
import org.jrapidoc.exception.JrapidocFailureException;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.APIModel;
import org.jrapidoc.model.ServiceGroup;
import org.jrapidoc.model.handler.ModelHandler;
import org.jrapidoc.model.type.provider.TypeProvider;
import org.jrapidoc.plugin.ConfigGroup;

import javax.ws.rs.Path;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.3.15.
 */
public class RestIntrospector extends AbstractIntrospector {

    @Override
    public void run(URL[] urlsForClassloader, List<ConfigGroup> groups, String typeProviderClass, File output, List<String> modelHandlerClasses, Map<String, String> customInfo) throws JrapidocExecutionException, JrapidocFailureException {
        Logger.debug("Introspection started");
        setUp(groups, output);
        List<ModelHandler> modelHandlers = getModelHandlers(modelHandlerClasses);
        URLClassLoader loader = getProjectUrlClassLoader(urlsForClassloader);
        APIModel apiModel = createModel(customInfo, groups, loader, typeProviderClass);
        processHandlers(modelHandlers, apiModel);
        writeModelToFile(apiModel, output);
        Logger.debug("Introspection finished");
    }

    ServiceGroup createServiceGroup(String basePath, String description, Set<Class<?>> resourceClasses, ResourceClassProcessor resourceClassProcessor) {
        ServiceGroup.ServiceGroupBuilder serviceGroupBuilder = new ServiceGroup.ServiceGroupBuilder();
        serviceGroupBuilder.baseUrl(basePath);
        serviceGroupBuilder.description(description);
        Set<ResourceClass> resourceClassesMeta = doPreIntrospection(resourceClasses);
        return resourceClassProcessor.createServiceGroup(resourceClassesMeta, serviceGroupBuilder);
    }

    APIModel createModel(Map<String, String> customInfo, List<ConfigGroup> groups, URLClassLoader loader, String typeProviderClass) throws JrapidocFailureException {
        try {
            TypeProvider typeProvider = getTypeProvider(typeProviderClass);
            ResourceClassProcessor resourceClassProcessor = getResourceClassProcessor(typeProvider);
            APIModel.APIModelBuilder APIModelBuilder = new APIModel.APIModelBuilder();
            addCustomInfo(customInfo, APIModelBuilder);
            addServiceGroups(groups, resourceClassProcessor, loader, APIModelBuilder);
            APIModelBuilder.types(typeProvider.getUsedTypes());
            return APIModelBuilder.build();
        }catch (Exception e){
            Logger.error(e, "Unexpected error during creating model");
            throw new JrapidocFailureException(e.getMessage(), e);
        }
    }

    void addServiceGroups(List<ConfigGroup> groups, ResourceClassProcessor resourceClassProcessor, URLClassLoader loader, APIModel.APIModelBuilder APIModelBuilder) {
        for (ConfigGroup group:groups) {
            Set<Class<?>> resourceClasses = getScannedClasses(group.getIncludes(), group.getExcludes(), loader, Path.class);
            ServiceGroup serviceGroup = createServiceGroup(group.getBaseUrl(), group.getDescription(), resourceClasses, resourceClassProcessor);
            APIModelBuilder.resourceGroup(serviceGroup);
        }
    }

    ResourceClassProcessor getResourceClassProcessor(TypeProvider typeProvider) {
        return new ResourceClassProcessor(typeProvider);
    }

    Set<ResourceClass> doPreIntrospection(Set<Class<?>> resourceClasses) {
        Set<ResourceClass> resourceClassesMeta = new HashSet<ResourceClass>();
        for (Class<?> rootResourceClass : resourceClasses) {
            try {
                ResourceClass resourceClass = ResourceBuilder.rootResourceFromAnnotations(rootResourceClass);
                resourceClassesMeta.add(resourceClass);
            }catch (Exception e){
                Logger.error(e, "Problem during preintrospection of class {0}, skipping this resource class", rootResourceClass.getCanonicalName());
            }
        }
        return resourceClassesMeta;
    }
}
