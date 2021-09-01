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
    public static String userLvl;

    public String userId(){ return userIdent;}
    public String userLvl(){ return userIdent;}

    @CrossOrigin
    @PostMapping(path = "/usuario/seusdados")
    public String consultar() {
        userIdent = userId();

        if (userIdent != null) {
            try {
                int test = Integer.parseInt(userIdent);
                Usuario usuario = repo.findById(test).get();
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
        System.out.println("json: " + json);
        Integer idUsuario = repo.findByEmailAndSenha(json.get("email"), json.get("senha"));
        System.out.println("Id Usuario: " + idUsuario);
        if (idUsuario != null) {
            userIdent = String.valueOf(idUsuario);
            Usuario usuario = repo.findById(idUsuario).get();
            userLvl = String.valueOf(usuario.getTipoUsuario());
            System.out.println("Nivel usuario: " + userLvl);
            System.out.println("Usuário: " + usuario.getNome() + " logado com sucesso");
            return "true";
        }

        return "Email e/ou senha inválidos";
    }

    @CrossOrigin
    @PostMapping(path = "/usuario/ver-usuarios")
    public String verTodosUsuarios() {
        userIdent = userId();
        userLvl = userLvl();
        if (userIdent != null && userLvl.equals("ADMIN")) {
            Iterable<Usuario> usuarios = repo.findAll();
            String listaUsuariosStr = "Lista de Usuários:\n";
            for (Usuario usuario : usuarios) {
                listaUsuariosStr += "Usuario: " + usuario.getIdUsuario() + "\n"
                        + "Nome do usuario: " + usuario.getNome() + "\n"
                        + "Nível de acesso: " + usuario.getTipoUsuario() + "\n"
                        + "Email do usuário: " + usuario.getEmail() + "\n"
                        + "Matrícula do usuário: " + usuario.getMatricula() + "\n";
            }
            System.out.println(listaUsuariosStr);
            return listaUsuariosStr;
        }
        return LOGAR_NO_SITE;
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