package org.jrapidoc.model.type.provider;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.jrapidoc.model.object.type.Type;

/**
 * Created by papa on 11.4.15.
 */
public class JacksonJsonJaxbProvider extends JacksonJsonProvider{

    public JacksonJsonJaxbProvider() {
        super();
        objectMapper.setAnnotationIntrospector(AnnotationIntrospector.pair(new JacksonAnnotationIntrospector(), new JaxbAnnotationIntrospector(objectMapper.getTypeFactory())));
    }

    @Override
    public Type createType(java.lang.reflect.Type genericType) {
        return super.createType(genericType);
    }
}
