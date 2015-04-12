package org.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.ResourceListing;
import org.jrapidoc.model.generator.ModelGenerator;
import org.jrapidoc.model.handler.HandlerFactory;
import org.jrapidoc.model.handler.ModelHandler;
import org.jrapidoc.model.type.provider.JacksonJaxbJsonProvider;
import org.jrapidoc.model.type.provider.TypeProvider;
import org.jrapidoc.model.type.provider.TypeProviderFactory;
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
public class SoapIntrospector extends AbstractIntrospector{

    @Override
    public void run(URL[] urlsForClassloader, List<String> include, List<String> exclude, String basePath, String typeProviderClass, File output, List<String> modelHandlerClasses) throws Exception {
        Logger.debug("Introspection started");
        createOutputDir(output);
        List<ModelHandler> modelHandlers = getModelHandlers(modelHandlerClasses);
        URLClassLoader loader = getProjectUrlClassLoader(urlsForClassloader);
        Set<Class<?>> seiClasses = getScannedClasses(include, exclude, loader, WebService.class);
        seiClasses = removeInterfaces(seiClasses);
        seiClasses = removeExcludedResourceClasses(exclude, seiClasses);
        SEIProcessor seiProcessor = getSeiClassProcessor(typeProviderClass, loader);
        ResourceListing listing = createModel(basePath, seiClasses, seiProcessor);
        processHandlers(modelHandlers, listing);
        writeModelToFile(listing, output);
        Logger.debug("Introspection finished");
    }

    ResourceListing createModel(String basePath, Set<Class<?>> seiClasses, SEIProcessor seiProcessor) throws ClassNotFoundException {
        return seiProcessor.createListing(seiClasses, basePath);
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
