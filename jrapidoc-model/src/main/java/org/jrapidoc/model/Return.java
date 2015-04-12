package org.jrapidoc.model;

import org.jrapidoc.model.object.type.Type;
import org.jrapidoc.model.param.CookieParam;
import org.jrapidoc.model.param.HeaderParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by papa on 23.12.14.
 */
public class Return {

    private int httpStatus;
    private List<HeaderParam> headerParams;
    private List<CookieParam> cookieParams;
    private List<TransportType> returnTypes;
    private String description;
    private List<TransportType> soapOutputHeaders;

    private Return(int httpStatus, List<HeaderParam> headerParams, List<CookieParam> cookieParams, List<TransportType> returnTypes, String description, List<TransportType> soapOutputHeaders) {
        this.httpStatus = httpStatus;
        this.headerParams = headerParams;
        this.cookieParams = cookieParams;
        this.returnTypes = returnTypes;
        this.description = description;
        this.soapOutputHeaders = soapOutputHeaders;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public List<HeaderParam> getHeaderParams() {
        return headerParams;
    }

    public List<CookieParam> getCookieParams() {
        return cookieParams;
    }

    public List<TransportType> getReturnTypes() {
        return returnTypes;
    }

    public String getDescription() {
        return description;
    }

    public List<TransportType> getSoapOutputHeaders() {
        return soapOutputHeaders;
    }

    public static class ReturnBuilder{

        private int httpStatus;
        private List<HeaderParam> headerParams = new ArrayList<HeaderParam>();
        private List<CookieParam> cookieParams = new ArrayList<CookieParam>();
        private List<TransportType> returnTypes;
        private String description;
        private List<TransportType> soapOutputHeaders = new ArrayList<TransportType>();

        public ReturnBuilder httpStatus(int httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ReturnBuilder headerParams(List<HeaderParam> headerParams) {
            this.headerParams.addAll(headerParams);
            return this;
        }

        public ReturnBuilder cookieParams(List<CookieParam> cookieParams) {
            this.cookieParams.addAll(cookieParams);
            return this;
        }

        public ReturnBuilder returnTypes(List<TransportType> returnTypes) {
            this.returnTypes = returnTypes;
            return this;
        }

        public ReturnBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ReturnBuilder soapOutputHeader(TransportType soapHeader) {
            this.soapOutputHeaders.add(soapHeader);
            return this;
        }

        public Return build(){
            return new Return(httpStatus, headerParams, cookieParams, returnTypes, description, (soapOutputHeaders.isEmpty())?null: soapOutputHeaders);
        }
    }
}
