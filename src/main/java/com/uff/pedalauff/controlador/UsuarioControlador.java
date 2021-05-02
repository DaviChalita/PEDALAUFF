package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.uff.pedalauff.consts.PedalaUffConstants.LOGAR_NO_SITE;
import static com.uff.pedalauff.enums.TipoUsuario.NORMAL;

@RestController
public class UsuarioControlador {

    @Autowired
    private UsuarioRepo repo;

    public static String userIdent;

    @CrossOrigin
    @PostMapping(path = "/usuario/seusdados")
    public String consultar() {
        if (userIdent != null) {
            try {
                Usuario usuario = repo.findById(Integer.parseInt(userIdent)).get();
                return "Dados do usuário logado: " +
                        "\nNome: " + usuario.getNome() +
                        "\nMatrícula: " + usuario.getMatricula() +
                        "\nEmail: " + usuario.getEmail();
            } catch (NullPointerException | NumberFormatException e) {
                return "Usuário buscado não existe";
            }
        }
        return LOGAR_NO_SITE;
    }

    @CrossOrigin
    @PostMapping(path = "/usuario/salvar")
    public String salvar(@RequestBody Usuario usuario) {
        try {
            usuario.setTipoUsuario(NORMAL);
            repo.save(usuario);
        } catch (Exception e) {
            System.out.println("Exceção: " + e.getMessage());
            return "Já existe usuário cadastrado com esse email e/ou matrícula";
        }
        return "Usuário registrado com sucesso";
    }

    @CrossOrigin
    @PostMapping(path = "/usuario/logar")
    public String login(@RequestBody Map<String, String> json) {

        Integer idUsuario = repo.findByEmailAndSenha(json.get("email"), json.get("senha"));
        System.out.println("Id Usuario: " + idUsuario);
        if (idUsuario != null) {
            userIdent = String.valueOf(idUsuario);
            Usuario usuario = repo.findById(idUsuario).get();
            System.out.println("Usuário: " + usuario.getNome() + " logado com sucesso");
            return "true";
        }

        return "Email e/ou senha inválidos";
    }

    @PostMapping(path = "/usuario/deslogar")
    public String logout() {
        if (userIdent != null) {
            userIdent = "";
            System.out.println(userIdent);
            return "Usuário deslogado com sucesso";
        }
        return "Você não está logado";
    }

}