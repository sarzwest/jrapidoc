package org.jrapidoc.model.type;

import org.apache.commons.lang3.StringUtils;
import org.jrapidoc.logger.Logger;

/**
 * Created by papa on 25.3.15.
 */
public class TypeProviderFactory {

    public static TypeProvider createTypeProvider(String clazzToLoad) {
        try {
            if (StringUtils.isNotEmpty(clazzToLoad)) {
                Logger.debug("Trying to load class {0} as TypeProvider instance", clazzToLoad);
                Class<?> providerImpl = Thread.currentThread().getContextClassLoader().loadClass(clazzToLoad);
                return (TypeProvider)providerImpl.newInstance();
            }else{
                Logger.debug("Using default TypeProvider instance");
                return new JacksonTypeProvider();
            }
        } catch (Exception e) {
            Logger.warn(e, "Exception occured during loading creating TypeProvider, using default TypeProvider");
            return new JacksonTypeProvider();
        }
    }
}
