package org.jrapidoc.model.param;

/**
 * Created by papa on 23.12.14.
 */
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
    /**v nektere metode muze byt required, jinde neni required*/
    private boolean isRequired;
    private Type type;
    private String description;
    private String typeref;

    protected Param(String name, boolean isRequired, String typeref, Type type) {
        this.name = name;
        this.isRequired = isRequired;
        this.typeref = typeref;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public abstract ParamBuilder setDescription(String description) ;

    public String getName() {
        return name;
    }

    protected boolean isRequired() {
        return isRequired;
    }

    protected abstract  ParamBuilder setRequired(boolean isRequired) ;

    public Type getType() {
        return type;
    }

//    protected abstract  ParamBuilder setType(Type type);

    protected abstract  ParamBuilder setName(String name);

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

        public ParamBuilder setRequired(boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

//        public ParamBuilder setType(Type type) {
//            this.type = type;
//            return this;
//        }

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
