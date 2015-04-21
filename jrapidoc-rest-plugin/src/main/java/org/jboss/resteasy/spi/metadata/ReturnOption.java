package org.jboss.resteasy.spi.metadata;

import org.jrapidoc.annotation.rest.Return;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas "sarzwest" Jiricek on 10.1.15.
 */
public class ReturnOption {

    int status;
    List<String> cookies = new ArrayList<String>();
    List<String> headers = new ArrayList<String>();
    Type parameterized;
    Class<?> returnClass;
    Return.Structure structure;
    String description;

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public Type getParameterized() {
        return parameterized;
    }

    public void parameterized(Type parameterized) {
        this.parameterized = parameterized;
    }

    public Return.Structure getStructure() {
        return structure;
    }

    public void setStructure(Return.Structure structure) {
        this.structure = structure;
    }

    public void setReturnClass(Class<?> returnClass) {
        this.returnClass = returnClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public int getStatus() {
        return status;
    }

    public List<String> getCookies() {
        return cookies;
    }

//    public List<ReturnType> getReturnTypes() {
//        return returnTypes;
//    }

//    public class ReturnType{
//
//        Class<?> returnClass;
//
//        ReturnType(Class<?> returnClass) {
//            this.returnClass = returnClass;
//        }
//
//        public Class<?> getReturnClass() {
//            return returnClass;
//        }
//
//    }
}
