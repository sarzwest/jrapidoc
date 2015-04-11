package org.jrapidoc.model.param;

/**
 * Created by papa on 23.12.14.
 */
public class QueryParam extends Param  {
    public QueryParam(String name, boolean isRequired, String typeref, Type type, String description) {
        super(name, isRequired, typeref, type, description);
    }

    @Override
    public QueryParamBuilder setDescription(String description) {
        return new QueryParamBuilder().setDescription(description);
    }

    @Override
    protected QueryParamBuilder setRequired(boolean isRequired) {
        return new QueryParamBuilder().setRequired(isRequired);
    }

    @Override
    protected QueryParamBuilder setName(String name) {
        return (QueryParamBuilder) new QueryParamBuilder().setName(name);
    }

    public static class QueryParamBuilder extends ParamBuilder{
        @Override
        public QueryParamBuilder setName(String name) {
            return (QueryParamBuilder)super.setName(name);
        }

        @Override
        public QueryParamBuilder setRequired(boolean isRequired) {
            return (QueryParamBuilder)super.setRequired(isRequired);
        }

        @Override
        public QueryParamBuilder setDescription(String description) {
            return(QueryParamBuilder) super.setDescription(description);
        }

        @Override
        public QueryParamBuilder setTyperef(String typeref) {
            return(QueryParamBuilder) super.setTyperef(typeref);
        }

        @Override
        public Param build() {
            return new QueryParam(name, isRequired, typeref, Type.QUERY_PARAM, description);
        }
    }
}
