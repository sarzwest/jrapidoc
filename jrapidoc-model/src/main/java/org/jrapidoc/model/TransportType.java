package org.jrapidoc.model;

import org.jrapidoc.model.object.type.Type;

/**
 * Created by papa on 10.4.15.
 */
public class TransportType {

    Type type;
    String description;

    private TransportType(Type type, String description) {
        this.type = type;
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class TransportTypeBuilder{
        Type type;
        String description;

        public TransportTypeBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public TransportTypeBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TransportType build(){
            return new TransportType(type, description);
        }

    }
}
