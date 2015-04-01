package org.jrapidoc.model.param;

/**
 * Created by papa on 23.12.14.
 */
public class CookieParam extends Param {
    public CookieParam(String name, boolean isRequired, String typeref, Type type) {
        super(name, isRequired, typeref, type);
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

    public static class CookieParamBuilder extends ParamBuilder{

        @Override
        public CookieParamBuilder setName(String name) {
            return (CookieParamBuilder)super.setName(name);
        }

        @Override
        public CookieParamBuilder setRequired(boolean isRequired) {
            return (CookieParamBuilder)super.setRequired(isRequired);
        }

        @Override
        public CookieParamBuilder setDescription(String description) {
            return(CookieParamBuilder) super.setDescription(description);
        }

        @Override
        public CookieParamBuilder setTyperef(String typeref) {
            return(CookieParamBuilder) super.setTyperef(typeref);
        }

        @Override
        public CookieParam build() {
            return new CookieParam(name, isRequired, typeref, Type.COOKIE_PARAM);
        }
    }
}
