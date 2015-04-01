package org.jrapidoc.model.object.type;


/**
 * Created by papa on 19.1.15.
 */
public class MapTypeJrapidoc extends org.jrapidoc.model.object.type.Type {

    private String keyType;
    private String keyTypeRef;
    private String valueType;
    private String valueTypeRef;

    public MapTypeJrapidoc(String typeName, String typeRef, String keyType, String keyTypeRef, String valueType, String valueTypeRef) {
        super(typeName, typeRef);
        this.keyType = keyType;
        this.keyTypeRef = keyTypeRef;
        this.valueType = valueType;
        this.valueTypeRef = valueTypeRef;
    }
}
