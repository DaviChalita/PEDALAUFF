package com.uff.pedalauff.modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "posto")
public class Posto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer idPosto;
    @Column(nullable = false, length = 58)
    private String endereco;
    @Column(nullable = false, length = 58)
    private String campus;
    @OneToMany
    private List<Vaga> vagas;

    public Posto() {

    }

    public Posto(Integer idPosto) {
        this.idPosto = idPosto;
    }

    public Posto(Integer idPosto, String endereco, String campus, List<Vaga> vagas) {
        this.idPosto = idPosto;
        this.endereco = endereco;
        this.campus = campus;
        this.vagas = vagas;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdPosto() {
        return idPosto;
    }

    public void setIdPosto(Integer idPosto) {
        this.idPosto = idPosto;
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

    public List<Vaga> getVagas() {
        return vagas;
    }

    public void setVagas(List<Vaga> vagas) {
        this.vagas = vagas;
    }
}