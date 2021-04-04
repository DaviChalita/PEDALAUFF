package com.uff.pedalauff.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity(name = "vaga")
public class Vaga implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer idVaga;
    @Column(nullable = false, length = 58)
    private String qrCode;
    @Column(nullable = false, length = 58)
    private boolean disponibilidade;
    @ManyToOne
    private Posto posto;

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

    public void alteraDisponibilidadeVaga(Vaga vaga) {
        vaga.setDisponibilidade(!vaga.isDisponibilidade());
    }

}