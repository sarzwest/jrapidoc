package org.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jrapidoc.model.object.type.Type;
import org.jrapidoc.model.param.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by papa on 23.12.14.
 */
@JsonPropertyOrder({"path", "httpMethodType", "pathExample", "headerParams",
        "pathParams", "queryParams", "matrixParams", "cookieParams",
        "formParams", "isAsynchronous", "parameters", "returnOptions"})
public class Method {

    private boolean isAsynchronous;
    private List<HeaderParam> headerParams = new ArrayList<HeaderParam>();
    private List<CookieParam> cookieParams = new ArrayList<CookieParam>();
    private List<FormParam> formParams = new ArrayList<FormParam>();
    private List<MatrixParam> matrixParams = new ArrayList<MatrixParam>();
    private List<PathParam> pathParams = new ArrayList<PathParam>();
    private List<QueryParam> queryParams = new ArrayList<QueryParam>();
    private String path;
    private String pathExample;
    private List<Return> returnOptions;
    private List<TransportType> parameters;
    private String httpMethodType;
    private String description;
    private String name;
    private List<TransportType> soapInputHeaders;
    private SoapBinding soapBinding;

    private Method(boolean isAsynchronous, List<HeaderParam> headerParams, List<CookieParam> cookieParams, List<FormParam> formParams, List<MatrixParam> matrixParams, List<PathParam> pathParams, List<QueryParam> queryParams, String path, String pathExample, List<Return> returnOptions, List<TransportType> parameters, String httpMethodType, String description, String name, List<TransportType> soapInputHeaders, SoapBinding soapBinding) {
        this.isAsynchronous = isAsynchronous;
        this.headerParams = headerParams;
        this.cookieParams = cookieParams;
        this.formParams = formParams;
        this.matrixParams = matrixParams;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
        this.path = path;
        this.pathExample = pathExample;
        this.returnOptions = returnOptions;
        this.parameters = parameters;
        this.httpMethodType = httpMethodType;
        this.description = description;
        this.name = name;
        this.soapInputHeaders = soapInputHeaders;
        this.soapBinding = soapBinding;
    }

    public MethodBuilder returnOption(List<Return> returnOptions) {
        return new MethodBuilder().returnOptions(returnOptions);
    }

    public String getName() {
        return name;
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
                this.pathParams, this.queryParams, this.path, this.pathExample, this.returnOptions,
                this.parameters, httpMethod, this.description, this.name, this.soapInputHeaders, this.soapBinding);
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
        private List<Return> returnOptions = new ArrayList<Return>();
        private List<TransportType> parameters = new ArrayList<TransportType>();
        private String httpMethodType;
        private String description;
        private String name;
        private List<TransportType> soapInputHeaders = new ArrayList<TransportType>();
        private SoapBinding soapBinding;

        public boolean isAsynchronous() {
            return isAsynchronous;
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

        public List<String> getConsumes() {
            return consumes;
        }

        public List<String> getProduces() {
            return produces;
        }

        public String getPath() {
            return path;
        }

        public String getPathExample() {
            return pathExample;
        }

        public List<Return> getReturnOptions() {
            return returnOptions;
        }

        public List<TransportType> getParameters() {
            return parameters;
        }

        public String getHttpMethodType() {
            return httpMethodType;
        }

        public MethodBuilder isAsynchronous(boolean isAsynchronous) {
            this.isAsynchronous = isAsynchronous;
            return this;
        }

        public MethodBuilder consumes(String consumes) {
            this.consumes.add(consumes);
            return this;
        }

        public MethodBuilder name(String name) {
            this.name = name;
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

        public MethodBuilder returnOptions(List<Return> returnOptions) {
            this.returnOptions.addAll(returnOptions);
            return this;
        }

        public MethodBuilder parameter(TransportType parameter) {
            this.parameters.add(parameter);
            return this;
        }

        public MethodBuilder httpMethodType(String httpMethodType) {
            this.httpMethodType = httpMethodType;
            return this;
        }

        public MethodBuilder description(String description) {
            this.description = description;
            return this;
        }

        public MethodBuilder soapBinding(SoapBinding soapBinding) {
            this.soapBinding = soapBinding;
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

        public void soapInputHeader(TransportType soapHeader) {
            this.soapInputHeaders.add(soapHeader);
        }

        public Method build(){
            return new Method(isAsynchronous, headerParams, cookieParams, formParams, matrixParams, pathParams, queryParams, path, pathExample, returnOptions, parameters, httpMethodType, description, name, (soapInputHeaders.isEmpty())?null: soapInputHeaders, soapBinding);
        }
    }
}
