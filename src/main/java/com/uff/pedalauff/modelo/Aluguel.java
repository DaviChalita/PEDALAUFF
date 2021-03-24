package com.uff.pedalauff.modelo;

import java.util.Date;
import java.util.List;

public class Aluguel {
    public Aluguel() {
    }

    public Aluguel(Integer idAluguel, Date dthrAluguel, Date dthrDevolucao, Bicicleta bicicleta, Usuario usuario) {
        this.idAluguel = idAluguel;
        this.dthrAluguel = dthrAluguel;
        this.dthrDevolucao = dthrDevolucao;
        this.bicicleta = bicicleta;
        this.usuario = usuario;
    }

    public Integer getIdAluguel() {
        return idAluguel;
    }

    public void setIdAluguel(Integer idAluguel) {
        this.idAluguel = idAluguel;
    }

    public Date getDthrAluguel() {
        return dthrAluguel;
    }

    public void setDthrAluguel(Date dthrAluguel) {
        this.dthrAluguel = dthrAluguel;
    }

    public Date getDthrDevolucao() {
        return dthrDevolucao;
    }

    public void setDthrDevolucao(Date dthrDevolucao) {
        this.dthrDevolucao = dthrDevolucao;
    }

    public Bicicleta getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(Bicicleta bicicleta) {
        this.bicicleta = bicicleta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private Integer idAluguel;
    private Date dthrAluguel;
    private Date dthrDevolucao;
    private Bicicleta bicicleta;
    private Usuario usuario;

    //parâmetro listaBicicleta temporário até desenvolvermos a conexão com o BD
    public static boolean scanQrCodeBike(String qrCode, List<Bicicleta> listaBicicleta) {
        for (Bicicleta bicicleta : listaBicicleta) {
            if (bicicleta.getIdBicicleta().equals(qrCode)) {
                return true;
            }
        }
        //busca instancia no banco aonde tem id_bicicleta = qrCode
        //retorna falso se nao encontrar
        return false;
    }

    //parâmetro listaVaga temporário até desenvolvermos a conexão com o BD
    public static boolean scanQrCodeVaga(String qrCode, List<Vaga> listaVaga) {
        for (Vaga vaga : listaVaga) {
            if (vaga.getIdVaga().equals(qrCode)) {
                return true;
            }
        }
        //busca instancia no banco aonde tem id_vaga = qrCode
        //retorna falso se nao encontrar
        return false;
    }

}