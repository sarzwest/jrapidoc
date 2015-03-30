package org.jrapidoc;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.spi.metadata.MethodParameter;
import org.jrapidoc.model.param.Param;

import java.util.Set;

/**
 * Created by papa on 29.3.15.
 */
public class RestUtil {

    public static String trimSlash(String urlPath){
        if(StringUtils.isEmpty(urlPath)){
            return urlPath;
        }
        if(urlPath.charAt(0) == '/'){
            urlPath = urlPath.substring(1);
        }
        if(urlPath.charAt(urlPath.length() - 1) == '/'){
            urlPath = urlPath.substring(0, urlPath.length() - 1);
        }
        return urlPath;
    }

    public static boolean isHttpParam(MethodParameter methodParameter){
        try{
            Param.Type.valueOf(methodParameter.getParamType().name());
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
