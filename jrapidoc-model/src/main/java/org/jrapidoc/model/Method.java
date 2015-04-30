package org.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.param.*;

import java.util.*;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
@JsonPropertyOrder({"name", "soapBinding", "path", "httpMethodType", "pathExample", "headerParams",
        "pathParams", "queryParams", "matrixParams", "cookieParams",
        "formParams", "asynchronous", "parameters", "returnOptions"})
public class Method {

    @JsonProperty("asynchronous")
    private boolean isAsynchronous;
    private Map<String, HeaderParam> headerParams = new LinkedHashMap<String, HeaderParam>();
    private Map<String, CookieParam> cookieParams = new LinkedHashMap<String, CookieParam>();
    private Map<String, FormParam> formParams = new LinkedHashMap<String, FormParam>();
    private Map<String, MatrixParam> matrixParams = new LinkedHashMap<String, MatrixParam>();
    private Map<String, PathParam> pathParams = new LinkedHashMap<String, PathParam>();
    private Map<String, QueryParam> queryParams = new LinkedHashMap<String, QueryParam>();
    private String path;
    private String pathExample;
    private List<Return> returnOptions;
    private List<TransportType> parameters;
    private String httpMethodType;
    private String description;
    private String name;
    private List<TransportType> soapInputHeaders;
    private SoapBinding soapBinding;

    private Method(boolean isAsynchronous, Map<String, HeaderParam> headerParams, Map<String, CookieParam> cookieParams, Map<String, FormParam> formParams, Map<String, MatrixParam> matrixParams, Map<String, PathParam> pathParams, Map<String, QueryParam> queryParams, String path, String pathExample, List<Return> returnOptions, List<TransportType> parameters, String httpMethodType, String description, String name, List<TransportType> soapInputHeaders, SoapBinding soapBinding) {
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

    public String getName() {
        return name;
    }

    public boolean isAsynchronous() {
        return isAsynchronous;
    }

    public Map<String, HeaderParam> getHeaderParams() {
        return headerParams;
    }

    public Map<String, CookieParam> getCookieParams() {
        return cookieParams;
    }

    public Map<String, FormParam> getFormParams() {
        return formParams;
    }

    public Map<String, MatrixParam> getMatrixParams() {
        return matrixParams;
    }

    public Map<String, PathParam> getPathParams() {
        return pathParams;
    }

    public Map<String, QueryParam> getQueryParams() {
        return queryParams;
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

    public String getDescription() {
        return description;
    }

    public List<TransportType> getSoapInputHeaders() {
        return soapInputHeaders;
    }

    public SoapBinding getSoapBinding() {
        return soapBinding;
    }

    public void setAsynchronous(boolean isAsynchronous) {
        this.isAsynchronous = isAsynchronous;
    }

    public void setHeaderParams(Map<String, HeaderParam> headerParams) {
        this.headerParams = headerParams;
    }

    public void setCookieParams(Map<String, CookieParam> cookieParams) {
        this.cookieParams = cookieParams;
    }

    public void setFormParams(Map<String, FormParam> formParams) {
        this.formParams = formParams;
    }

    public void setMatrixParams(Map<String, MatrixParam> matrixParams) {
        this.matrixParams = matrixParams;
    }

    public void setPathParams(Map<String, PathParam> pathParams) {
        this.pathParams = pathParams;
    }

    public void setQueryParams(Map<String, QueryParam> queryParams) {
        this.queryParams = queryParams;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPathExample(String pathExample) {
        this.pathExample = pathExample;
    }

    public void setReturnOptions(List<Return> returnOptions) {
        this.returnOptions = returnOptions;
    }

    public void setParameters(List<TransportType> parameters) {
        this.parameters = parameters;
    }

    public void setHttpMethodType(String httpMethodType) {
        this.httpMethodType = httpMethodType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSoapInputHeaders(List<TransportType> soapInputHeaders) {
        this.soapInputHeaders = soapInputHeaders;
    }

    public void setSoapBinding(SoapBinding soapBinding) {
        this.soapBinding = soapBinding;
    }

    @Override
    public String toString() {
        return "Method{" +
                "path='" + path + '\'' +
                '}';
    }

    public Method clone(String httpMethod) {
        return new Method(this.isAsynchronous, this.headerParams, this.cookieParams, this.formParams, this.matrixParams,
                this.pathParams, this.queryParams, this.path, this.pathExample, this.returnOptions,
                this.parameters, httpMethod, this.description, this.name, this.soapInputHeaders, this.soapBinding);
    }

    public static class MethodBuilder {

        private boolean isAsynchronous;
        private Map<String, HeaderParam> headerParams = new LinkedHashMap<String, HeaderParam>();
        private Map<String, CookieParam> cookieParams = new LinkedHashMap<String, CookieParam>();
        private Map<String, FormParam> formParams = new LinkedHashMap<String, FormParam>();
        private Map<String, MatrixParam> matrixParams = new LinkedHashMap<String, MatrixParam>();
        private Map<String, PathParam> pathParams = new LinkedHashMap<String, PathParam>();
        private Map<String, QueryParam> queryParams = new LinkedHashMap<String, QueryParam>();
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
            if (parameter != null) {
                this.parameters.add(parameter);
            }
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
                addCookieParam((CookieParam) param);
            } else if (paramType.equals(Param.Type.FORM_PARAM)) {
                addFormParam((FormParam) param);
            } else if (paramType.equals(Param.Type.HEADER_PARAM)) {
                addHeaderParam((HeaderParam) param);
            } else if (paramType.equals(Param.Type.MATRIX_PARAM)) {
                addMatrixParam((MatrixParam) param);
            } else if (paramType.equals(Param.Type.PATH_PARAM)) {
                addPathParam((PathParam) param);
            } else if (paramType.equals(Param.Type.QUERY_PARAM)) {
                addQueryParam((QueryParam) param);
            }
            return this;
        }

        /**
         * First try to add more specific value and then try to add less specific value.
         */
        protected void addHeaderParam(HeaderParam headerParam) {
            for (HeaderParam param : headerParams.values()) {
                if (param.getName().equals(headerParam.getName())) {
                    return;
                }
            }
            Logger.debug(this.getClass().getSimpleName() + " " + headerParam.getName());
            this.headerParams.put(headerParam.getName(), headerParam);
        }

        protected void addCookieParam(CookieParam cookieParam) {
            Logger.debug(this.getClass().getSimpleName() + " " + cookieParam.getName());
            this.cookieParams.put(cookieParam.getName(), cookieParam);
        }

        protected void addFormParam(FormParam formParam) {
            Logger.debug(this.getClass().getSimpleName() + " " + formParam.getName());
            this.formParams.put(formParam.getName(), formParam);
        }

        protected void addMatrixParam(MatrixParam matrixParam) {
            Logger.debug(this.getClass().getSimpleName() + " " + matrixParam.getName());
            this.matrixParams.put(matrixParam.getName(), matrixParam);
        }

        protected void addPathParam(PathParam pathParam) {
            Logger.debug(this.getClass().getSimpleName() + " " + pathParam.getName());
            this.pathParams.put(pathParam.getName(), pathParam);
        }

        protected void addQueryParam(QueryParam queryParam) {
            Logger.debug(this.getClass().getSimpleName() + " " + queryParam.getName());
            this.queryParams.put(queryParam.getName(), queryParam);
        }

        public void soapInputHeader(TransportType soapHeader) {
            this.soapInputHeaders.add(soapHeader);
        }

        public Method build() {
            return new Method(isAsynchronous, headerParams, cookieParams, formParams, matrixParams, pathParams, queryParams, path, pathExample, returnOptions, parameters, httpMethodType, description, name, (soapInputHeaders.isEmpty()) ? null : soapInputHeaders, soapBinding);
        }
    }
}
