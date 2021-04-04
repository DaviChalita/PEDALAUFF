package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Vaga;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VagaControlador {

    @Autowired
    private VagaRepo repo;

    @GetMapping(path = "/vaga/consulta/{idVaga}")
    public ResponseEntity consultar(@PathVariable("idVaga") Integer idVaga) {
        return repo.findById(idVaga)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/vaga/salvar")
    public Vaga salvar(@RequestBody Vaga vaga) {
        return repo.save(vaga);
    }

    @GetMapping(path = "/vaga/scanqrcode/{qrCode}")
    public ResponseEntity scanQrCode(@PathVariable("qrCode") String qrCode) {
        Integer idVaga = repo.findByQrCode(qrCode).getIdVaga();
        return repo.findById(idVaga)
                .map(record -> ResponseEntity.ok()
                        .body("Vaga " + record.getIdVaga() + " escaneada"))
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping(path = "vaga/alteradisp/{idVaga}")
    public ResponseEntity alteraDispVaga(@PathVariable("idVaga") Integer idVaga) {
        Vaga vaga = repo.findById(idVaga).get();
        vaga.alteraDisponibilidadeVaga(vaga);
        repo.save(vaga);
        return repo.findById(idVaga)
                .map(record -> ResponseEntity.ok()
                        .body("Novo estado da vaga: " + vaga.isDisponibilidade()))
                .orElse(ResponseEntity.notFound().build());
    }

}
