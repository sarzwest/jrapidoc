package org.jrapidoc.model.param;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
public class PathParam extends Param  {
    public PathParam(String name, boolean isRequired, String typeref, Type type, String description) {
        super(name, isRequired, typeref, type, description);
    }

    public static class PathParamBuilder extends ParamBuilder{
        @Override
        public PathParamBuilder setName(String name) {
            return (PathParamBuilder)super.setName(name);
        }

        @Override
        public PathParamBuilder setRequired(Boolean isRequired) {
            return (PathParamBuilder)super.setRequired(isRequired);
        }

        @Override
        public PathParamBuilder setDescription(String description) {
            return(PathParamBuilder) super.setDescription(description);
        }

        @Override
        public PathParamBuilder setTyperef(String typeref) {
            return(PathParamBuilder) super.setTyperef(typeref);
        }

        @Override
        public Param build() {
            return new PathParam(name, isRequired, typeref, Type.PATH_PARAM, description);
        }
    }
}
