package com.uff.pedalauff.modelo;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
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
    @JoinColumn(nullable = false)
    private Bicicleta bicicletaAlugada;
    @OneToOne
    @JoinColumn(nullable = false)
    private Usuario usuarioAlugado;

}
