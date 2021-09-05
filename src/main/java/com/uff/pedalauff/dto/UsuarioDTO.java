package com.uff.pedalauff.dto;

import com.uff.pedalauff.enums.TipoUsuario;
import com.uff.pedalauff.modelo.Usuario;
import lombok.Getter;

@Getter
public class UsuarioDTO {
    private Integer idUsuario;
    private Integer matricula;
    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipoUsuario;

    public Usuario transformaParaObjeto() {
        return new Usuario(idUsuario, matricula, nome, email, senha, tipoUsuario);
    }
}
