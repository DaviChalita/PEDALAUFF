package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.modelo.Vaga;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VagaControlador {

    @Autowired
    private VagaRepo repo;

    @GetMapping(path = "/api/vaga/{idVaga}")
    public ResponseEntity consultar(@PathVariable("idVaga") Integer idVaga) {
        return repo.findById(idVaga)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/api/vaga/salvar")
    public Vaga salvar(@RequestBody Vaga vaga) {
        return repo.save(vaga);
    }

}
