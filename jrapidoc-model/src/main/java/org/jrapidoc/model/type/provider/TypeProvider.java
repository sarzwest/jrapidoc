package org.jrapidoc.model.type.provider;

import org.jrapidoc.model.object.type.Type;

import java.util.Map;

/**
 * Created by papa on 25.3.15.
 */
public abstract class TypeProvider {

    public abstract Type createType(java.lang.reflect.Type genericType);

    public abstract Map<String, Type> getUsedTypes();
}
