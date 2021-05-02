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

import static com.uff.pedalauff.enums.EstadoBicicleta.NA_VAGA;

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

    @CrossOrigin
    @PostMapping(path = "/vaga/salvar")
    public String salvar(@RequestBody Map<String, String> json) {
        Vaga vaga = new Vaga();
        try {
            vaga.setQrCode(geraQrCodeAleatorio());
            vaga.setDisponibilidade(true);
        } catch (NullPointerException e) {
            return "Vaga está sendo criada sem Qr Code";
        }

        try {
            Posto posto = postoRepo.findById(
                    Integer.parseInt(json.get("idPosto"))).get();
            vaga.setPosto(posto);
        } catch (NullPointerException | NumberFormatException e) {
            return "Vaga não pode ser criada sem um posto vinculado, favor tentar novamente";
        }

        try {
            Bicicleta bicicleta = bicicletaRepo.findByQrCode(json.get("qrCodeBicicleta"));
            bicicleta.setEstadoAtual(NA_VAGA);
            vaga.setBicicleta(bicicleta);
            vaga.setDisponibilidade(false);
            bicicletaRepo.save(bicicleta);
        } catch (NullPointerException | NumberFormatException e) {
            System.out.println("Vaga sendo criada sem uma bicicleta vinculada");
        }
        vagaRepo.save(vaga);
        return "Vaga com o QRCode: " + vaga.getQrCode() + " criada com sucesso";
    }


    @GetMapping(path = "/vaga/scanqrcode/{qrCode}")
    public ResponseEntity scanQrCode(@PathVariable("qrCode") String qrCode) {
        Integer idVaga = vagaRepo.findByQrCode(qrCode).getIdVaga();
        return vagaRepo.findById(idVaga)
                .map(record -> ResponseEntity.ok()
                        .body("Vaga " + record.getIdVaga() + " escaneada"))
                .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @PostMapping(path = "vaga/inserebike")
    public String insereBikeNaVaga(@RequestBody Map<String, String> json) {
        Vaga vaga;
        try {
            vaga = vagaRepo.findByQrCode(json.get("qrCodeVaga"));
            vaga.alteraDisponibilidadeVaga(vaga);
        } catch (NullPointerException | NumberFormatException e) {
            return "Favor inserir o QRCode da vaga";
        }

        Bicicleta bicicleta;

        try {
            bicicleta = bicicletaRepo.findByQrCode(json.get("qrCodeBicicleta"));
            bicicleta.setEstadoAtual(NA_VAGA);
            vaga.setBicicleta(bicicleta);
        } catch (NullPointerException | NumberFormatException e) {
            return "Você está tentando inserir uma bicicleta na vaga, especifique o QRCode da bicicleta";
        }

        bicicletaRepo.save(bicicleta);
        vagaRepo.save(vaga);
        return "Bicicleta: " + bicicleta.getQrCode() + " inserida na vaga com sucesso";
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

}
