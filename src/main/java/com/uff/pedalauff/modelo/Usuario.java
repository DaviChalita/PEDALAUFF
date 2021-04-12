package com.uff.pedalauff.modelo;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "usuario")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idUsuario;
    @Column(nullable = false, length = 58)
    private Integer matricula;
    @Column(nullable = false, length = 58)
    private String nome;
    @Column(nullable = false, length = 58)
    private String email;
    @Column(nullable = false, length = 10)
    private String senha;
}
