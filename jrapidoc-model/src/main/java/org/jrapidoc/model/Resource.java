package org.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jrapidoc.model.param.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by papa on 23.12.14.
 */
@JsonPropertyOrder({"description", "path", "pathExample", "methods"})
public class Resource {

    private String path;
    private String pathExample;
    @JsonIgnore
    private List<HeaderParam> headerParams = new ArrayList<HeaderParam>();
    @JsonIgnore
    private List<CookieParam> cookieParams = new ArrayList<CookieParam>();
    @JsonIgnore
    private List<FormParam> formParams = new ArrayList<FormParam>();
    @JsonIgnore
    private List<MatrixParam> matrixParams = new ArrayList<MatrixParam>();
    @JsonIgnore
    private List<PathParam> pathParams = new ArrayList<PathParam>();
    @JsonIgnore
    private List<QueryParam> queryParams = new ArrayList<QueryParam>();
    private List<Method> methods = new ArrayList<Method>();
    private String description;
    private String name;

    private Resource(String path, String pathExample, List<HeaderParam> headerParams, List<CookieParam> cookieParams, List<FormParam> formParams, List<MatrixParam> matrixParams, List<PathParam> pathParams, List<QueryParam> queryParams, List<Method> methods, String description, String name) {
        this.path = path;
        this.pathExample = pathExample;
        this.headerParams = headerParams;
        this.cookieParams = cookieParams;
        this.formParams = formParams;
        this.matrixParams = matrixParams;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
        this.methods = methods;
        this.description = description;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public String getDescription() {
        return description;
    }

    public String getPathExample() {
        return pathExample;
    }

    public List<HeaderParam> getHeaderParams() {
        return headerParams;
    }

    public List<CookieParam> getCookieParams() {
        return cookieParams;
    }

    public List<FormParam> getFormParams() {
        return formParams;
    }

    public List<MatrixParam> getMatrixParams() {
        return matrixParams;
    }

    public List<PathParam> getPathParams() {
        return pathParams;
    }

    public List<QueryParam> getQueryParams() {
        return queryParams;
    }


    private <T extends Param> T getParam(List<T> params, String paramName){
        for (T p:params){
            if(p.getName().equals(paramName)){
                return p;
            }
        }
        return null;
    }

    public Param getParam(String paramType, String paramName){
        if(paramType.equals("COOKIE_PARAM")){
            return getParam(cookieParams, paramName);
        }else if(paramType.equals("FORM_PARAM")){
            return getParam(formParams, paramName);
        }else if(paramType.equals("HEADER_PARAM")){
            return getParam(headerParams, paramName);
        }else if(paramType.equals("MATRIX_PARAM")){
            return getParam(matrixParams, paramName);
        }else if(paramType.equals("PATH_PARAM")){
            return getParam(pathParams, paramName);
        }else if(paramType.equals("QUERY_PARAM")){
            return getParam(queryParams, paramName);
        }
        return null;
    }

    public static class ResourceBuilder{

        private String path;
        private String pathExample;
        private List<HeaderParam> headerParams = new ArrayList<HeaderParam>();
        private List<CookieParam> cookieParams = new ArrayList<CookieParam>();
        private List<FormParam> formParams = new ArrayList<FormParam>();
        private List<MatrixParam> matrixParams = new ArrayList<MatrixParam>();
        private List<PathParam> pathParams = new ArrayList<PathParam>();
        private List<QueryParam> queryParams = new ArrayList<QueryParam>();
        private List<Method> methods = new ArrayList<Method>();
        private String description;
        private String name;

        public ResourceBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ResourceBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ResourceBuilder pathExample(String pathExample) {
            this.pathExample = pathExample;
            return this;
        }

        public ResourceBuilder method(Method method) {
            this.methods.add(method);
            return this;
        }

        public ResourceBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ResourceBuilder param(Param.Type paramType, Param param) {
            if (paramType.equals(Param.Type.COOKIE_PARAM)) {
                addCookieParam((CookieParam)param);
            } else if (paramType.equals(Param.Type.FORM_PARAM)) {
                addFormParam((FormParam)param);
            } else if (paramType.equals(Param.Type.HEADER_PARAM)) {
                addHeaderParam((HeaderParam)param);
            } else if (paramType.equals(Param.Type.MATRIX_PARAM)) {
                addMatrixParam((MatrixParam)param);
            } else if (paramType.equals(Param.Type.PATH_PARAM)) {
                addPathParam((PathParam)param);
            } else if (paramType.equals(Param.Type.QUERY_PARAM)) {
                addQueryParam((QueryParam)param);
            }
            return this;
        }

        /**First try to add more specific value and then try to add less specific value.  */
        protected void addHeaderParam(HeaderParam headerParam) {
            for (HeaderParam param:headerParams){
                if(param.getName().equals(headerParam.getName())){
                    return;
                }
            }
            this.headerParams.add(headerParam);
        }

        protected void addCookieParam(CookieParam cookieParam) {
            this.cookieParams.add(cookieParam);
        }

        protected void addFormParam(FormParam formParam) {
            this.formParams.add(formParam);
        }

        protected void addMatrixParam(MatrixParam matrixParam) {
            this.matrixParams.add(matrixParam);
        }

        protected void addPathParam(PathParam pathParam) {
            this.pathParams.add(pathParam);
        }

        protected void addQueryParam(QueryParam queryParam) {
            this.queryParams.add(queryParam);
        }

        public Resource build(){
//            for(Method method:methods){
//
//            }
            return new Resource(path, pathExample, headerParams, cookieParams, formParams, matrixParams, pathParams, queryParams, methods, description, name);
        }
    }
}
