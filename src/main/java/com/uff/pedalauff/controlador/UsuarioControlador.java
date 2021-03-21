package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsuarioControlador {

    @Autowired
    private UsuarioRepo repo;

    @GetMapping(path = "/api/usuario/{idUsuario}")
    public ResponseEntity consultar(@PathVariable("idUsuario") Integer idUsuario) {
        return repo.findById(idUsuario)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/api/usuario/salvar")
    public Usuario salvar(@RequestBody Usuario usuario) {
        return repo.save(usuario);
    }

}
