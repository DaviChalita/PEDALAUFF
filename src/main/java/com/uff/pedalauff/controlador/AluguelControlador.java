package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Aluguel;
import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.repo.AluguelRepo;
import com.uff.pedalauff.repo.BicicletaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.uff.pedalauff.enums.EstadoBicicleta.EM_USO;
import static com.uff.pedalauff.enums.EstadoBicicleta.NA_VAGA;

@RestController
public class AluguelControlador {

    @Autowired
    private AluguelRepo repoAluguel;

    @Autowired
    private BicicletaRepo repoBicicleta;

    @GetMapping(path = "/api/aluguel/{idAluguel}")
    public ResponseEntity consultar(@PathVariable("idAluguel") Integer idAluguel) {
        return repoAluguel.findById(idAluguel)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/api/aluguel/salvar")
    public Aluguel salvar(@RequestBody Aluguel aluguel) {
        return repoAluguel.save(aluguel);
    }

    @PostMapping(path = "/api/aluguel/altstatbike/{idBicicleta}")
    public String alteraEstadoBicicleta(@PathVariable("idBicicleta") Integer idBicicleta) {
        Bicicleta bicicleta = repoBicicleta.findById(idBicicleta).get();
        if (bicicleta.getEstadoAtual().equals(NA_VAGA)) {
            bicicleta.setEstadoAtual(EM_USO);
        } else {
            bicicleta.setEstadoAtual(NA_VAGA);
        }
        repoBicicleta.save(bicicleta);
        return "Estado da Bicicleta: " + idBicicleta + " atualizado para: " + bicicleta.getEstadoAtual();
    }

}
