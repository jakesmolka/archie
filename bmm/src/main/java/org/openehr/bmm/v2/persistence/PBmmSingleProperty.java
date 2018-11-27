package org.openehr.bmm.v2.persistence;

public final class PBmmSingleProperty extends PBmmProperty<PBmmSimpleType> {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public PBmmSimpleType getTypeRef() {
        if(getTypeDef() == null) {
            return new PBmmSimpleType(type);
        }
        return getTypeDef();
    }

}
