package org.jrapidoc.model.object.type;

import org.jrapidoc.model.object.BeanProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by papa on 18.1.15.
 */
public class CustomType extends org.jrapidoc.model.object.type.Type {

    private List<BeanProperty> attributes = new ArrayList<BeanProperty>();
    private Class<?> typeClass;
    private List<String> enumerations = new ArrayList<String>();

    public CustomType(String typeName, String jacksonToString, List<BeanProperty> attributes, Class<?> typeClass) {
        super(typeName, jacksonToString);
        this.attributes = attributes;
        this.typeClass = typeClass;
    }

    public CustomType(String typeName, String jacksonToString, Class<?> typeClass){
        super(typeName, jacksonToString);
        this.typeClass = typeClass;
    }

    public void addBeanProperty(BeanProperty variable) {
        attributes.add(variable);
    }

    public void addEnumeration(String enumeration) {
        enumerations.add(enumeration);
    }

    @Override
    public String toString() {
        return "CustomType{" +
                "attributes=" + attributes +
                ", typeClass=" + typeClass +
                ", enumerations=" + enumerations +
                "} " + super.toString();
    }
}
