package org.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.jrapidoc.exception.JrapidocExecutionException;
import org.jrapidoc.exception.JrapidocFailureException;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.APIModel;
import org.jrapidoc.model.ServiceGroup;
import org.jrapidoc.model.handler.ModelHandler;
import org.jrapidoc.model.type.provider.JacksonJaxbJsonProvider;
import org.jrapidoc.model.type.provider.JacksonJaxbProvider;
import org.jrapidoc.model.type.provider.TypeProvider;
import org.jrapidoc.model.type.provider.TypeProviderFactory;
import org.jrapidoc.plugin.ConfigGroup;

import javax.jws.WebService;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 7.4.15.
 */
public class SoapIntrospector extends AbstractIntrospector {

    @Override
    public void run(URL[] urlsForClassloader, List<ConfigGroup> groups, String typeProviderClass, File output, List<String> modelHandlerClasses, Map<String, String> customInfo) throws JrapidocExecutionException, JrapidocFailureException {
        Logger.info("");
        Logger.info("Introspection started");
        Logger.info("");
        setUp(groups, output);
        List<ModelHandler> modelHandlers = getModelHandlers(modelHandlerClasses);
        URLClassLoader loader = getProjectUrlClassLoader(urlsForClassloader);
//        Set<Class<?>> seiClasses = getScannedClasses(include, exclude, loader, WebService.class);
//        seiClasses = removeInterfaces(seiClasses);
//        seiClasses = removeExcludedResourceClasses(exclude, seiClasses);
        APIModel apiModel = createModel(customInfo, groups, loader, typeProviderClass);
        processHandlers(modelHandlers, apiModel);
        writeModelToFile(apiModel, output);
        Logger.info("");
        Logger.info("Introspection finished");
        Logger.info("");
    }

    APIModel createModel(Map<String, String> customInfo, List<ConfigGroup> groups, URLClassLoader loader, String typeProviderClass) throws JrapidocExecutionException, JrapidocFailureException {
        try {
            TypeProvider typeProvider = getTypeProvider(typeProviderClass);
            SEIProcessor seiProcessor = getSeiClassProcessor(typeProvider, loader);
            APIModel.APIModelBuilder APIModelBuilder = new APIModel.APIModelBuilder();
            addCustomInfo(customInfo, APIModelBuilder);
            addServiceGroups(groups, seiProcessor, loader, APIModelBuilder);
            APIModelBuilder.types(typeProvider.getUsedTypes());
            return APIModelBuilder.build();
        } catch (Exception e) {
            Logger.error(e, "Unexpected error during creating model");
            throw new JrapidocFailureException(e.getMessage(), e);
        }
    }

    TypeProvider getTypeProvider(String typeProviderClass) {
        return TypeProviderFactory.createTypeProvider((StringUtils.isEmpty(typeProviderClass) ? JacksonJaxbProvider.class.getCanonicalName() : typeProviderClass));
    }

    ServiceGroup createServiceGroup(String basePath, String description, Set<Class<?>> resourceClasses, SEIProcessor seiProcessor) throws JrapidocExecutionException {
        ServiceGroup.ServiceGroupBuilder serviceGroupBuilder = new ServiceGroup.ServiceGroupBuilder();
        serviceGroupBuilder.baseUrl(basePath);
        serviceGroupBuilder.description(description);
        return seiProcessor.createServiceGroup(resourceClasses, serviceGroupBuilder);
    }

    void addServiceGroups(List<ConfigGroup> groups, SEIProcessor seiProcessor, URLClassLoader loader, APIModel.APIModelBuilder APIModelBuilder) throws JrapidocExecutionException {
        for (ConfigGroup group : groups) {
            Logger.info("Service group {0} processing started", group.getBaseUrl());
            Set<Class<?>> resourceClasses = getScannedClasses(group.getIncludes(), group.getExcludes(), loader, WebService.class);
            resourceClasses = removeInterfaces(resourceClasses);
            ServiceGroup serviceGroup = createServiceGroup(group.getBaseUrl(), group.getDescription(), resourceClasses, seiProcessor);
            APIModelBuilder.resourceGroup(serviceGroup);
            Logger.info("Service group {0} processing finished", group.getBaseUrl());
        }
    }

    SEIProcessor getSeiClassProcessor(TypeProvider typeProvider, URLClassLoader loader) {
        return new SEIProcessor(typeProvider, loader);
    }

    Set<Class<?>> removeInterfaces(Set<Class<?>> seiClasses) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for (Class<?> clazz : seiClasses) {
            if (!clazz.isInterface()) {
                classes.add(clazz);
            }
        }
        return classes;
    }
}
