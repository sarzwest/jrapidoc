package org.jrapidoc.model.param;

/**
 * Created by papa on 23.12.14.
 */
public class HeaderParam extends Param  {

    String[] options;
    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE = "Content-Type";

//    private HeaderParam(String name, boolean isRequired, String typeref, Type type) {
//        super(name, isRequired, typeref, type);
//    }

    private HeaderParam(String name, boolean isRequired, String typeref, String[] options, Type type) {
        super(name, isRequired, typeref, type);
        this.options = options;
    }

    @Override
    public HeaderParamBuilder setDescription(String description) {
        return (HeaderParamBuilder) new HeaderParamBuilder().setDescription(description);
    }

    public String[] getOptions() {
        return options;
    }

    public HeaderParamBuilder setOptions(String[] options) {
        return (HeaderParamBuilder) new HeaderParamBuilder().setOptions(options);
    }

    @Override
    protected HeaderParamBuilder setRequired(boolean isRequired) {
        return (HeaderParamBuilder) new HeaderParamBuilder().setRequired(isRequired);
    }

    @Override
    protected HeaderParamBuilder setName(String name) {
        return (HeaderParamBuilder) new HeaderParamBuilder().setName(name);
    }

    public static class HeaderParamBuilder extends ParamBuilder{

        String[] options;

        public HeaderParamBuilder setOptions(String[] options) {
            this.options = options;
            return this;
        }

        @Override
        public HeaderParamBuilder setName(String name) {
            return (HeaderParamBuilder)super.setName(name);
        }

        @Override
        public HeaderParamBuilder setRequired(boolean isRequired) {
            return (HeaderParamBuilder)super.setRequired(isRequired);
        }

        @Override
        public HeaderParamBuilder setDescription(String description) {
            return(HeaderParamBuilder) super.setDescription(description);
        }

        @Override
        public HeaderParamBuilder setTyperef(String typeref) {
            return(HeaderParamBuilder) super.setTyperef(typeref);
        }

        @Override
        public HeaderParam build() {
            return new HeaderParam(name, isRequired, typeref, options, Type.HEADER_PARAM);
        }
    }
}
