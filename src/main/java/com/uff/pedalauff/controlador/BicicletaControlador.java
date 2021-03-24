package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.repo.BicicletaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BicicletaControlador {
    @Autowired
    private BicicletaRepo repo;

    @GetMapping(path = "/api/bicicleta/{idBicicleta}")
    public ResponseEntity consultar(@PathVariable("idBicicleta") Integer idBicicleta){
        return repo.findById(idBicicleta)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/api/bicicleta/salvar")
    public Bicicleta salvar(@RequestBody Bicicleta bicicleta){
        return repo.save(bicicleta);
    }

}
