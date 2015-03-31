package org.jboss.resteasy.spi.metadata;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by papa on 10.1.15.
 */
public class ResponseObject {

    int status;
    Set<String> cookies = new HashSet<String>();
    List<ReturnType> returnTypes = new ArrayList<ReturnType>();

    public void addReturnType(Class<?> clazz, boolean includeSubtypes){
        returnTypes.add(new ReturnType(clazz, includeSubtypes));
    }

    public int getStatus() {
        return status;
    }

    public Set<String> getCookies() {
        return cookies;
    }

    public List<ReturnType> getReturnTypes() {
        return returnTypes;
    }

    public class ReturnType{

        Class<?> returnClass;
        boolean includeSubtypes;

        ReturnType(Class<?> returnClass, boolean includeSubtypes) {
            this.returnClass = returnClass;
            this.includeSubtypes = includeSubtypes;
        }

        public Class<?> getReturnClass() {
            return returnClass;
        }

        public boolean isIncludeSubtypes() {
            return includeSubtypes;
        }
    }
}
