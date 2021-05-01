package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.repo.BicicletaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.uff.pedalauff.enums.EstadoBicicleta.EM_USO;

@RestController
public class BicicletaControlador {
    @Autowired
    private BicicletaRepo repo;

    @GetMapping(path = "/bicicleta/consulta/{idBicicleta}")
    public ResponseEntity consultar(@PathVariable("idBicicleta") Integer idBicicleta) {
        return repo.findById(idBicicleta)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/bicicleta/salvar")
    public String salvar(@RequestBody Bicicleta bicicleta) {
        bicicleta.setQrCode(geraQrCodeAleatorio());
        bicicleta.setEstadoAtual(EM_USO);
        repo.save(bicicleta);
        return "O QR Code da bicicleta criada é igual a: " + bicicleta.getQrCode();
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
        Integer idBicicleta = repo.findByQrCode(qrCode).getIdBicicleta();
        return repo.findById(idBicicleta)
                .map(record -> ResponseEntity.ok()
                        .body("Bicicleta " + record.getIdBicicleta() + " escaneada"))
                .orElse(ResponseEntity.notFound().build());
    }

}
