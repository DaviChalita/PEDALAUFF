package com.uff.pedalauff.modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "aluguel")
public class Aluguel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idAluguel;
    @Column(length = 58)
    private Date dthrAluguel;
    @Column(length = 58)
    private Date dthrDevolucao;
    @OneToOne
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

}
