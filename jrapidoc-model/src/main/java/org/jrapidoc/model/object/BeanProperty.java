package org.jrapidoc.model.object;

/**
 * Created by papa on 13.1.15.
 */
public class BeanProperty {

    public static String ROOT_VARIABLE = "root";
    String name;
    Class<?> type;
    String typeRef;
    String description;
    boolean isRequired;

    public BeanProperty(String name, String typeRef, Class<?> type, String description, boolean isRequired) {
        this.name = name;
        this.type = type;
        this.typeRef = typeRef;
        this.description = description;
        this.isRequired = isRequired;
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
                ", description='" + description + '\'' +
                ", isRequired=" + isRequired +
                '}';
    }
}
