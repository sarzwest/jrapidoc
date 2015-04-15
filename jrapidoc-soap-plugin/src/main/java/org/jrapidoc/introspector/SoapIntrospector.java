package org.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.APIModel;
import org.jrapidoc.model.handler.ModelHandler;
import org.jrapidoc.model.type.provider.JacksonJaxbJsonProvider;
import org.jrapidoc.model.type.provider.TypeProvider;

import javax.jws.WebService;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by papa on 7.4.15.
 */
public class SoapIntrospector extends AbstractIntrospector{

    @Override
    public void run(URL[] urlsForClassloader, List<String> include, List<String> exclude, String basePath, String typeProviderClass, File output, List<String> modelHandlerClasses, Map<String, String> customInfo) throws Exception {
        Logger.debug("Introspection started");
        createOutputDir(output);
        List<ModelHandler> modelHandlers = getModelHandlers(modelHandlerClasses);
        URLClassLoader loader = getProjectUrlClassLoader(urlsForClassloader);
        Set<Class<?>> seiClasses = getScannedClasses(include, exclude, loader, WebService.class);
        seiClasses = removeInterfaces(seiClasses);
        seiClasses = removeExcludedResourceClasses(exclude, seiClasses);
        APIModel apiModel = createModel(customInfo, basePath, seiClasses, typeProviderClass, loader);
        processHandlers(modelHandlers, apiModel);
        writeModelToFile(apiModel, output);
        Logger.debug("Introspection finished");
    }

    APIModel createModel(Map<String, String> customInfo, String basePath, Set<Class<?>> seiClasses, String typeProviderClass, URLClassLoader loader) throws ClassNotFoundException {
        TypeProvider typeProvider = getTypeProvider(typeProviderClass);
        SEIProcessor seiProcessor = getSeiClassProcessor(typeProviderClass, loader);
        APIModel.APIModelBuilder APIModelBuilder = new APIModel.APIModelBuilder();
        addCustomInfo(customInfo, APIModelBuilder);
        APIModelBuilder.baseUrl(basePath);
        seiProcessor.createApiModel(seiClasses, APIModelBuilder);
        APIModelBuilder.types(typeProvider.getUsedTypes());
        return APIModelBuilder.build();
    }

    void addCustomInfo(Map<String, String> customInfo, APIModel.APIModelBuilder APIModelBuilder) {
        if (customInfo != null || !customInfo.isEmpty()) {
            for (String key : customInfo.keySet()) {
                APIModelBuilder.customInfo(key, customInfo.get(key));
            }
        }
    }

    SEIProcessor getSeiClassProcessor(String typeProviderClass, URLClassLoader loader) {
        TypeProvider typeProvider = getTypeProvider((StringUtils.isEmpty(typeProviderClass) ? JacksonJaxbJsonProvider.class.getCanonicalName() : typeProviderClass));
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
