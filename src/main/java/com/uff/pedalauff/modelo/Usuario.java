package com.uff.pedalauff.modelo;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "usuario")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer idUsuario;
    @Column(nullable = false, length = 58)
    private Integer matricula;
    @Column(nullable = false, length = 58)
    private String nome;
    @Column(nullable = false, length = 58)
    private String email;
    @Column(nullable = false, length = 10)
    private String senha;

    public Usuario() {
    }

    public Usuario(Integer idUsuario, Integer matricula, String nome, String email, String senha) {
        this.idUsuario = idUsuario;
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
