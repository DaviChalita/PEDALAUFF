package com.uff.pedalauff.enums;

public enum EstadoBicicleta {
    NA_VAGA(1),
    EM_USO(2);

    private final int valor;

    EstadoBicicleta(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}