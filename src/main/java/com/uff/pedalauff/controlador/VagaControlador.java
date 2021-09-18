package com.uff.pedalauff.controlador;

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
import java.util.Random;

import static com.uff.pedalauff.consts.PedalaUffConstants.LOGAR_NO_SITE;
import static com.uff.pedalauff.controlador.UsuarioControlador.userIdent;
import static com.uff.pedalauff.controlador.UsuarioControlador.userLvl;
import static com.uff.pedalauff.enums.EstadoBicicleta.NA_VAGA;

@RestController
public class VagaControlador {

    @Autowired
    private VagaRepo vagaRepo;

    @Autowired
    private Random random;

    @Autowired
    private PostoRepo postoRepo;

    @Autowired
    private BicicletaRepo bicicletaRepo;

    public String userId(){ return userIdent;}
    public String userLvl(){ return userIdent;}

    @CrossOrigin
    @PostMapping(path = "/vaga/ver-todas")
    public String verTodasVagas() {
        userIdent = userId();
        userLvl = userLvl();

        if (userIdent != null && userLvl.equals("ADMIN")) {
            Iterable<Vaga> vagas = vagaRepo.findAll();
            String listaVagasStr = "Lista de Vagas:\n";

            for (Vaga vaga : vagas) {
                listaVagasStr += "Vaga: " + vaga.getIdVaga() + "\n"
                        + "QRCode da vaga: " + vaga.getQrCode() + "\n"
                        + "Endereço e campus do posto aonde a vaga se localiza: \n"
                        + vaga.getPosto().getEndereco() + ", " + vaga.getPosto().getCampus() + "\n";
                if (vaga.getBicicleta() != null) {
                    listaVagasStr += "QRCode da bicicleta nessa vaga: " + vaga.getBicicleta().getQrCode() + "\n";
                } else {
                    listaVagasStr += "Não há bicicleta nessa vaga\n";
                }
                listaVagasStr += "\n";
            }
            return listaVagasStr;
        }
        return LOGAR_NO_SITE;
    }

    @CrossOrigin
    @PostMapping(path = "/vaga/salvar")
    public String salvar(@RequestBody Map<String, String> json) {
        userIdent = userId();
        userLvl = userLvl();

        if (userIdent != null) {
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
            } catch (NoSuchElementException e) {
                return "Posto inserido não existe";
            }

            try {
                String qrCode = json.get("qrCodeBicicleta");
                if (qrCode != "") {
                    Bicicleta bicicleta = bicicletaRepo.findByQrCode(qrCode);
                    if (bicicleta == null) {
                        return "QRCode da bicicleta inserido é inválido";
                    }
                    System.out.println("Bicicleta: " + bicicleta.getIdBicicleta());
                    bicicleta.setEstadoAtual(NA_VAGA);
                    vaga.setBicicleta(bicicleta);
                    vaga.setDisponibilidade(false);
                    bicicletaRepo.save(bicicleta);
                }
            } catch (NullPointerException | NumberFormatException e) {
                System.out.println("Vaga sendo criada sem uma bicicleta vinculada");
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
        userIdent = userId();
        userLvl = userLvl();

        if (userIdent != null && userLvl.equals("ADMIN")) {
            Vaga vaga;
            try {
                vaga = vagaRepo.findByQrCode(json.get("qrCodeVaga"));
                if (vaga == null) {
                    return "Favor inserir um QRCode de uma vaga válida";
                }
                if(!vaga.isDisponibilidade()){
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

    public String geraQrCodeAleatorio() {
        //https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
        String ALPHA_NUMERIC_STRING = "0123456789abcdefghijklmnopqrstuvxyz";
        int n = 4;

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {

            int index = random.nextInt(ALPHA_NUMERIC_STRING.length());
            //int index = (int) (ALPHA_NUMERIC_STRING.length() * Math.random());

            sb.append(ALPHA_NUMERIC_STRING.charAt(index));
        }
        return sb.toString();
    }

}
