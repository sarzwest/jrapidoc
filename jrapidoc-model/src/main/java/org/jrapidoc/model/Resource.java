package org.jrapidoc.model;

import org.jrapidoc.model.param.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by papa on 23.12.14.
 */
public class Resource {

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

    private Resource(String path, String pathExample, List<HeaderParam> headerParams, List<CookieParam> cookieParams, List<FormParam> formParams, List<MatrixParam> matrixParams, List<PathParam> pathParams, List<QueryParam> queryParams, List<Method> methods, String description) {
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
    }

    public ResourceBuilder method(Method method) {
        return new ResourceBuilder().method(method);
    }

    public ResourceBuilder path(String path) {
        return new ResourceBuilder().path(path);
    }

    public ResourceBuilder pathExample(String pathExample) {
        return new ResourceBuilder().pathExample(pathExample);
    }

    public ResourceBuilder param(Param.Type paramType, Param param) {
        return new ResourceBuilder().param(paramType, param);
    }

    public ResourceBuilder description(String description) {
        return new ResourceBuilder().description(description);
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

    public void addMethod(Method method) {
        methods.add(method);
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
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

        public ResourceBuilder path(String path) {
            this.path = path;
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
            return new Resource(path, pathExample, headerParams, cookieParams, formParams, matrixParams, pathParams, queryParams, methods, description);
        }
    }
}
