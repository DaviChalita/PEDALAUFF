package com.uff.pedalauff.modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "posto")
public class Posto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idPosto;
    @Column(nullable = false, length = 58)
    private String endereco;
    @Column(nullable = false, length = 58)
    private String campus;
    @Column(nullable = false)
    private int qtdVagasDisp;
    @OneToMany
    private List<Vaga> vagas;

    public Posto() {
    }

    public Posto(Integer idPosto) {
        this.idPosto = idPosto;
    }

    public Posto(Integer idPosto, String endereco, String campus, int qtdVagasDisp, List<Vaga> vagas) {
        this.idPosto = idPosto;
        this.endereco = endereco;
        this.campus = campus;
        this.qtdVagasDisp = qtdVagasDisp;
        this.vagas = vagas;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getQtdVagasDisp() {
        return qtdVagasDisp;
    }

    public void setQtdVagasDisp(int qtdVagasDips) {
        this.qtdVagasDisp = qtdVagasDips;
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

    public boolean temVagasDisp(int qtdVagasDisp) {
            return qtdVagasDisp > 0;
    }
}