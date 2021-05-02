package com.uff.pedalauff.modelo;

import com.uff.pedalauff.enums.TipoUsuario;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "usuario")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;
    @Column(nullable = false, unique = true, length = 58)
    private Integer matricula;
    @Column(nullable = false, length = 58)
    private String nome;
    @Column(nullable = false, unique = true, length = 58)
    private String email;
    @Column(nullable = false, length = 58)
    private String senha;
    @Enumerated
    private TipoUsuario tipoUsuario;
}
