package com.uff.pedalauff.controlador;

import com.uff.pedalauff.dto.UsuarioDTO;
import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static com.uff.pedalauff.consts.PedalaUffConstants.LOGAR_NO_SITE;
import static com.uff.pedalauff.consts.PedalaUffConstants.LOGAR_NO_SITE_BUILDER;
import static com.uff.pedalauff.enums.TipoUsuario.NORMAL;

@RestController
public class UsuarioControlador {

    @Autowired
    private UsuarioRepo repo;

    public static String userIdent;
    public static String userLvl;

    private Logger log;

    @CrossOrigin
    @PostMapping(path = "/usuario/seusdados")
    public String consultar() {
        if (userIdent != null) {
            try {
                Optional<Usuario> usuarioOpt = repo.findById(Integer.parseInt(userIdent));
                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    return "Dados do usuário logado: " +
                            "\nNome: " + usuario.getNome() +
                            "\nMatrícula: " + usuario.getMatricula() +
                            "\nEmail: " + usuario.getEmail();
                } else {
                    return "Usuário buscado não existe";
                }
            } catch (NumberFormatException e) {
                return "Usuário buscado não existe";
            }
        }
        return LOGAR_NO_SITE;
    }

    @CrossOrigin
    @PostMapping(path = "/usuario/salvar")
    public String salvar(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            repo.save(usuarioDTO.transformaParaObjeto());
        } catch (Exception e) {
            log.warning("Exceção: " + e.getMessage());
            return "Já existe usuário cadastrado com esse email e/ou matrícula";
        }
        return "Usuário registrado com sucesso";
    }

    @CrossOrigin
    @PostMapping(path = "/usuario/logar")
    public String login(@RequestBody Map<String, String> json) {

        Integer idUsuario = repo.findByEmailAndSenha(json.get("email"), json.get("senha"));
        if (idUsuario != null) {
            userIdent = String.valueOf(idUsuario);
            Optional<Usuario> usuarioOpt = repo.findById(idUsuario);
            Usuario usuario;
            if (usuarioOpt.isPresent()) {
                usuario = usuarioOpt.get();
                userLvl = String.valueOf(usuario.getTipoUsuario());
                return "true";
            }
        }
        return "Email e/ou senha inválidos";
    }

    @CrossOrigin
    @PostMapping(path = "/usuario/ver-usuarios")
    public StringBuilder verTodosUsuarios() {
        if (userIdent != null && userLvl.equals("ADMIN")) {
            Iterable<Usuario> usuarios = repo.findAll();
            StringBuilder listaUsuariosStr = new StringBuilder("Lista de Usuários:\n");
            for (Usuario usuario : usuarios) {
                listaUsuariosStr.append("Usuario: ")
                        .append(usuario.getIdUsuario())
                        .append("\nNome do usuario: ")
                        .append(usuario.getNome())
                        .append("\nNível de acesso: ")
                        .append(usuario.getTipoUsuario())
                        .append("\nEmail do usuário: ")
                        .append(usuario.getEmail())
                        .append("\nMatrícula do usuário: ")
                        .append(usuario.getMatricula()).append("\n");
            }
            return listaUsuariosStr;
        }
        return LOGAR_NO_SITE_BUILDER;
    }

    @PostMapping(path = "/usuario/deslogar")
    public String logout() {
        if (userIdent != null) {
            userIdent = "";
            return "Usuário deslogado com sucesso";
        }
        return "Você não está logado";
    }

}