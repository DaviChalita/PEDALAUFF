package com.uff.pedalauff.modelo;


import com.uff.pedalauff.enums.EstadoBicicleta;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "bicicleta")
public class Bicicleta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer idBicicleta;
    @Column(nullable = false, length = 58)
    private String qrCode;
    @Column
    private boolean cestinho;
    @Enumerated
    private EstadoBicicleta estadoAtual;
    @OneToMany(mappedBy = "bicicletaAlugada")
    private List<Aluguel> alugueis;

    public Bicicleta() {
    }

    public Bicicleta(Integer idBicicleta, String qrCode, boolean cestinho, EstadoBicicleta estadoAtual, List<Aluguel> alugueis) {
        this.idBicicleta = idBicicleta;
        this.qrCode = qrCode;
        this.cestinho = cestinho;
        this.estadoAtual = estadoAtual;
        this.alugueis = alugueis;
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

    public boolean isCestinho() {
        return cestinho;
    }

    public void setCestinho(boolean cestinho) {
        this.cestinho = cestinho;
    }

    public EstadoBicicleta getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(EstadoBicicleta estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

    public List<Aluguel> getAlugueis() {
        return alugueis;
    }

    public void setAlugueis(List<Aluguel> alugueis) {
        this.alugueis = alugueis;
    }

    public static void alteraEstadoBicicleta(Bicicleta bicicleta, EstadoBicicleta estado) {
        bicicleta.setEstadoAtual(estado);
    }

}