package com.uff.pedalauff.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}