package org.jrapidoc.model;

import org.jrapidoc.model.object.type.Type;
import org.jrapidoc.model.param.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by papa on 23.12.14.
 */

public class Method {

    private boolean isAsynchronous;
    private List<HeaderParam> headerParams = new ArrayList<HeaderParam>();
    private List<CookieParam> cookieParams = new ArrayList<CookieParam>();
    private List<FormParam> formParams = new ArrayList<FormParam>();
    private List<MatrixParam> matrixParams = new ArrayList<MatrixParam>();
    private List<PathParam> pathParams = new ArrayList<PathParam>();
    private List<QueryParam> queryParams = new ArrayList<QueryParam>();
//    private List<String> consumes = new ArrayList<String>();
//    private List<String> produces = new ArrayList<String>();
    private String path;
    private String pathExample;
    private List<Return> returnOption;
    private Type parameter;
    private String httpMethodType;

    private Method(boolean isAsynchronous, List<HeaderParam> headerParams, List<CookieParam> cookieParams, List<FormParam> formParams, List<MatrixParam> matrixParams, List<PathParam> pathParams, List<QueryParam> queryParams, /*List<String> consumes, List<String> produces,*/ String path, String pathExample, List<Return> returnOption, Type parameter, String httpMethodType) {
        this.isAsynchronous = isAsynchronous;
        this.headerParams = headerParams;
        this.cookieParams = cookieParams;
        this.formParams = formParams;
        this.matrixParams = matrixParams;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
//        this.consumes = consumes;
//        this.produces = produces;
        this.path = path;
        this.pathExample = pathExample;
        this.returnOption = returnOption;
        this.parameter = parameter;
        this.httpMethodType = httpMethodType;
    }

    public MethodBuilder returnOption(Return returnOption) {
        return new MethodBuilder().returnOption(returnOption);
    }

    public MethodBuilder path(String path) {
        return new MethodBuilder().path(path);
    }

    public MethodBuilder pathExample(String pathExample) {
        return new MethodBuilder().pathExample(pathExample);
    }

    public MethodBuilder httpMethodType(String httpMethodType) {
        return new MethodBuilder().httpMethodType(httpMethodType);
    }

    public MethodBuilder consumes(String mediatype) {
        return new MethodBuilder().consumes(mediatype);
    }

    public MethodBuilder produces(String mediatype) {
        return new MethodBuilder().produces(mediatype);
    }

    public MethodBuilder param(Param.Type paramType, Param paramName) {
        return new MethodBuilder().param(paramType, paramName);
    }

    public MethodBuilder isAsynchronous(boolean asynchronous) {
        return new MethodBuilder().isAsynchronous(isAsynchronous);
    }

    @Override
    public String toString() {
        return "Method{" +
                "path='" + path + '\'' +
                '}';
    }
    
    public Method clone(String httpMethod){
        return new Method(this.isAsynchronous, this.headerParams, this.cookieParams, this.formParams, this.matrixParams,
                this.pathParams, this.queryParams, this.path, this.pathExample, this.returnOption,
                this.parameter, httpMethod);
    }

    public static class MethodBuilder {

        private boolean isAsynchronous;
        private List<HeaderParam> headerParams = new ArrayList<HeaderParam>();
        private List<CookieParam> cookieParams = new ArrayList<CookieParam>();
        private List<FormParam> formParams = new ArrayList<FormParam>();
        private List<MatrixParam> matrixParams = new ArrayList<MatrixParam>();
        private List<PathParam> pathParams = new ArrayList<PathParam>();
        private List<QueryParam> queryParams = new ArrayList<QueryParam>();
        private List<String> consumes = new ArrayList<String>();
        private List<String> produces = new ArrayList<String>();
        private String path;
        private String pathExample;
        private List<Return> returnOption;
        private Type parameter;
        private String httpMethodType;

        public MethodBuilder isAsynchronous(boolean isAsynchronous) {
            this.isAsynchronous = isAsynchronous;
            return this;
        }

        public MethodBuilder consumes(String consumes) {
            this.consumes.add(consumes);
            return this;
        }

        public MethodBuilder produces(String produces) {
            this.produces.add(produces);
            return this;
        }

        public MethodBuilder path(String path) {
            this.path = path;
            return this;
        }

        public MethodBuilder pathExample(String pathExample) {
            this.pathExample = pathExample;
            return this;
        }

        public MethodBuilder returnOption(Return returnOption) {
            this.returnOption.add(returnOption);
            return this;
        }

        public MethodBuilder parameter(Type parameter) {
            this.parameter = parameter;
            return this;
        }

        public MethodBuilder httpMethodType(String httpMethodType) {
            this.httpMethodType = httpMethodType;
            return this;
        }

        public MethodBuilder param(Param.Type paramType, Param param) {
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

        public Method build(){
            return new Method(isAsynchronous, headerParams, cookieParams, formParams, matrixParams, pathParams, queryParams,/* consumes, produces,*/ path, pathExample, returnOption, parameter, httpMethodType);
        }
    }
}
