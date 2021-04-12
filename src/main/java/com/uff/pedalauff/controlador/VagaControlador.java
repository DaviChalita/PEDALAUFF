package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.modelo.Posto;
import com.uff.pedalauff.modelo.Vaga;
import com.uff.pedalauff.repo.BicicletaRepo;
import com.uff.pedalauff.repo.PostoRepo;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class VagaControlador {

    @Autowired
    private VagaRepo vagaRepo;

    @Autowired
    private PostoRepo postoRepo;

    @Autowired
    private BicicletaRepo bicicletaRepo;

    @GetMapping(path = "/vaga/consulta/{idVaga}")
    public ResponseEntity consultar(@PathVariable("idVaga") Integer idVaga) {
        return vagaRepo.findById(idVaga)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/vaga/salvar")
    public Vaga salvar(@RequestBody Map<String, String> json) {
        Vaga vaga = new Vaga();
        vaga.setQrCode(json.get("qrCode"));
        vaga.setDisponibilidade(
                Boolean.parseBoolean(json.get("disponibilidade")));
        Posto posto = postoRepo.findById(
                Integer.parseInt(json.get("idPosto"))).get();
        Bicicleta bicicleta = bicicletaRepo.findById(
                Integer.parseInt(json.get("idBicicleta"))).get();
        vaga.setPosto(posto);
        vaga.setBicicleta(bicicleta);
        return vagaRepo.save(vaga);
    }


    @GetMapping(path = "/vaga/scanqrcode/{qrCode}")
    public ResponseEntity scanQrCode(@PathVariable("qrCode") String qrCode) {
        Integer idVaga = vagaRepo.findByQrCode(qrCode).getIdVaga();
        return vagaRepo.findById(idVaga)
                .map(record -> ResponseEntity.ok()
                        .body("Vaga " + record.getIdVaga() + " escaneada"))
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping(path = "vaga/alteradisp/{idVaga}")
    public ResponseEntity alteraDispVaga(@PathVariable("idVaga") Integer idVaga) {
        Vaga vaga = vagaRepo.findById(idVaga).get();
        vaga.alteraDisponibilidadeVaga(vaga);
        vagaRepo.save(vaga);
        return vagaRepo.findById(idVaga)
                .map(record -> ResponseEntity.ok()
                        .body("Novo estado da vaga: " + vaga.isDisponibilidade()))
                .orElse(ResponseEntity.notFound().build());
    }

    //todo refazer
    @GetMapping(path = "/vaga/disp/{idPosto}")
    public /*ResponseEntity*/ String dispVagas(@PathVariable("idPosto") Integer idPosto) {
        //Posto posto = postoRepo.findById(idPosto).get();
        Integer vagasDisp = vagaRepo.qtdVagasDisp(idPosto);
        /*ResponseEntity responseEntity;

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
        return responseEntity;*/
        return "Numero de vagas disponiveis no posto: " + idPosto + " = " + vagasDisp;
    }

}
