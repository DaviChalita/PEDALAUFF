package com.uff.pedalauff.modelo;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "posto")
public class Posto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer idPosto;
    //@Column(nullable = false, length = 58)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "idVaga")
    private Integer idVaga;
    @Column(nullable = false, length = 58)
    private String endereco;
    @Column(nullable = false, length = 58)
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