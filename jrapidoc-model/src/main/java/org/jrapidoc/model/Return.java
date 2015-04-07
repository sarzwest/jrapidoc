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
    private Type returnType;
    private String description;

    private Return(int httpStatus, List<HeaderParam> headerParams, List<CookieParam> cookieParams, Type returnType, String description) {
        this.httpStatus = httpStatus;
        this.headerParams = headerParams;
        this.cookieParams = cookieParams;
        this.returnType = returnType;
        this.description = description;
    }

    public static ReturnBuilder  httpStatus(int httpStatus) {
        return new ReturnBuilder().httpStatus(httpStatus);
    }

    public static ReturnBuilder headerParams(List<HeaderParam> headerParams) {
        return new ReturnBuilder().headerParams(headerParams);
    }

    public static ReturnBuilder cookieParams(List<CookieParam> cookieParams) {
        return new ReturnBuilder().cookieParams(cookieParams);
    }

    public static ReturnBuilder  returnType(Type returnType) {
        return new ReturnBuilder().returnType(returnType);
    }

    public static class ReturnBuilder{

        private int httpStatus;
        private List<HeaderParam> headerParams = new ArrayList<HeaderParam>();
        private List<CookieParam> cookieParams = new ArrayList<CookieParam>();
        private Type returnType;
        private String description;

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

        public ReturnBuilder returnType(Type returnType) {
            this.returnType = returnType;
            return this;
        }

        public ReturnBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Return build(){
            return new Return(httpStatus, headerParams, cookieParams, returnType, description);
        }
    }
}
