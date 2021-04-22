package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Posto;
import com.uff.pedalauff.repo.PostoRepo;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostoControlador {

    @Autowired
    private PostoRepo postoRepo;

    @Autowired
    private VagaRepo vagaRepo;

    @GetMapping(path = "/posto/{idPosto}")
    public ResponseEntity consultar(@PathVariable("idPosto") Integer idPosto) {
        return postoRepo.findById(idPosto)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/posto/salvar")
    public Posto salvar(@RequestBody Posto posto) {
        return postoRepo.save(posto);
    }

    @GetMapping(path = "/posto/dispVagas/{idPosto}")
    public String dispVagas(@PathVariable("idPosto") Integer idPosto) {
        Integer vagasDisp = vagaRepo.qtdVagasDisp(idPosto);
        System.out.println("Vagas Disps:" + vagasDisp);

        return "Numero de vagas disponiveis no posto: " + idPosto + " = " + vagasDisp;
    }

    @GetMapping(path = "/posto/dispBicicletas/{idPosto}")
    public String dispBicicletas(@PathVariable("idPosto") Integer idPosto) {
        Integer bicicletasDisp = vagaRepo.qtdBicicletasDisp(idPosto);
        System.out.println("Vagas Disps:" + bicicletasDisp);

        return "Numero de bicicletas disponiveis no posto: " + idPosto + " = " + bicicletasDisp;
    }

}