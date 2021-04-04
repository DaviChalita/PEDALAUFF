package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Aluguel;
import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.repo.AluguelRepo;
import com.uff.pedalauff.repo.BicicletaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

import static com.uff.pedalauff.enums.EstadoBicicleta.EM_USO;
import static com.uff.pedalauff.enums.EstadoBicicleta.NA_VAGA;

@RestController
public class AluguelControlador {

    @Autowired
    private AluguelRepo repoAluguel;

    @Autowired
    private BicicletaRepo repoBicicleta;

    @GetMapping(path = "/aluguel/{idAluguel}")
    public ResponseEntity consultar(@PathVariable("idAluguel") Integer idAluguel) {
        return repoAluguel.findById(idAluguel)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    /*@PostMapping(path = "/aluguel/salvar")
    public Aluguel salvar(@RequestBody Aluguel aluguel) {
        aluguel.setDthrAluguel(new Date(System.currentTimeMillis()));
        aluguel.setDthrDevolucao(new Date(System.currentTimeMillis()));
        return repoAluguel.save(aluguel);
    }*/

    @PostMapping(path = "/aluguel/salvar")
    public Aluguel salvar(@RequestBody Map<String, Integer> json) {
        Aluguel aluguel = new Aluguel();
        aluguel.setDthrAluguel(new Date(System.currentTimeMillis()));
        aluguel.setDthrDevolucao(new Date(System.currentTimeMillis()));
        Bicicleta bicicleta = repoBicicleta.findById(json.get("idBicicleta")).get();
        aluguel.setBicicletaAlugada(bicicleta);
        return repoAluguel.save(aluguel);
    }

    @GetMapping(path = "/aluguel/todosAlugueis")
    public Iterable<Aluguel> todosAlugueis() {
        return repoAluguel.findAll();

    }

    @PostMapping(path = "/aluguel/altstatbike/{idBicicleta}")
    public String alteraEstadoBicicleta(@PathVariable("idBicicleta") Integer idBicicleta) {
        Bicicleta bicicleta = repoBicicleta.findById(idBicicleta).get();
        if (bicicleta.getEstadoAtual().equals(NA_VAGA)) {
            bicicleta.setEstadoAtual(EM_USO);
        } else {
            bicicleta.setEstadoAtual(NA_VAGA);
        }
        repoBicicleta.save(bicicleta);
        return "Estado da Bicicleta: " + bicicleta.getIdBicicleta() + " atualizado para: " + bicicleta.getEstadoAtual();
    }
}
