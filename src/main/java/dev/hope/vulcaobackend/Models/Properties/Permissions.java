package dev.hope.vulcaobackend.Models.Properties;


public enum Permissions {
    ADD_PRODUCT, UPDATE_PRODUCT, DELETE_PRODUCT,
    ADD_STAFF, UPDATE_STAFF, FIRE_STAFF,
    ADD_COMPANY, UPDATE_COMPANY, DELETE_COMPANY,
    ADD_SERVICE, UPDATE_SERVICE, DELETE_SERVICE,
    VIEW_REPORTS, UPDATE_ACCOUNT;

    public String getAuthority(){
        return this.name();
    }
}
