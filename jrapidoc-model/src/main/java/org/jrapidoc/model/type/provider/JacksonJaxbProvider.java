package org.jrapidoc.model.type.provider;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.jrapidoc.model.object.type.Type;

/**
 * Created by papa on 25.3.15.
 */
public class JacksonJaxbProvider extends JacksonJsonProvider {

    public JacksonJaxbProvider() {
        super();
        objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()));
    }

    @Override
    public Type createType(java.lang.reflect.Type genericType) {
        return super.createType(genericType);
    }
}
