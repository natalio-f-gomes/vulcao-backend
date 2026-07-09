package dev.hope.vulcaobackend.Properties;

import lombok.Getter;

@Getter
public enum ISLAND {
    SANTO_ANTÃO("Santo Antão"),
    SÃO_VICENTE("São Vicente"),
    SÃO_NICOLAU("São Nicolau"),
    SAL("Sal"),
    BOA_VISTA("Boa Vista"),
    MAIO("Maio"),
    SANTIAGO("Santiago"),
    FOGO("Fogo"),
    BRAVA("Brava");

    private final String displayName;

    ISLAND(String displayName) {
        this.displayName = displayName;
    }

}