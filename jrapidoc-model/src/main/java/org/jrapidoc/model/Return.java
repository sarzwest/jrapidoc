package org.jrapidoc.model;

import org.jrapidoc.model.object.type.Type;
import org.jrapidoc.model.param.CookieParam;
import org.jrapidoc.model.param.HeaderParam;

import java.util.List;

/**
 * Created by papa on 23.12.14.
 */
public class Return {

    private int httpStatus;
    private List<HeaderParam> headerParams;
    private List<CookieParam> cookieParams;
    private Type returnType;

    private Return(int httpStatus, List<HeaderParam> headerParams, List<CookieParam> cookieParams, Type returnType) {
        this.httpStatus = httpStatus;
        this.headerParams = headerParams;
        this.cookieParams = cookieParams;
        this.returnType = returnType;
    }

    public static ReturnBuilder  httpStatus(int httpStatus) {
        return new ReturnBuilder().httpStatus(httpStatus);
    }

    public static ReturnBuilder  headerParam(HeaderParam headerParam) {
        return new ReturnBuilder().headerParam(headerParam);
    }

    public static ReturnBuilder  cookieParam(CookieParam cookieParam) {
        return new ReturnBuilder().cookieParam(cookieParam);
    }

    public static ReturnBuilder  returnType(Type returnType) {
        return new ReturnBuilder().returnType(returnType);
    }

    public static class ReturnBuilder{

        private int httpStatus;
        private List<HeaderParam> headerParams;
        private List<CookieParam> cookieParams;
        private Type returnType;

        public ReturnBuilder httpStatus(int httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ReturnBuilder headerParam(HeaderParam headerParam) {
            this.headerParams.add(headerParam);
            return this;
        }

        public ReturnBuilder cookieParam(CookieParam cookieParam) {
            this.cookieParams.add(cookieParam);
            return this;
        }

        public ReturnBuilder returnType(Type returnType) {
            this.returnType = returnType;
            return this;
        }

        public Return build(){
            return new Return(httpStatus, headerParams, cookieParams, returnType);
        }
    }
}
