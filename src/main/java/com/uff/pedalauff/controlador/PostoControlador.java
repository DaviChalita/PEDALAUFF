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

    @GetMapping(path = "/posto/{idPosto}")
    public ResponseEntity consultar(@PathVariable("idPosto") Integer idPosto) {
        return repo.findById(idPosto)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/posto/salvar")
    public Posto salvar(@RequestBody Posto posto) {
        return repo.save(posto);
    }

    @GetMapping(path = "/posto/vagasdisp/{idPosto}")
    public ResponseEntity dispVagas(@PathVariable("idPosto") Integer idPosto) {
        Posto posto = repo.findById(idPosto).get();
        ResponseEntity responseEntity;
        if (posto.temVagasDisp(posto.getQtdVagasDisp())) {
            responseEntity = repo.findById(idPosto)
                    .map(record -> ResponseEntity.ok()
                            .body("Existem: " + record.getQtdVagasDisp() + " vagas disponiveis"))
                    .orElse(ResponseEntity.notFound().build());
        } else {
            responseEntity = repo.findById(idPosto)
                    .map(record -> ResponseEntity.ok()
                            .body("Não existem vagas disponiveis"))
                    .orElse(ResponseEntity.notFound().build());
        }
        return responseEntity;
    }

}