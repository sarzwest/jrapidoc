package org.jrapidoc.model.param;

/**
 * Created by papa on 23.12.14.
 */
public class CookieParam extends Param {
    public CookieParam(String name, boolean isRequired, String typeref, Type type, String description) {
        super(name, isRequired, typeref, type, description);
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
            return new CookieParam(name, isRequired, typeref, Type.COOKIE_PARAM, description);
        }
    }
}
