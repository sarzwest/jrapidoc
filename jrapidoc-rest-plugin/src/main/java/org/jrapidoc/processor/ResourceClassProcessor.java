package org.jrapidoc.processor;

import org.jboss.resteasy.spi.metadata.*;
import org.jrapidoc.RestUtil;
import org.jrapidoc.model.Method;
import org.jrapidoc.model.ModelUtil;
import org.jrapidoc.model.Resource;
import org.jrapidoc.model.ResourceListing;
import org.jrapidoc.model.object.type.Type;
import org.jrapidoc.model.param.*;
import org.jrapidoc.model.type.TypeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        for (ResourceMethod resourceMethod : resourceClass.getResourceMethods()) {
            Method method = createMethod(resourceMethod, resourceClass);
            resourceBuilder.method(method);
            for (String httpMethod : resourceMethod.getHttpMethods()) {
                method = method.clone(httpMethod);
                resourceBuilder.method(method);
            }
        }
        return resourceBuilder.build();
    }

    Method createMethod(ResourceMethod resourceMethod, ResourceClass resourceClass) {
        resourceMethod.messageBodyCheck();
        Method.MethodBuilder methodBuilder = new Method.MethodBuilder();
        methodBuilder.isAsynchronous(resourceMethod.isAsynchronous());
        addClassParams(methodBuilder, resourceClass);
        addConsumesParam(methodBuilder, resourceClass, resourceMethod);
        addProducesParam(methodBuilder, resourceClass, resourceMethod);
        addMethodParams(methodBuilder, resourceMethod);
        addPaths(methodBuilder, resourceClass, resourceMethod);
        methodBuilder.parameter(createParameterType(resourceMethod.getParams()));
        for (String httpMethod : resourceMethod.getHttpMethods()) {
            methodBuilder.httpMethodType(httpMethod);
            resourceMethod.getHttpMethods().remove(httpMethod);
            break;
        }
        //TODO methodBuilder.returnOption();
        return methodBuilder.build();
    }

    void addPaths(Method.MethodBuilder methodBuilder, ResourceClass resourceClass, ResourceMethod resourceMethod) {
        methodBuilder.path(RestUtil.trimSlash(resourceClass.getPath()) + "/" + RestUtil.trimSlash(resourceMethod.getPath()));
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
            Param param = createParam(fieldParameter);
            methodBuilder.param(param.getType(), param);
        }
    }

    Param createParam(Parameter parameter) {
        Param.ParamBuilder paramBuilder = null;
        if (parameter.getParamType().name().equals(Param.Type.QUERY_PARAM.name())) {
            paramBuilder = new QueryParam.QueryParamBuilder();
        } else if (parameter.getParamType().name().equals(Parameter.ParamType.HEADER_PARAM.name())) {
            paramBuilder = new HeaderParam.HeaderParamBuilder();
        }else if(parameter.getParamType().name().equals(Param.Type.PATH_PARAM.name())){
            paramBuilder = new PathParam.PathParamBuilder();
        }else if(parameter.getParamType().name().equals(Param.Type.MATRIX_PARAM.name())){
            paramBuilder = new MatrixParam.MatrixParamBuilder();
        }
        paramBuilder.setName(parameter.getParamName());
        Type type = createParameterType(parameter);
        paramBuilder.setTyperef(type.getTypeRef());
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

    private Type createParameterType(MethodParameter[] parameters) {
        for (MethodParameter methodParameter : parameters) {
            if (methodParameter.getParamType().equals(MethodParameter.ParamType.MESSAGE_BODY)) {
                return typeProvider.createType(methodParameter.getGenericType());
            }
        }
        //TODO nebere parametr - void
        return null;
    }
}
