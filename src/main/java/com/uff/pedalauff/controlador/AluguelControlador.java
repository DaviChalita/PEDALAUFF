package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Aluguel;
import com.uff.pedalauff.repo.AluguelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AluguelControlador {

    @Autowired
    private AluguelRepo repo;

    @GetMapping(path = "/api/aluguel/{idAluguel}")
    public ResponseEntity consultar(@PathVariable("idAluguel") Integer idAluguel) {
        return repo.findById(idAluguel)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/api/aluguel/salvar")
    public Aluguel salvar(@RequestBody Aluguel aluguel) {
        return repo.save(aluguel);
    }

}
