package com.uff.pedalauff.modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "vaga")
public class Vaga implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idVaga;
    @Column(nullable = false, length = 58)
    private String qrCode;
    @Column(nullable = false, length = 58)
    private boolean disponibilidade;
    @ManyToOne
    private Posto posto;
    @ManyToMany
    List<Bicicleta> bicicletas;

    public Vaga() {
    }

    public Vaga(Integer idVaga, String qrCode, boolean disponibilidade, Posto posto, List<Bicicleta> bicicletas) {
        this.idVaga = idVaga;
        this.qrCode = qrCode;
        this.disponibilidade = disponibilidade;
        this.posto = posto;
        this.bicicletas = bicicletas;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Posto getPosto() {
        return posto;
    }

    public void setPosto(Posto posto) {
        this.posto = posto;
    }

    public List<Bicicleta> getBicicletas() {
        return bicicletas;
    }

    public void setBicicletas(List<Bicicleta> bicicletas) {
        this.bicicletas = bicicletas;
    }

    public void alteraDisponibilidadeVaga(Vaga vaga) {
        vaga.setDisponibilidade(!vaga.isDisponibilidade());
    }

}