package org.jrapidoc.processor;

import org.jboss.resteasy.spi.metadata.*;
import org.jrapidoc.RestUtil;
import org.jrapidoc.model.*;
import org.jrapidoc.model.object.type.Type;
import org.jrapidoc.model.param.*;
import org.jrapidoc.model.type.provider.TypeProvider;

import java.util.*;

/**
 * Created by papa on 26.1.15.
 */
public class ResourceClassProcessor {

    TypeProvider typeProvider;

    public ResourceClassProcessor(TypeProvider typeProvider) {
        this.typeProvider = typeProvider;
    }

    /**
     * Hlavni metoda, z metadat vytvari vysledny resource listing
     *
     * @param resourceClasses
     * @param basePath
     * @return
     */
    public ResourceListing createListing(Set<ResourceClass> resourceClasses, String basePath) {
        List<Resource> resources = new ArrayList<Resource>();
        for (ResourceClass resourceClass : resourceClasses) {
            Resource resource = createResource(resourceClass);
            resources.add(resource);
        }
        return new ResourceListing(basePath, resources);
    }

    void addConsumesParam(Method.MethodBuilder methodBuilder, ResourceClass resourceClass, ResourceMethod resourceMethod) {
        if (resourceMethod.getConsumes() != null) {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(resourceMethod.getConsumes()).setName(HeaderParam.ACCEPT).build();
            methodBuilder.param(param.getType(), param);
        }
        if (resourceClass.getConsumes() != null) {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(resourceClass.getConsumes()).setName(HeaderParam.ACCEPT).build();
            methodBuilder.param(param.getType(), param);
        } else {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(new String[]{"*/*"}).setName(HeaderParam.ACCEPT).build();
            methodBuilder.param(param.getType(), param);
        }
    }

    void addProducesParam(Method.MethodBuilder methodBuilder, ResourceClass resourceClass, ResourceMethod resourceMethod) {
        if (resourceMethod.getProduces() != null) {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(resourceMethod.getProduces()).setName(HeaderParam.CONTENT_TYPE).build();
            methodBuilder.param(param.getType(), param);
        }
        if (resourceClass.getProduces() != null) {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(resourceClass.getProduces()).setName(HeaderParam.CONTENT_TYPE).build();
            methodBuilder.param(param.getType(), param);
        } else {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(new String[]{"*/*"}).setName(HeaderParam.CONTENT_TYPE).build();
            methodBuilder.param(param.getType(), param);
        }
    }

    Resource createResource(ResourceClass resourceClass) {
        Resource.ResourceBuilder resourceBuilder = new Resource.ResourceBuilder();
        resourceBuilder.path(RestUtil.trimSlash(resourceClass.getPath()));
        resourceBuilder.pathExample(resourceClass.getPathExample());
        resourceBuilder.description(resourceClass.getDescription());
        addMethods(resourceClass, resourceBuilder);
        addLocatorMethods(resourceClass, resourceBuilder);
        return resourceBuilder.build();
    }

    void addLocatorMethods(ResourceClass resourceClass, Resource.ResourceBuilder resourceBuilder) {
        for (ResourceLocator resourceLocator : resourceClass.getResourceLocators()) {
            ResourceClass newResourceClass = ResourceBuilder.locatorFromAnnotations(resourceLocator.getReturnType());
            newResourceClass.setPath(resourceLocator.getFullpath());
            newResourceClass.setConstructor(resourceClass.getConstructor());
            ResourceListing locatorListing = createListing(new HashSet<ResourceClass>(Arrays.asList(new ResourceClass[]{newResourceClass})), null);
            for (Method method : locatorListing.getResources().get(0).getMethods()) {
                resourceBuilder.method(method);
            }
        }
    }

    void addMethods(ResourceClass resourceClass, Resource.ResourceBuilder resourceBuilder) {
        for (ResourceMethod resourceMethod : resourceClass.getResourceMethods()) {
            Method method = createMethod(resourceMethod, resourceClass);
            resourceBuilder.method(method);
            for (String httpMethod : resourceMethod.getHttpMethods()) {
                method = method.clone(httpMethod);
                resourceBuilder.method(method);
            }
        }
    }

    Method createMethod(ResourceMethod resourceMethod, ResourceClass resourceClass) {
        resourceMethod.messageBodyCheck();
        Method.MethodBuilder methodBuilder = new Method.MethodBuilder();
        methodBuilder.isAsynchronous(resourceMethod.isAsynchronous());
        methodBuilder.description(resourceMethod.getDescription());
        addClassParams(methodBuilder, resourceClass);
        addConsumesParam(methodBuilder, resourceClass, resourceMethod);
        addProducesParam(methodBuilder, resourceClass, resourceMethod);
        addMethodParams(methodBuilder, resourceMethod);
        addConstructorParams(methodBuilder, resourceClass);
        addPaths(methodBuilder, resourceClass, resourceMethod);
        methodBuilder.parameter(createParameterType(resourceMethod.getParams()));
        removeThisMethodFromResource(resourceMethod, methodBuilder);
        methodBuilder.returnOptions(createReturnOptions(resourceMethod));
        return methodBuilder.build();
    }

    void removeThisMethodFromResource(ResourceMethod resourceMethod, Method.MethodBuilder methodBuilder) {
        for (String httpMethod : resourceMethod.getHttpMethods()) {
            methodBuilder.httpMethodType(httpMethod);
            resourceMethod.getHttpMethods().remove(httpMethod);
            break;
        }
    }

    List<Return> createReturnOptions(ResourceMethod resourceMethod) {
        List<Return> returnObjects = new ArrayList<Return>();
        for (ReturnOption returnOption : resourceMethod.getReturnOptions()) {
            returnObjects.add(createReturnOption(returnOption));
        }
        return returnObjects;
    }

    Return createReturnOption(ReturnOption returnOption) {
        List<TransportType> returnTypes = new ArrayList<TransportType>();
        if (returnOption.getReturnClass() != null) {
            Type returnType = typeProvider.createType(returnOption.getParameterized());
            returnTypes.add(new TransportType.TransportTypeBuilder().type(returnType).description(returnOption.getDescription()).build());
        }
        List<HeaderParam> headerParams = createReturnHeaders(returnOption.getHeaders());
        List<CookieParam> cookieParams = createReturnCookies(returnOption.getCookies());
        return Return.httpStatus(returnOption.getStatus()).headerParams(headerParams).cookieParams(cookieParams).returnTypes(returnTypes).description(returnOption.getDescription()).build();
    }

    List<HeaderParam> createReturnHeaders(List<String> headersString) {
        List<HeaderParam> items = new ArrayList<HeaderParam>();
        for (String header : headersString) {
            items.add(new HeaderParam.HeaderParamBuilder().setName(header).setTyperef(ModelUtil.getSimpleTypeSignature(String.class, null)).build());
        }
        return items;
    }

    List<CookieParam> createReturnCookies(List<String> cookiesString) {
        List<CookieParam> items = new ArrayList<CookieParam>();
        for (String header : cookiesString) {
            items.add(new CookieParam.CookieParamBuilder().setName(header).setTyperef(ModelUtil.getSimpleTypeSignature(String.class, null)).build());
        }
        return items;
    }

    void addPaths(Method.MethodBuilder methodBuilder, ResourceClass resourceClass, ResourceMethod resourceMethod) {
        methodBuilder.path(RestUtil.trimSlash(RestUtil.trimSlash(resourceClass.getPath()) + "/" + RestUtil.trimSlash(resourceMethod.getPath())));
        methodBuilder.pathExample(resourceMethod.getPathExample());
    }

    void addMethodParams(Method.MethodBuilder methodBuilder, ResourceMethod resourceMethod) {
        for (MethodParameter methodParameter : resourceMethod.getParams()) {
            if (RestUtil.isHttpParam(methodParameter)) {
                Param param = createParam(methodParameter);
                methodBuilder.param(param.getType(), param);
            }
        }
    }

    void addClassParams(Method.MethodBuilder methodBuilder, ResourceClass resourceClass) {
        for (FieldParameter fieldParameter : resourceClass.getFields()) {
            if (RestUtil.isHttpParam(fieldParameter)) {
                Param param = createParam(fieldParameter);
                methodBuilder.param(param.getType(), param);
            }
        }
    }

    void addConstructorParams(Method.MethodBuilder methodBuilder, ResourceClass resourceClass) {
        for (ConstructorParameter constructorParameter : resourceClass.getConstructor().getParams()) {
            if (RestUtil.isHttpParam(constructorParameter)) {
                Param param = createParam(constructorParameter);
                methodBuilder.param(param.getType(), param);
            }
        }
    }

    Param createParam(Parameter parameter) {
        Param.ParamBuilder paramBuilder = null;
        if (parameter.getParamType().name().equals(Param.Type.QUERY_PARAM.name())) {
            paramBuilder = new QueryParam.QueryParamBuilder();
        } else if (parameter.getParamType().name().equals(Parameter.ParamType.HEADER_PARAM.name())) {
            paramBuilder = new HeaderParam.HeaderParamBuilder();
        } else if (parameter.getParamType().name().equals(Param.Type.PATH_PARAM.name())) {
            paramBuilder = new PathParam.PathParamBuilder();
        } else if (parameter.getParamType().name().equals(Param.Type.MATRIX_PARAM.name())) {
            paramBuilder = new MatrixParam.MatrixParamBuilder();
        } else if (parameter.getParamType().name().equals(Param.Type.COOKIE_PARAM.name())) {
            paramBuilder = new CookieParam.CookieParamBuilder();
        } else if (parameter.getParamType().name().equals(Param.Type.FORM_PARAM.name())) {
            paramBuilder = new FormParam.FormParamBuilder();
        }
        paramBuilder.setName(parameter.getParamName());
        Type type = createParameterType(parameter);
        paramBuilder.setTyperef(type.getTypeRef());
        paramBuilder.setDescription(parameter.getDescription());
//        paramBuilder.setRequired();
        return paramBuilder.build();
    }

    /**
     * Dosazeni za *Param typy a vytvoreni spravneho typu
     */
    private Type createParameterType(Parameter parameter) {
        if (ModelUtil.isNumericType(parameter.getType())) {
            return typeProvider.createType(Integer.class);
        }
        if (ModelUtil.isBooleanType(parameter.getType())) {
            return typeProvider.createType(Boolean.class);
        }
        return typeProvider.createType(String.class);
//        return typeProvider.createType(parameter.getGenericType());
    }

    private TransportType createParameterType(MethodParameter[] parameters) {
        for (MethodParameter methodParameter : parameters) {
            if (methodParameter.getParamType().equals(MethodParameter.ParamType.MESSAGE_BODY)) {
                return new TransportType.TransportTypeBuilder().type(typeProvider.createType(methodParameter.getGenericType())).description(methodParameter.getDescription()).build();
            }
        }
        //TODO nebere parametr - void
        return null;
    }
}
