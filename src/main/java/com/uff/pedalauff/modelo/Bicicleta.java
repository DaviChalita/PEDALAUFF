package com.uff.pedalauff.modelo;


import com.uff.pedalauff.enums.EstadoBicicleta;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "bicicleta")
public class Bicicleta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idBicicleta;
    @Column(nullable = false, length = 58)
    private String qrCode;
    @Enumerated
    private EstadoBicicleta estadoAtual;
    @OneToOne(mappedBy = "bicicletaAlugada")
    private Aluguel aluguel;
    @ManyToMany
    List<Vaga> vagas;

    public Bicicleta() {
    }

    public Bicicleta(Integer idBicicleta, String qrCode, EstadoBicicleta estadoAtual, Aluguel aluguel) {
        this.idBicicleta = idBicicleta;
        this.qrCode = qrCode;
        this.estadoAtual = estadoAtual;
        this.aluguel = aluguel;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdBicicleta() {
        return idBicicleta;
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

    public EstadoBicicleta getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(EstadoBicicleta estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

    public Aluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(Aluguel aluguel) {
        this.aluguel = aluguel;
    }

    public static void alteraEstadoBicicleta(Bicicleta bicicleta, EstadoBicicleta estado) {
        bicicleta.setEstadoAtual(estado);
    }
}