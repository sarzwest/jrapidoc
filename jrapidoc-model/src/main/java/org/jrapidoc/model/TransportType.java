package org.jrapidoc.model;

import org.jrapidoc.model.object.type.Type;

/**
 * Created by Tomas "sarzwest" Jiricek on 10.4.15.
 */
public class TransportType {

    Type type;
    String description;
    Boolean isRequired;

    private TransportType(Type type, String description, Boolean isRequired) {
        this.type = type;
        this.description = description;
        this.isRequired = isRequired;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public static class TransportTypeBuilder{
        Type type;
        String description;
        Boolean isRequired;

        public TransportTypeBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public TransportTypeBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TransportTypeBuilder isRequired(Boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        public TransportType build(){
            return new TransportType(type, description, isRequired);
        }

    }
}
