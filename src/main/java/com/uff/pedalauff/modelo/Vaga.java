package com.uff.pedalauff.modelo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "vaga")
public class Vaga implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVaga;
    @Column(nullable = false, unique = true, length = 58)
    private String qrCode;
    @Column(nullable = false, length = 58)
    private boolean disponibilidade;
    @OneToOne
    private Posto posto;
    @OneToOne
    private Bicicleta bicicleta;

    public void alteraDisponibilidadeVaga(@org.jetbrains.annotations.NotNull Vaga vaga) {
        vaga.setDisponibilidade(!vaga.isDisponibilidade());
    }

}