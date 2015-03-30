package org.jrapidoc.model.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.jrapidoc.model.object.type.Type;

/**
 * Created by papa on 25.3.15.
 */
public class JaxbTypeProvider extends JacksonTypeProvider {

    public JaxbTypeProvider() {
        super();
        objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()));
    }

    @Override
    public Type createType(java.lang.reflect.Type genericType) {
        return super.createType(genericType);
    }
}
