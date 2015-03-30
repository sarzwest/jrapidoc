package org.jrapidoc.model.type;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by papa on 25.3.15.
 */
public class TypeProviderFactory {

    public static TypeProvider createTypeProvider(String clazzToLoad) {
        try {
            if (StringUtils.isNotEmpty(clazzToLoad)) {
                Class<?> providerImpl = Thread.currentThread().getContextClassLoader().loadClass(clazzToLoad);
                return (TypeProvider)providerImpl.newInstance();
            }else{
                return new JacksonTypeProvider();
            }
        } catch (Exception e) {
            e.printStackTrace();//TODO dodelat logovani
            return new JacksonTypeProvider();
        }
    }
}
