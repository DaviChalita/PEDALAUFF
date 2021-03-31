package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Posto;
import com.uff.pedalauff.repo.PostoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostoControlador {

    @Autowired
    private PostoRepo repo;

    @GetMapping(path = "/api/posto/{idPosto}")
    public ResponseEntity consultar(@PathVariable("idPosto") Integer idPosto) {
        return repo.findById(idPosto)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/api/posto/salvar")
    public Posto salvar(@RequestBody Posto posto) {
        return repo.save(posto);
    }

}