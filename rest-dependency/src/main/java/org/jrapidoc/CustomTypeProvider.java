package org.jrapidoc;

import org.jrapidoc.model.object.type.CustomType;
import org.jrapidoc.model.object.type.Type;
import org.jrapidoc.model.type.TypeProvider;

/**
 * Created by papa on 26.3.15.
 */
public class CustomTypeProvider extends TypeProvider {
    @Override
    public Type createType(java.lang.reflect.Type type) {
        return new CustomType(null, null, null);
    }
}
