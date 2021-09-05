package com.uff.pedalauff.controlador;

import com.uff.pedalauff.comuns.Comuns;
import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.modelo.Posto;
import com.uff.pedalauff.modelo.Vaga;
import com.uff.pedalauff.repo.BicicletaRepo;
import com.uff.pedalauff.repo.PostoRepo;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;

import static com.uff.pedalauff.consts.PedalaUffConstants.LOGAR_NO_SITE;
import static com.uff.pedalauff.consts.PedalaUffConstants.LOGAR_NO_SITE_BUILDER;
import static com.uff.pedalauff.enums.EstadoBicicleta.NA_VAGA;

@RestController
public class VagaControlador {

    @Autowired
    private VagaRepo vagaRepo;

    @Autowired
    private PostoRepo postoRepo;

    @Autowired
    private BicicletaRepo bicicletaRepo;
    private Logger log;



    @CrossOrigin
    @PostMapping(path = "/vaga/ver-todas")
    public StringBuilder verTodasVagas() {
        if (UsuarioControlador.getUserIdent() != null && UsuarioControlador.getUserLvl().equals("ADMIN")) {
            Iterable<Vaga> vagas = vagaRepo.findAll();
            StringBuilder listaVagasStr = new StringBuilder("Lista de Vagas:\n");

            for (Vaga vaga : vagas) {
                listaVagasStr.append("Vaga: ")
                        .append(vaga.getIdVaga())
                        .append("\nQRCode da vaga: ")
                        .append(vaga.getQrCode())
                        .append("\nEndereço e campus do posto aonde a vaga se localiza: \n")
                        .append(vaga.getPosto().getEndereco()).append(", ")
                        .append(vaga.getPosto().getCampus()).append("\n");
                if (vaga.getBicicleta() != null) {
                    listaVagasStr.append("QRCode da bicicleta nessa vaga: ").append(vaga.getBicicleta().getQrCode()).append("\n");
                } else {
                    listaVagasStr.append("Não há bicicleta nessa vaga\n");
                }
                listaVagasStr.append("\n");
            }
            return listaVagasStr;
        }
        return LOGAR_NO_SITE_BUILDER;
    }

    @CrossOrigin
    @PostMapping(path = "/vaga/salvar")
    public String salvar(@RequestBody Map<String, String> json) {
        Comuns comuns = new Comuns();
        if (UsuarioControlador.getUserIdent() != null) {
            Vaga vaga = new Vaga();
            try {
                vaga.setQrCode(comuns.geraQrCodeAleatorio());
                vaga.setDisponibilidade(true);
            } catch (NullPointerException e) {
                return "Vaga está sendo criada sem Qr Code";
            }

            try {
                Optional<Posto> postoOpt = postoRepo.findById(Integer.parseInt(json.get("idPosto")));
                if (postoOpt.isPresent()) {
                    Posto posto = postoOpt.get();
                    vaga.setPosto(posto);
                } else {
                    return "Posto inserido não existe";
                }
            } catch (NumberFormatException e) {
                return "Vaga não pode ser criada sem um posto vinculado, favor tentar novamente";
            }

            try {
                String qrCode = json.get("qrCodeBicicleta");
                if (!qrCode.equals("")) {
                    Bicicleta bicicleta = bicicletaRepo.findByQrCode(qrCode);
                    if (bicicleta == null) {
                        return "QRCode da bicicleta inserido é inválido";
                    }
                    bicicleta.setEstadoAtual(NA_VAGA);
                    vaga.setBicicleta(bicicleta);
                    vaga.setDisponibilidade(false);
                    bicicletaRepo.save(bicicleta);
                }
            } catch (NullPointerException | NumberFormatException e) {
                log.warning("Vaga sendo criada sem uma bicicleta vinculada");
            } catch (NoSuchElementException e) {
                return "Bicicleta inserida não existe";
            }

            vagaRepo.save(vaga);
            return "Vaga com o QRCode: " + vaga.getQrCode() + " criada com sucesso";

        }
        return LOGAR_NO_SITE;
    }

    @CrossOrigin
    @PostMapping(path = "vaga/inserebike")
    public String insereBikeNaVaga(@RequestBody Map<String, String> json) {
        if (UsuarioControlador.getUserIdent() != null && UsuarioControlador.getUserLvl().equals("ADMIN")) {
            Vaga vaga;
            try {
                vaga = vagaRepo.findByQrCode(json.get("qrCodeVaga"));
                if (vaga == null) {
                    return "Favor inserir um QRCode de uma vaga válida";
                }
                if (!vaga.isDisponibilidade()) {
                    return "Vaga já possui uma bicicleta";
                }
                vaga.alteraDisponibilidadeVaga(vaga);
            } catch (NullPointerException | NumberFormatException e) {
                return "Favor inserir o QRCode da vaga";
            }

            Bicicleta bicicleta;

            try {
                bicicleta = bicicletaRepo.findByQrCode(json.get("qrCodeBicicleta"));
                if (bicicleta == null) {
                    return "Favor inserir um QRCode de uma bicicleta válida";
                }
                if (bicicleta.getEstadoAtual() == NA_VAGA) {
                    return "Bicicleta buscada já está em uma vaga";
                }
                bicicleta.setEstadoAtual(NA_VAGA);
                vaga.setBicicleta(bicicleta);
            } catch (NullPointerException | NumberFormatException e) {
                return "Você está tentando inserir uma bicicleta na vaga, especifique o QRCode da bicicleta";
            }

            bicicletaRepo.save(bicicleta);
            vagaRepo.save(vaga);
            return "Bicicleta: " + bicicleta.getQrCode() + " inserida na vaga com sucesso";
        }
        return LOGAR_NO_SITE;
    }


}
