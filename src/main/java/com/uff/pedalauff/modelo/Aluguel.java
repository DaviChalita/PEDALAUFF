package com.uff.pedalauff.modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "aluguel")
public class Aluguel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer idAluguel;
    @Column(nullable = false, length = 58)
    private Date dthrAluguel;
    @Column(nullable = false, length = 58)
    private Date dthrDevolucao;
    @ManyToOne
    private Bicicleta bicicletaAlugada;
    @ManyToOne
    private Usuario usuarioAlugado;

    public Aluguel() {
    }

    public Aluguel(Integer idAluguel, Date dthrAluguel, Date dthrDevolucao, Bicicleta bicicletaAlugada, Usuario usuarioAlugado) {
        this.idAluguel = idAluguel;
        this.dthrAluguel = dthrAluguel;
        this.dthrDevolucao = dthrDevolucao;
        this.bicicletaAlugada = bicicletaAlugada;
        this.usuarioAlugado = usuarioAlugado;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Bicicleta getBicicletaAlugada() {
        return bicicletaAlugada;
    }

    public void setBicicletaAlugada(Bicicleta bicicletaAlugada) {
        this.bicicletaAlugada = bicicletaAlugada;
    }

    public Usuario getUsuarioAlugado() {
        return usuarioAlugado;
    }

    public void setUsuarioAlugado(Usuario usuarioAlugado) {
        this.usuarioAlugado = usuarioAlugado;
    }

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
