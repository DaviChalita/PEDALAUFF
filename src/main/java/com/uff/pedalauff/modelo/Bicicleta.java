package com.uff.pedalauff.modelo;


import com.uff.pedalauff.enums.EstadoBicicleta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "bicicleta")
public class Bicicleta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBicicleta;
    @Column(nullable = false, unique = true, length = 58)
    private String qrCode;
    @Enumerated
    private EstadoBicicleta estadoAtual;
}