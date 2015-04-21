package org.jrapidoc.model.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
@JsonPropertyOrder({"name", "description", "required", "type", "typeref"})
public abstract class Param {

    public static enum Type {
        FORM_PARAM,
        QUERY_PARAM,
        MATRIX_PARAM,
        HEADER_PARAM,
        PATH_PARAM,
        COOKIE_PARAM
    };

    private String name;
    @JsonProperty("required")
    private boolean isRequired;
    @JsonIgnore
    private Type type;
    private String description;
    private String typeref;

    protected Param(String name, boolean isRequired, String typeref, Type type, String description) {
        this.name = name;
        this.isRequired = isRequired;
        this.typeref = typeref;
        this.type = type;
        this.description = description;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public String getTyperef() {
        return typeref;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTyperef(String typeref) {
        this.typeref = typeref;
    }

    public static abstract class ParamBuilder{

        String name;
        /**v nektere metode muze byt required, jinde neni required*/
        boolean isRequired;
        String description;
        String typeref;

        public ParamBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ParamBuilder setRequired(Boolean isRequired) {
            boolean isRequiredValue = true;
            if(isRequired != null){
                isRequiredValue = isRequired;
            }
            this.isRequired = isRequiredValue;
            return this;
        }

        public ParamBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ParamBuilder setTyperef(String typeref) {
            this.typeref = typeref;
            return this;
        }

        public abstract Param build();
    }
}
