package org.jrapidoc.model.param;

/**
 * Created by papa on 23.12.14.
 */
public class FormParam extends Param {
    public FormParam(String name, boolean isRequired, String typeref, Type type) {
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
}
