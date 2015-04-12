package org.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.jrapidoc.annotation.Description;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.*;
import org.jrapidoc.model.param.HeaderParam;
import org.jrapidoc.model.type.provider.TypeProvider;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;
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
        resourceBuilder.description(getDescription(seiClass.getDeclaredAnnotations()));
        addMethods(seiClass, resourceBuilder);
        return resourceBuilder.build();
    }

    void addMethods(Class<?> seiClass, Resource.ResourceBuilder resourceBuilder) {
        for (Method method : seiClass.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                WebMethod webMetAnnotation = method.getAnnotation(WebMethod.class);
                if (webMetAnnotation == null) {
                    resourceBuilder.method(createMethod(method, seiClass));
                } else if (!method.getAnnotation(WebMethod.class).exclude()) {
                    resourceBuilder.method(createMethod(method, seiClass));
                }
            }
        }
    }

    String getDescription(Annotation[] annotations) {
        Description description = getAnnotation(annotations, Description.class);
        if (description == null) {
            return null;
        } else {
            return description.value();
        }
    }

    org.jrapidoc.model.Method createMethod(Method method, Class<?> seiClass) {
        org.jrapidoc.model.Method.MethodBuilder methodBuilder = new org.jrapidoc.model.Method.MethodBuilder();
        methodBuilder.description(getDescription(method.getDeclaredAnnotations())).isAsynchronous(true);
        addOperationName(method, methodBuilder);
        addMethodName(method, methodBuilder);
        addInputHeaders(method, methodBuilder);
        addInputParams(method, methodBuilder);
        addReturn(method, methodBuilder);
        addSoapBinding(method, seiClass, methodBuilder);
        return methodBuilder.build();
    }

    void addOperationName(Method method, org.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        WebMethod webMethodAnno = getAnnotation(method.getDeclaredAnnotations(), WebMethod.class);
        String operationName = method.getName();
        if (webMethodAnno != null) {
            if (StringUtils.isNotEmpty(webMethodAnno.operationName())) {
                operationName = webMethodAnno.operationName();
            }
        }
        methodBuilder.name(operationName);
    }

    void addSoapBinding(Method method, Class<?> seiClass, org.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        SOAPBinding soapBindingAnno = getAnnotation(method.getDeclaredAnnotations(), SOAPBinding.class);
        SoapBinding.SoapBindingBuilder soapBindingBuilder = new SoapBinding.SoapBindingBuilder();
        if (soapBindingAnno == null) {
            soapBindingAnno = getAnnotation(seiClass.getDeclaredAnnotations(), SOAPBinding.class);
        }
        if (soapBindingAnno != null) {
            soapBindingBuilder.parameterStyle(soapBindingAnno.parameterStyle().name()).style(soapBindingAnno.style().name()).use(soapBindingAnno.use().name()).build();
        }
        methodBuilder.soapBinding(soapBindingBuilder.build());
    }

    void addReturn(Method method, org.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        if (!isOneWay(method)) {
            Return.ReturnBuilder returnBuilder = new Return.ReturnBuilder();
            addOutputHeaders(method, returnBuilder);
            addOutputParams(method, returnBuilder);
            HeaderParam headerParam = new HeaderParam.HeaderParamBuilder().setName(HeaderParam.CONTENT_TYPE).setOptions(new String[]{"application/xml"}).build();
            returnBuilder.httpStatus(200).headerParams(Arrays.asList(headerParam));
            Return returnType = returnBuilder.build();
            List<Return> returnOptions = new ArrayList<Return>(Arrays.asList(new Return[]{returnType}));
            addExceptionTypes(method, returnOptions);
            methodBuilder.returnOptions(returnOptions);
        }
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
            Annotation[] annotations = method.getParameterAnnotations()[i];
            if (isHeader(annotations)) {
                if (isInputMode(annotations)) {
                    TransportType soapInputHeader = new TransportType.TransportTypeBuilder().type(createType(param)).description(getDescription(annotations)).build();
                    methodBuilder.soapInputHeader(soapInputHeader);
                }
            }
        }
    }

    void addInputParams(Method method, org.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        List<org.jrapidoc.model.object.type.Type> parameterTypes = new ArrayList<org.jrapidoc.model.object.type.Type>();
        for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
            Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
            org.jrapidoc.model.object.type.Type parameterType = createType(param);
            Annotation[] annotations = method.getParameterAnnotations()[i];
            if (isInputMode(annotations)) {
                if (!isHeader(annotations)) {
                    TransportType soapInputParameter = new TransportType.TransportTypeBuilder().type(createType(param)).description(getDescription(annotations)).build();
                    methodBuilder.parameter(soapInputParameter);
                }
            }
        }
    }

    void addOutputHeaders(Method method, Return.ReturnBuilder returnBuilder) {
        if (!isOneWay(method)) {
            for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
                Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
                Annotation[] annotations = method.getParameterAnnotations()[i];
                if (isHeader(annotations)) {
                    if (isOutputMode(annotations)) {
                        TransportType soapOutputHeader = new TransportType.TransportTypeBuilder().type(createType(param)).description(getDescription(annotations)).build();
                        returnBuilder.soapOutputHeader(soapOutputHeader);
                    }
                }
            }
            addHeaderFromReturn(method, returnBuilder);
        }
    }

    //udelat podporu pro soapbinding
    void addOutputParams(Method method, Return.ReturnBuilder returnBuilder) {
        if (!isOneWay(method)) {
            List<TransportType> returnTypes = new ArrayList<TransportType>();
            for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
                Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
                Annotation[] annotations = method.getParameterAnnotations()[i];
                TransportType soapOutputParameter = new TransportType.TransportTypeBuilder().type(createType(param)).description(getDescription(annotations)).build();
                if (isOutputMode(method.getParameterAnnotations()[i])) {
                    if (!isHeader(method.getParameterAnnotations()[i])) {
                        returnTypes.add(soapOutputParameter);
                    }
                }
            }
            addTypeFromReturn(method, returnTypes);
            returnBuilder.returnTypes(returnTypes);
        }
    }

    void addExceptionTypes(Method method, List<Return> returnOptions) {
        for (Type exception : method.getGenericExceptionTypes()) {
            TransportType exceptionTransport = new TransportType.TransportTypeBuilder().type(createType(exception)).description(null).build();
            List<TransportType> transportTypes = new ArrayList<TransportType>(Arrays.asList(new TransportType[]{exceptionTransport}));
            List<HeaderParam> httpHeaders = new ArrayList<HeaderParam>(Arrays.asList(new HeaderParam[]{new HeaderParam.HeaderParamBuilder().setName(HeaderParam.CONTENT_TYPE).setOptions(new String[]{"application/xml"}).build()}));
            Return returnException = new Return.ReturnBuilder().httpStatus(500).description(null).returnTypes(transportTypes).headerParams(httpHeaders).build();
            returnOptions.add(returnException);
        }
    }

    void addTypeFromReturn(Method method, List<TransportType> returnTypes) {
        if (!isHeader(method.getDeclaredAnnotations())) {
            if (!method.getGenericReturnType().equals(Void.TYPE)) {
                org.jrapidoc.annotation.soap.Return returnAnno = getAnnotation(method.getDeclaredAnnotations(), org.jrapidoc.annotation.soap.Return.class);
                String description = (returnAnno == null) ? null : returnAnno.description();
                TransportType soapOutputParameter = new TransportType.TransportTypeBuilder().description(description).type(createType(method.getGenericReturnType())).build();
                returnTypes.add(soapOutputParameter);
            }
        }
    }

    void addHeaderFromReturn(Method method, Return.ReturnBuilder returnBuilder) {
        if (isHeader(method.getDeclaredAnnotations())) {
            if (!method.getGenericReturnType().equals(Void.TYPE)) {
                org.jrapidoc.annotation.soap.Return returnAnno = getAnnotation(method.getDeclaredAnnotations(), org.jrapidoc.annotation.soap.Return.class);
                String description = (returnAnno == null) ? null : returnAnno.description();
                TransportType soapOutputHeader = new TransportType.TransportTypeBuilder().description(description).type(createType(method.getGenericReturnType())).build();
                returnBuilder.soapOutputHeader(soapOutputHeader);
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
