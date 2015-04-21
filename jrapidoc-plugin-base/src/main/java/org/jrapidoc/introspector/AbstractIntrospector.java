package org.jrapidoc.introspector;

import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.APIModel;
import org.jrapidoc.model.generator.ModelGenerator;
import org.jrapidoc.model.handler.HandlerException;
import org.jrapidoc.model.handler.HandlerFactory;
import org.jrapidoc.model.handler.ModelHandler;
import org.jrapidoc.model.type.provider.TypeProvider;
import org.jrapidoc.model.type.provider.TypeProviderFactory;
import org.jrapidoc.plugin.ConfigGroup;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 12.4.15.
 */
public abstract class AbstractIntrospector {

    public abstract void run(URL[] urlsForClassloader, List<ConfigGroup> groups, String typeProviderClass, File output, List<String> modelHandlerClasses, Map<String, String> custom) throws Exception;

    void writeModelToFile(APIModel apiModel, File output) throws FileNotFoundException {
        ModelGenerator.generateModel(apiModel, output);
    }

    void processHandlers(List<ModelHandler> modelHandlers, APIModel apiModel) throws Exception {
        if(modelHandlers == null || modelHandlers.isEmpty()){
            Logger.debug("No model handlers found");
            return;
        }
        for (ModelHandler modelHandler:modelHandlers){
            try {
                modelHandler.handleModel(apiModel);
            }catch (HandlerException e){
                if(e.getBehaviour() == HandlerException.Action.CONTINUE){
                    Logger.info("Exception occured in handler {0}, continue with processing next handler", modelHandler.getClass().getCanonicalName());
                }else if(e.getBehaviour() == HandlerException.Action.STOP_HANDLERS){
                    Logger.warn(e, "Exception occured in handler {0}, skipping handlers processing", modelHandler.getClass().getCanonicalName());
                }else if (e.getBehaviour() == HandlerException.Action.STOP_ALL){
                    Logger.error(e, "Exception occured in handler {0}, stopping plugin", modelHandler.getClass().getCanonicalName());
                    throw new Exception(e);
                }
            }
        }
    }

    void addCustomInfo(Map<String, String> customInfo, APIModel.APIModelBuilder APIModelBuilder) {
        if (customInfo != null && !customInfo.isEmpty()) {
            for (String key : customInfo.keySet()) {
                APIModelBuilder.customInfo(key, customInfo.get(key));
            }
        }
    }

    List<ModelHandler> getModelHandlers(List<String> modelHandlerClasses) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return HandlerFactory.createModelHandlers(modelHandlerClasses);
    }

    void createOutputDir(File output) throws Exception {
        if (!output.getParentFile().canWrite()) {
            if (!output.getParentFile().mkdirs()) {
                Logger.error("Directory {0} could not be created", output.getParentFile().getAbsolutePath());
                throw new Exception("Directory could not be created");
            }
        }
    }

    TypeProvider getTypeProvider(String typeProviderClass) {
        return TypeProviderFactory.createTypeProvider(typeProviderClass);
    }

    Set<Class<?>> getScannedClasses(List<String> include, List<String> exclude, URLClassLoader loader, Class<? extends Annotation> annotatedWith) {
        Reflections ref = getUnionOfIncludedPaths(include, loader);
        Set<Class<?>> resourceClassesAll = ref.getTypesAnnotatedWith(annotatedWith);
        Logger.debug("Root resource classes on path: {0}", resourceClassesAll.toString());
        return removeExcludedResourceClasses(exclude, resourceClassesAll);
    }

    URLClassLoader getProjectUrlClassLoader(URL[] urlsForClassloader) {
        return new URLClassLoader(urlsForClassloader,
                Thread.currentThread().getContextClassLoader());
    }

    Set<Class<?>> removeExcludedResourceClasses(List<String> exclude, Set<Class<?>> resourceClasses) {
        Set<Class<?>> resourceClassesFiltered = new HashSet<Class<?>>(resourceClasses);
        if(exclude == null || exclude.isEmpty()){
            return resourceClasses;
        }
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
        if (include == null || include.isEmpty()) {
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

    void checkIncludeConfigNotEmpty(List<String> include) throws Exception {
        if(include == null || include.isEmpty()){
            Logger.error("Please specify packages with API\n" +
                    "Add missing plugin configuration:\n" +
                    "\n" +
                    "<includes>\n" +
                    "   <include>package.with.api</include>\n" +
                    "   <include>another.package.with.api</include>\n" +
                    "</includes>");
            throw new Exception("\"include\" packages are not configured");
        }
    }

    void setUp(List<ConfigGroup> groups, File output) throws Exception {
        createOutputDir(output);
        if(groups == null){
            Logger.error("Groups element in configuration is null");
            throw new Exception("Groups element in configuration is null");
        }
        for (ConfigGroup group:groups){
            if(group == null){
                Logger.error("Group element in groups element is null");
                throw new Exception("Group element in groups element is null");
            }
            checkIncludeConfigNotEmpty(group.getIncludes());
        }
    }
}
