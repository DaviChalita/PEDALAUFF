package com.uff.pedalauff.modelo;

public class Vaga {
    private Integer idVaga;
    private String qrCode;
    private boolean disponibilidade;

    public Vaga() {
    }

    public Vaga(Integer idVaga, String qrCode, boolean disponibilidade) {
        this.idVaga = idVaga;
        this.qrCode = qrCode;
        this.disponibilidade = disponibilidade;
    }

    public Integer getIdVaga() {
        return idVaga;
    }

    public void setIdVaga(Integer idVaga) {
        this.idVaga = idVaga;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public static void alteraDisponibilidadeVaga(Vaga vaga) {
        vaga.setDisponibilidade(!vaga.isDisponibilidade());
    }
}