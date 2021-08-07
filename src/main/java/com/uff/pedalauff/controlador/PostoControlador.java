package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Posto;
import com.uff.pedalauff.repo.PostoRepo;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.uff.pedalauff.consts.PedalaUffConstants.LOGAR_NO_SITE;
import static com.uff.pedalauff.controlador.UsuarioControlador.userIdent;
import static com.uff.pedalauff.controlador.UsuarioControlador.userLvl;

@RestController
public class PostoControlador {

    @Autowired
    private PostoRepo postoRepo;

    @Autowired
    private VagaRepo vagaRepo;

    @CrossOrigin
    @PostMapping(path = "/posto/salvar")
    public String salvar(@RequestBody Posto posto) {
        if (userIdent != null && userLvl.equals("ADMIN")) {
            postoRepo.save(posto);
            return "Posto criado com sucesso.";
        }
        return LOGAR_NO_SITE;
    }

    @CrossOrigin
    @PostMapping(path = "/posto/consultar")
    public String consultar(@RequestBody Map<String, String> json) {
        if (userIdent != null) {
            Integer idPosto;
            Posto posto;
            try {
                idPosto = Integer.parseInt(json.get("idPosto"));
                System.out.println("idPosto: " + idPosto);
                posto = postoRepo.findById(idPosto).get();
            } catch (NullPointerException | NumberFormatException | NoSuchElementException e) {
                System.out.println("Erro ao pegar o id do posto: " + e);
                return "Posto buscado não existe";
            }

            List<Boolean> bicicletasDisp = new ArrayList<>(vagaRepo.qtdBicicletasDisp(posto.getIdPosto()));
            Integer qtdBicicletasDisp = 0;
            for (Boolean bicicleta : bicicletasDisp) {
                if (bicicleta)
                    qtdBicicletasDisp++;
            }

            List<Boolean> vagasDisp = new ArrayList<>(vagaRepo.qtdVagasDisp(posto.getIdPosto()));
            Integer qtdVagasDisp = 0;
            for (Boolean vaga : vagasDisp) {
                if (vaga)
                    qtdVagasDisp++;
            }

            System.out.println("Vagas Disps:" + qtdVagasDisp);

            return "Posto: " + idPosto + " tem " + qtdBicicletasDisp + " bicicleta(s) disponivel(is) para aluguel" +
                    " e " + qtdVagasDisp + " vaga(s) disponível(is).";

        }
        return LOGAR_NO_SITE;
    }

}