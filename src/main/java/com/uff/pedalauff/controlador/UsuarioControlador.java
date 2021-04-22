package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UsuarioControlador {

    @Autowired
    private UsuarioRepo repo;

    @GetMapping(path = "/usuario/{idUsuario}")
    public ResponseEntity consultar(@PathVariable("idUsuario") Integer idUsuario) {
        return repo.findById(idUsuario)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @PostMapping(path = "/usuario/salvar")
    public String salvar(@RequestBody Usuario usuario) {
        try {
            repo.save(usuario);
        } catch (Exception e) {
            System.out.println("Exceção: " + e.getMessage());
            return "Já existe usuário cadastrado com esse email e/ou matrícula";
        }
        return "Usuário registrado com sucesso";
    }

    //todo fazer verificacao via query
    @PostMapping(path = "/usuario/logar")
    public String login(@RequestBody Map<String, String> json) {

        try {
            Integer idUsuario = repo.findByEmailAndSenha(json.get("email"), json.get("senha"));
            Usuario usuario = repo.findById(idUsuario).get();
            return "Usuário: " + usuario.getNome() + " logado com sucesso";
        } catch (NullPointerException e) {
            return "Email e/ou senha inválidos";
        }

    }

    @GetMapping(path = "/usuario/deslogar/{idUsuario}")
    public ResponseEntity logout(@PathVariable("idUsuario") Integer idUsuario) {
        return repo.findById(idUsuario)
                .map(record -> ResponseEntity.ok().body("Usuario " + record.getNome() + " deslogado"))
                .orElse(ResponseEntity.notFound().build());
    }

}