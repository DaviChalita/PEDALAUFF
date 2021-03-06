package com.uff.pedalauff.enums;


public enum EstadoBicicleta {
    NA_VAGA(0),
    EM_USO(1),
    EM_MANUTENCAO(2),
    CRIADA(3);

    private final int valor;

    EstadoBicicleta(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}