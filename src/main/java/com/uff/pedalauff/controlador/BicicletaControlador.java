package com.uff.pedalauff.controlador;

import com.uff.pedalauff.comuns.Comuns;
import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.modelo.Vaga;
import com.uff.pedalauff.repo.BicicletaRepo;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.uff.pedalauff.consts.PedalaUffConstants.ADMIN;
import static com.uff.pedalauff.consts.PedalaUffConstants.LOGAR_NO_SITE;
import static com.uff.pedalauff.enums.EstadoBicicleta.CRIADA;
import static com.uff.pedalauff.enums.EstadoBicicleta.EM_MANUTENCAO;

@RestController
public class BicicletaControlador {
    @Autowired
    private BicicletaRepo bicicletaRepo;

    @Autowired
    private VagaRepo vagaRepo;

    @CrossOrigin
    @PostMapping(path = "/bicicleta/consulta")
    public String consultar(@RequestBody Map<String, String> json) {
        if (UsuarioControlador.getUserIdent() != null && UsuarioControlador.getUserLvl().equals(ADMIN)) {
            Bicicleta bicicleta;
            Vaga vaga;
            String resp = "";
            try {
                bicicleta = bicicletaRepo.findByQrCode(json.get("qrCodeBicicleta"));
                resp += "QRCode da bicicleta buscada: " + bicicleta.getQrCode() + "\n" +
                        "Estado da bicicleta: " + bicicleta.getEstadoAtual();
            } catch (NullPointerException | NumberFormatException e) {
                return "Favor inserir um id de bicicleta válido";
            }

            try {
                vaga = vagaRepo.findById(bicicleta.getIdBicicleta()).get();
                resp += "\nE se encontra na vaga de QRCode: " + vaga.getQrCode();
            } catch (NullPointerException e) {
                System.out.println("Bicicleta não está em nenhuma vaga");
            }

            return resp;

        }
        return LOGAR_NO_SITE;
    }

    @CrossOrigin
    @PostMapping(path = "/bicicleta/salvar")
    public String salvar(@RequestBody Bicicleta bicicleta) {
        Comuns comuns = new Comuns();
        if (UsuarioControlador.getUserIdent() != null && UsuarioControlador.getUserLvl().equals(ADMIN)) {
            bicicleta.setQrCode(comuns.geraQrCodeAleatorio());
            bicicleta.setEstadoAtual(CRIADA);
            bicicletaRepo.save(bicicleta);
            return "O QR Code da bicicleta criada é igual a: " + bicicleta.getQrCode();
        }
        return LOGAR_NO_SITE;
    }

    @CrossOrigin
    @PostMapping(path = "/bicicleta/manutencao")
    public String consertarBicicleta(@RequestBody Map<String, String> json) {
        if (UsuarioControlador.getUserIdent() != null && UsuarioControlador.getUserLvl().equals(ADMIN)) {
            Bicicleta bicicleta;
            try {
                bicicleta = bicicletaRepo.findByQrCode(json.get("qrCodeBicicleta"));
                if (bicicleta == null) {
                    return "Favor inserir o QRCode de uma bicicleta que existe";
                }
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
        return LOGAR_NO_SITE;
    }

}
