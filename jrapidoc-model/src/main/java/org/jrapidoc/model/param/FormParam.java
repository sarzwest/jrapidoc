package org.jrapidoc.model.param;

/**
 * Created by papa on 23.12.14.
 */
public class FormParam extends Param {
    public FormParam(String name, boolean isRequired, String typeref, Type type, String description) {
        super(name, isRequired, typeref, type, description);
    }

    @Override
    public ParamBuilder setDescription(String description) {
        return null;
    }

    @Override
    protected ParamBuilder setRequired(boolean isRequired) {
        return null;
    }

    @Override
    protected ParamBuilder setName(String name) {
        return null;
    }

    public static class FormParamBuilder extends ParamBuilder{

        @Override
        public FormParamBuilder setName(String name) {
            return (FormParamBuilder)super.setName(name);
        }

        @Override
        public FormParamBuilder setRequired(boolean isRequired) {
            return (FormParamBuilder)super.setRequired(isRequired);
        }

        @Override
        public FormParamBuilder setDescription(String description) {
            return(FormParamBuilder) super.setDescription(description);
        }

        @Override
        public FormParamBuilder setTyperef(String typeref) {
            return(FormParamBuilder) super.setTyperef(typeref);
        }

        @Override
        public FormParam build() {
            return new FormParam(name, isRequired, typeref, Type.FORM_PARAM, description);
        }
    }
}
