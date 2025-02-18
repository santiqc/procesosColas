package com.procesos.colas.domain.alfesco;

public enum NodeType {
    DEPENDENCIA ("ltksg:dependencia"),
    SERIE ("ltksg:serie"),
    SUBSERIE("ltksg:subserie");
    private final String value;

    NodeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
