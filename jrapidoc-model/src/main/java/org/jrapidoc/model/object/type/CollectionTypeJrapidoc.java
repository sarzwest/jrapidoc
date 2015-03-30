package org.jrapidoc.model.object.type;

/**
 * Created by papa on 18.1.15.
 */
public class CollectionTypeJrapidoc extends org.jrapidoc.model.object.type.Type {

    private String includeType;
    private String includeTypeRef;

    public CollectionTypeJrapidoc(String typeName, String typeRef, String includeType, String includeTypeRef) {
        super(typeName, typeRef);
        this.includeType = includeType;
        this.includeTypeRef = includeTypeRef;
    }

    public void setIncludeTypeRef(String includeTypeRef) {
        this.includeTypeRef = includeTypeRef;
    }

    public String getIncludeType() {
        return includeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CollectionTypeJrapidoc that = (CollectionTypeJrapidoc) o;

        if (!includeType.equals(that.includeType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + includeType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CollectionTypeApition{" +
                "includeType='" + includeType + '\'' +
                ", includeTypeRef='" + includeTypeRef + '\'' +
                "} " + super.toString();
    }
}
