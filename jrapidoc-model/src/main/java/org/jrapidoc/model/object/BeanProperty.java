package org.jrapidoc.model.object;

/**
 * Created by papa on 13.1.15.
 */
public class BeanProperty {

    public static String ROOT_VARIABLE = "root";
    String name;
    Class<?> type;
    String typeRef;

    public BeanProperty(String name, String typeRef, Class<?> type) {
        this.name = name;
        this.type = type;
        this.typeRef = typeRef;
    }

    public void addType(Class<?> type){
        this.type = type;
    }

    @Override
    public String toString() {
        return "BeanProperty{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", typeRef='" + typeRef + '\'' +
                '}';
    }
}
