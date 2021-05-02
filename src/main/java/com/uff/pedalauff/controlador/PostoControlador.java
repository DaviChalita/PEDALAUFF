package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Posto;
import com.uff.pedalauff.repo.PostoRepo;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.NoSuchElementException;

import static com.uff.pedalauff.consts.PedalaUffConstants.LOGAR_NO_SITE;
import static com.uff.pedalauff.controlador.UsuarioControlador.userIdent;

@RestController
public class PostoControlador {

    @Autowired
    private PostoRepo postoRepo;

    @Autowired
    private VagaRepo vagaRepo;

    @CrossOrigin
    @PostMapping(path = "/posto/salvar")
    public String salvar(@RequestBody Posto posto) {
        if (userIdent != null) {
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

            Integer bicicletasDisp = vagaRepo.qtdBicicletasDisp(posto.getIdPosto());
            Integer vagasDisp = vagaRepo.qtdVagasDisp(posto.getIdPosto());
            System.out.println("Vagas Disps:" + bicicletasDisp);

            return "Posto: " + idPosto + " tem " + bicicletasDisp + " bicicleta(s) disponivel(is) para aluguel" +
                    " e " + vagasDisp + " vaga(s) disponível(is).";

        }
        return LOGAR_NO_SITE;
    }

}