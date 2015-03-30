package org.jrapidoc.model.object.type;


/**
 * Created by papa on 19.1.15.
 */
public class MapTypeJrapidoc extends org.jrapidoc.model.object.type.Type {

    private String keyType;
    private String keyTypeJacksonToString;
    private String valueType;
    private String valueTypeJacksonToString;

    public MapTypeJrapidoc(String typeName, String jacksonToString, String keyType, String keyTypeJacksonToString, String valueType, String valueTypeJacksonToString) {
        super(typeName, jacksonToString);
        this.keyType = keyType;
        this.keyTypeJacksonToString = keyTypeJacksonToString;
        this.valueType = valueType;
        this.valueTypeJacksonToString = valueTypeJacksonToString;
    }
}
