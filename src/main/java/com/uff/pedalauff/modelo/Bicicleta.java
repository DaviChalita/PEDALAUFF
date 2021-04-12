package com.uff.pedalauff.modelo;


import com.uff.pedalauff.enums.EstadoBicicleta;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
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

    public static void alteraEstadoBicicleta(Bicicleta bicicleta, EstadoBicicleta estado) {
        bicicleta.setEstadoAtual(estado);
    }
}