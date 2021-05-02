package com.uff.pedalauff.enums;

public enum TipoUsuario {
    ADMIN(0),
    NORMAL(1);

    private final int valor;

    TipoUsuario(int valor){
        this.valor = valor;
    }
    public int getValor() {
        return valor;
    }


}
