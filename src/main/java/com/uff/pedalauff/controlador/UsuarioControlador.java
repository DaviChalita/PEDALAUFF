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

    @PostMapping(path = "/usuario/salvar")
    public Usuario salvar(@RequestBody Usuario usuario) {
        return repo.save(usuario);
    }

    @PostMapping(path = "/usuario/logar")
    public ResponseEntity login(@RequestBody Map<String, String> json) {
        Integer idUsuarioEmail = -1;
        Integer idUsuarioSenha = -1;
        try {
            idUsuarioEmail = repo.findByEmail(json.get("email")).getIdUsuario();
            idUsuarioSenha = repo.findBySenha(json.get("senha")).getIdUsuario();
        } catch (NullPointerException e) {
            System.out.println("Não achou usuário relacionado ao email e/ou a senha");
        }
        Integer idUsuario = -1;
        if (idUsuarioEmail.equals(idUsuarioSenha))
            idUsuario = idUsuarioEmail;

        return repo.findById(idUsuario)
                .map(record -> ResponseEntity.ok().body("Usuario " + record.getNome() + " logado"))
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping(path = "/usuario/deslogar/{idUsuario}")
    public ResponseEntity logout(@PathVariable("idUsuario") Integer idUsuario) {
        return repo.findById(idUsuario)
                .map(record -> ResponseEntity.ok().body("Usuario " + record.getNome() + " deslogado"))
                .orElse(ResponseEntity.notFound().build());
    }

}
