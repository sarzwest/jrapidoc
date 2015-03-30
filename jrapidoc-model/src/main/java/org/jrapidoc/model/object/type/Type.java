package org.jrapidoc.model.object.type;

import org.jrapidoc.model.object.BeanProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by papa on 23.12.14.
 */
public class Type {

    /**pouzije se v modelu a bude to hodnota ulozena v property typeref, znazornuje zobrazeni typu*/
    private String typeRef;
    private String typeName;
//    private List<String> validations;

    public Type(String typeName, String typeRef) {
//        this.variableName = variableName;
        this.typeName = typeName;
        this.typeRef = typeRef;
//        this.enumList = enumList;
//        this.attributes = attributes;
//        this.validations = validations;
    }

    public String getTypeRef() {
        return typeRef;
    }
    //    public static TypeBuilder variableName(String variableName) {
//        return new TypeBuilder().variableName(variableName);
//    }

    public static TypeBuilder typeName(String typeName) {
        return new TypeBuilder().typeName(typeName);
    }

    public String getTypeName() {
        return typeName;
    }

//    public static TypeBuilder enumm(String enumm) {
//        return new TypeBuilder().enumm(enumm);
//    }
//
//    public static TypeBuilder attributes(Type attribute) {
//        return new TypeBuilder().attributes(attribute);
//    }
//
//    public static TypeBuilder validation(String validation) {
//        return new TypeBuilder().validation(validation);
//    }


    @Override
    public String toString() {
        return "Type{" +
                "typeRef='" + typeRef + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }

    public static class TypeBuilder{

//        private String variableName;
        private String typeName;
//        private List<String> enumList = new ArrayList<String>();
        private List<BeanProperty> attributes = new ArrayList<BeanProperty>();
//        private List<String> validations = new ArrayList<String>();

        public TypeBuilder(){
        }

//        public TypeBuilder variableName(String variableName) {
//            this.variableName = variableName;
//            return this;
//        }

        public TypeBuilder typeName(String typeName) {
            this.typeName = typeName;
            return this;
        }

//        public TypeBuilder enumm(String enumm) {
//            this.enumList.add(enumm);
//            return this;
//        }
//
        public TypeBuilder attributes(BeanProperty attribute) {
            this.attributes.add(attribute);
            return this;
        }
//
//        public TypeBuilder validation(String validation) {
//            this.validations.add(validation);
//            return this;
//        }

        public Type build() {
            if(!attributes.isEmpty()){
//                return new CustomType(typeName, attributes);
            }
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type type = (Type) o;

        if (!typeName.equals(type.typeName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return typeName.hashCode();
    }
}
