package com.uff.pedalauff.modelo;

import com.uff.pedalauff.enums.EstadoBicicleta;

public class Bicicleta {
    private Integer idBicicleta;
    private String qrCode;
    private boolean cestinho;
    private EstadoBicicleta estadoAtual;
    private Posto posto;
    private Vaga vaga;

    public Bicicleta() {
    }

    public Bicicleta(Integer idBicicleta, String qrCode, boolean cestinho, EstadoBicicleta estadoAtual, Posto posto, Vaga vaga) {
        this.idBicicleta = idBicicleta;
        this.qrCode = qrCode;
        this.cestinho = cestinho;
        this.estadoAtual = estadoAtual;
        this.posto = posto;
        this.vaga = vaga;
    }

    public Integer getIdBicicleta() {
        return idBicicleta;
    }

    public EstadoBicicleta getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(EstadoBicicleta estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

    public void setIdBicicleta(Integer idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public boolean isCestinho() {
        return cestinho;
    }

    public void setCestinho(boolean cestinho) {
        this.cestinho = cestinho;
    }

    public Posto getPosto() {
        return posto;
    }

    public void setPosto(Posto posto) {
        this.posto = posto;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    public static void alteraEstadoBicicleta(Bicicleta bicicleta, EstadoBicicleta estado) {
        bicicleta.setEstadoAtual(estado);
    }

}