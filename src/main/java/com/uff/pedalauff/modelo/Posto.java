package com.uff.pedalauff.modelo;

public class Posto {

    private Integer idPosto;
    private Integer idVaga;
    private String endereco;
    private String campus;

    public Posto(Integer idPosto, Integer idVaga, String endereco, String campus) {
        this.idPosto = idPosto;
        this.idVaga = idVaga;
        this.endereco = endereco;
        this.campus = campus;
    }

    public Posto() {
    }

    public Integer getIdPosto() {
        return idPosto;
    }

    public void setIdPosto(Integer idPosto) {
        this.idPosto = idPosto;
    }

    public Integer getIdVaga() {
        return idVaga;
    }

    public void setIdVaga(Integer idVaga) {
        this.idVaga = idVaga;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }
}