package org.jrapidoc.processor;

import org.apache.commons.lang3.StringUtils;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.Resource;
import org.jrapidoc.model.ResourceListing;
import org.jrapidoc.model.Return;
import org.jrapidoc.model.param.HeaderParam;
import org.jrapidoc.model.type.TypeProvider;

import javax.jws.*;
import javax.xml.ws.Holder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by papa on 7.4.15.
 */
public class SEIProcessor {

    TypeProvider typeProvider;
    URLClassLoader loader;

    public SEIProcessor(TypeProvider typeProvider, URLClassLoader loader) {
        this.typeProvider = typeProvider;
        this.loader = loader;
    }

    public ResourceListing createListing(Set<Class<?>> seiClasses, String basePath) throws ClassNotFoundException {
        List<Resource> resources = new ArrayList<Resource>();
        for (Class<?> seiClass : seiClasses) {
            seiClass = getSEI(seiClass);
            Resource resource = createEndpoint(seiClass);
            resources.add(resource);
        }
        return new ResourceListing(basePath, resources);
    }

    Class<?> getSEI(Class<?> seiClass) throws ClassNotFoundException {
        String interfaceClass = seiClass.getAnnotation(WebService.class).endpointInterface();
        if (StringUtils.isNotEmpty(interfaceClass)) {
            try {
                return loader.loadClass(interfaceClass);
            } catch (ClassNotFoundException e) {
                Logger.error("Endpoint interface {0} for {1} is not on project classpath", interfaceClass, seiClass.getCanonicalName());
                throw e;
            }
        } else {
            return seiClass;
        }
    }

    Resource createEndpoint(Class<?> seiClass) {
        Resource.ResourceBuilder resourceBuilder = new Resource.ResourceBuilder();
        addMethods(seiClass, resourceBuilder);
        return resourceBuilder.build();
    }

    void addMethods(Class<?> seiClass, Resource.ResourceBuilder resourceBuilder) {
        for (Method method : seiClass.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                WebMethod webMetAnnotation = method.getAnnotation(WebMethod.class);
                if (webMetAnnotation == null) {
                    resourceBuilder.method(createMethod(method));
                } else if (!method.getAnnotation(WebMethod.class).exclude()) {
                    resourceBuilder.method(createMethod(method));
                }
            }
        }
    }

    org.jrapidoc.model.Method createMethod(Method method) {
        org.jrapidoc.model.Method.MethodBuilder methodBuilder = new org.jrapidoc.model.Method.MethodBuilder();
        addMethodName(method, methodBuilder);
        addInputHeaders(method, methodBuilder);
        addInputParams(method, methodBuilder);
        addReturn(method, methodBuilder);
        return methodBuilder.build();
    }

    void addReturn(Method method, org.jrapidoc.model.Method.MethodBuilder methodBuilder){
        Return.ReturnBuilder returnBuilder = new Return.ReturnBuilder();
        addOutputHeaders(method, returnBuilder);
        addOutputParams(method, returnBuilder);
        HeaderParam headerParam = new HeaderParam.HeaderParamBuilder().setName(HeaderParam.CONTENT_TYPE).setOptions(new String[]{"application/xml"}).build();
        returnBuilder.httpStatus(200).headerParams(Arrays.asList(new HeaderParam[]{headerParam}));
        Return returnType = returnBuilder.build();
        methodBuilder.returnOptions(new ArrayList<Return>(Arrays.asList(new Return[]{returnType})));
    }

    org.jrapidoc.model.object.type.Type createType(Type param) {
        return typeProvider.createType(param);
    }

    <T extends Annotation> T getAnnotation(Annotation[] annotations, Class<T> annotation) {
        for (Annotation a : annotations) {
            if (a.annotationType().equals(annotation)) {
                return (T) a;
            }
        }
        return null;
    }

    Type extractFromHolder(Type holder) {
        if (holder instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) holder;
            if (paramType.getRawType().equals(Holder.class)) {
                return paramType.getActualTypeArguments()[0];
            }
        }
        return holder;
    }

    void addInputHeaders(Method method, org.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
            Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
            if (isHeader(method.getParameterAnnotations()[i])) {
                if (isInputMode(method.getParameterAnnotations()[i])) {
                    methodBuilder.soapInputHeader(createType(param));
                }
            }
        }
    }

    void addInputParams(Method method, org.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        List<org.jrapidoc.model.object.type.Type> parameterTypes = new ArrayList<org.jrapidoc.model.object.type.Type>();
        for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
            Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
            org.jrapidoc.model.object.type.Type parameterType = typeProvider.createType(param);
            if (isInputMode(method.getParameterAnnotations()[i])) {
                if (!isHeader(method.getParameterAnnotations()[i])) {
                    methodBuilder.parameter(parameterType);
                }
            }
        }
    }

    void addOutputHeaders(Method method, Return.ReturnBuilder returnBuilder) {
        if (!isOneWay(method)) {
            for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
                Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
                if (isHeader(method.getParameterAnnotations()[i])) {
                    if (isOutputMode(method.getParameterAnnotations()[i])) {
                        returnBuilder.soapOutputHeader(createType(param));
                    }
                }
            }
            addHeaderFromReturn(method, returnBuilder);
        }
    }

    void addOutputParams(Method method, Return.ReturnBuilder returnBuilder) {
        if (!isOneWay(method)) {
            List<org.jrapidoc.model.object.type.Type> returnTypes = new ArrayList<org.jrapidoc.model.object.type.Type>();
            for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
                Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
                org.jrapidoc.model.object.type.Type returnType = typeProvider.createType(param);
                if (isOutputMode(method.getParameterAnnotations()[i])) {
                    if (!isHeader(method.getParameterAnnotations()[i])) {
                        returnTypes.add(returnType);
                    }
                }
            }
            addTypeFromReturn(method, returnTypes);
            returnBuilder.returnTypes(returnTypes);
        }
    }

    void addTypeFromReturn(Method method, List<org.jrapidoc.model.object.type.Type> returnTypes) {
        if (!isHeader(method.getDeclaredAnnotations())) {
            if (!method.getGenericReturnType().equals(Void.TYPE)) {
                org.jrapidoc.model.object.type.Type retType = typeProvider.createType(method.getGenericReturnType());
                returnTypes.add(retType);
            }
        }
    }

    void addHeaderFromReturn(Method method, Return.ReturnBuilder returnBuilder) {
        if (isHeader(method.getDeclaredAnnotations())) {
            if (!method.getGenericReturnType().equals(Void.TYPE)) {
                org.jrapidoc.model.object.type.Type outHeader = typeProvider.createType(method.getGenericReturnType());
                returnBuilder.soapOutputHeader(outHeader);
            }
        }
    }

    boolean isHeader(Annotation[] annotations) {
        WebParam webParamAnno = getAnnotation(annotations, WebParam.class);
        WebResult webResultAnno = getAnnotation(annotations, WebResult.class);
        if (webParamAnno == null && webResultAnno == null) {
            return false;
        }
        if (webParamAnno != null) {
            return webParamAnno.header();
        } else if (webResultAnno != null) {
            return webResultAnno.header();
        }
        return false;
    }

    boolean isInputMode(Annotation[] annotations) {
        if (annotations == null) {
            return true;
        }
        for (Annotation a : annotations) {
            if (a.annotationType().equals(WebParam.class)) {
                return ((WebParam) a).mode() != WebParam.Mode.OUT;
            }
        }
        return true;
    }

    boolean isOutputMode(Annotation[] annotations) {
        if (annotations == null) {
            return false;
        }
        for (Annotation a : annotations) {
            if (a.annotationType().equals(WebParam.class)) {
                return ((WebParam) a).mode() != WebParam.Mode.IN;
            }
        }
        return false;
    }

    boolean isOneWay(Method method) {
        return (method.getAnnotation(Oneway.class) != null);
    }

    void addMethodName(Method method, org.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        WebMethod webMetAnno = method.getAnnotation(WebMethod.class);
        if (webMetAnno == null) {
            methodBuilder.name(method.getName());
            return;
        } else if (StringUtils.isNotEmpty(webMetAnno.operationName())) {
            methodBuilder.name(webMetAnno.operationName());
            return;
        }
        methodBuilder.name(method.getName());
    }
}
