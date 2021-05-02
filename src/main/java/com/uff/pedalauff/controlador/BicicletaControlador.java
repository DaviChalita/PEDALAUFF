package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.modelo.Vaga;
import com.uff.pedalauff.repo.BicicletaRepo;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.uff.pedalauff.enums.EstadoBicicleta.EM_MANUTENCAO;
import static com.uff.pedalauff.enums.EstadoBicicleta.CRIADA;

@RestController
public class BicicletaControlador {
    @Autowired
    private BicicletaRepo bicicletaRepo;

    @Autowired
    private VagaRepo vagaRepo;

    @GetMapping(path = "/bicicleta/consulta/{idBicicleta}")
    public ResponseEntity consultar(@PathVariable("idBicicleta") Integer idBicicleta) {
        return bicicletaRepo.findById(idBicicleta)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @PostMapping(path = "/bicicleta/salvar")
    public String salvar(@RequestBody Bicicleta bicicleta) {
        bicicleta.setQrCode(geraQrCodeAleatorio());
        bicicleta.setEstadoAtual(CRIADA);
        bicicletaRepo.save(bicicleta);
        return "O QR Code da bicicleta criada é igual a: " + bicicleta.getQrCode();
    }

    @CrossOrigin
    @PostMapping(path = "/bicicleta/manutencao")
    public String consertarBicicleta(@RequestBody Map<String, String> json) {

        Bicicleta bicicleta;
        try {
            bicicleta = bicicletaRepo.findByQrCode(json.get("qrCodeBicicleta"));
        } catch (NullPointerException | NumberFormatException e) {
            return "Favor inserir um id de bicicleta válido";
        }

        try {
            Integer idVaga = vagaRepo.findByBicicletaQrCode(bicicleta.getQrCode());
            Vaga vaga = vagaRepo.findById(idVaga).get();
            vaga.setDisponibilidade(true);
            vaga.setBicicleta(null);
            vagaRepo.save(vaga);
        } catch (NullPointerException e) {
            return "Bicicleta não está em nenhuma vaga, favor devolvê-la a uma vaga antes de consertá-la";
        }

        bicicleta.setEstadoAtual(EM_MANUTENCAO);
        bicicletaRepo.save(bicicleta);
        return "Status da bicicleta atualizado com sucesso.";
    }


    private String geraQrCodeAleatorio() {
        //https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
        String ALPHA_NUMERIC_STRING = "0123456789abcdefghijklmnopqrstuvxyz";
        int n = 4;

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {

            int index = (int) (ALPHA_NUMERIC_STRING.length() * Math.random());

            sb.append(ALPHA_NUMERIC_STRING.charAt(index));
        }

        return sb.toString();
    }

    @GetMapping(path = "/bicicleta/scanqrcode/{qrCode}")
    public ResponseEntity scanQrCode(@PathVariable("qrCode") String qrCode) {
        Integer idBicicleta = bicicletaRepo.findByQrCode(qrCode).getIdBicicleta();
        return bicicletaRepo.findById(idBicicleta)
                .map(record -> ResponseEntity.ok()
                        .body("Bicicleta " + record.getIdBicicleta() + " escaneada"))
                .orElse(ResponseEntity.notFound().build());
    }

}
