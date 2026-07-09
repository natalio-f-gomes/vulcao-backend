package dev.hope.vulcaobackend.Properties;


public enum Permissions {
    ADD_PRODUCT, UPDATE_PRODUCT, DELETE_PRODUCT,;

    public String getAuthority(){
        return this.name();
    }
}
