package dev.hope.vulcaobackend.Models.Properties;


public enum Permissions {
    ADD_PRODUCT, UPDATE_PRODUCT, DELETE_PRODUCT,
    ASSIGN_TASKS, CREATE_TASK, DELETE_TASK,
    VIEW_REPORTS,;

    public String getAuthority(){
        return this.name();
    }
}
