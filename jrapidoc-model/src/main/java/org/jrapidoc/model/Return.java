package org.jrapidoc.model;

import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.object.type.Type;
import org.jrapidoc.model.param.CookieParam;
import org.jrapidoc.model.param.HeaderParam;

import java.util.*;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
public class Return {

    private int httpStatus;
    private Map<String, HeaderParam> headerParams;
    private Map<String, CookieParam> cookieParams;
    private Map<String, TransportType> returnTypes;
    private String description;
    private Map<String, TransportType> soapOutputHeaders;

    private Return(int httpStatus, Map<String, HeaderParam> headerParams, Map<String, CookieParam> cookieParams, Map<String, TransportType> returnTypes, String description, Map<String, TransportType> soapOutputHeaders) {
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

    public Map<String, HeaderParam> getHeaderParams() {
        return headerParams;
    }

    public Map<String, CookieParam> getCookieParams() {
        return cookieParams;
    }

    public Map<String, TransportType> getReturnTypes() {
        return returnTypes;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, TransportType> getSoapOutputHeaders() {
        return soapOutputHeaders;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setHeaderParams(Map<String, HeaderParam> headerParams) {
        this.headerParams = headerParams;
    }

    public void setCookieParams(Map<String, CookieParam> cookieParams) {
        this.cookieParams = cookieParams;
    }

    public void setReturnTypes(Map<String, TransportType> returnTypes) {
        this.returnTypes = returnTypes;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSoapOutputHeaders(Map<String, TransportType> soapOutputHeaders) {
        this.soapOutputHeaders = soapOutputHeaders;
    }

    public static class ReturnBuilder{

        private int httpStatus;
        private Map<String, HeaderParam> headerParams = new LinkedHashMap<String, HeaderParam>();
        private Map<String, CookieParam> cookieParams = new LinkedHashMap<String, CookieParam>();
        private Map<String, TransportType> returnTypes = new LinkedHashMap<String, TransportType>();
        private String description;
        private Map<String, TransportType> soapOutputHeaders = new LinkedHashMap<String, TransportType>();

        public ReturnBuilder httpStatus(int httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ReturnBuilder headerParams(List<HeaderParam> headerParams) {
            for (HeaderParam param:headerParams){
                String key = param.getName();
                if(key == null){
                    Logger.warn("Putting null key into map!!!");
                }
                if(this.headerParams.containsKey(key)){
                    Logger.warn("Header param identifier must be unique, but header param with identifier {0} already exists!!!", key);
                }
                this.headerParams.put(param.getName(), param);
            }
            return this;
        }

        public ReturnBuilder cookieParams(List<CookieParam> cookieParams) {
            for (CookieParam param:cookieParams){
                String key = param.getName();
                if(key == null){
                    Logger.warn("Putting null key into map!!!");
                }
                if(this.cookieParams.containsKey(key)){
                    Logger.warn("Cookie param identifier must be unique, but cookie param with identifier {0} already exists!!!", key);
                }
                this.cookieParams.put(key, param);
            }
            return this;
        }

        public ReturnBuilder returnTypes(List<TransportType> returnTypes) {
            for (TransportType type:returnTypes){
                this.returnTypes.put(type.getType().getTypeRef(), type);
            }//todo neprepisuje se to tady? Podle me muze byt string, string a to nebude fungovat v soap
            return this;
        }

        public ReturnBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ReturnBuilder soapOutputHeader(TransportType soapHeader) {
            this.soapOutputHeaders.put(soapHeader.getType().getTypeRef(), soapHeader);
            //todo stejny jako todo predtim
            return this;
        }

        public Return build(){
            return new Return(httpStatus, headerParams, cookieParams, returnTypes, description, (soapOutputHeaders.isEmpty())?null: soapOutputHeaders);
        }
    }
}
