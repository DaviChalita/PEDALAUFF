package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Aluguel;
import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.modelo.Vaga;
import com.uff.pedalauff.repo.AluguelRepo;
import com.uff.pedalauff.repo.BicicletaRepo;
import com.uff.pedalauff.repo.UsuarioRepo;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

import static com.uff.pedalauff.enums.EstadoBicicleta.EM_USO;
import static com.uff.pedalauff.enums.EstadoBicicleta.NA_VAGA;

@RestController
public class AluguelControlador {

    @Autowired
    private AluguelRepo aluguelRepo;

    @Autowired
    private BicicletaRepo bicicletaRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private VagaRepo vagaRepo;

    @GetMapping(path = "/aluguel/{idAluguel}")
    public ResponseEntity consultar(@PathVariable("idAluguel") Integer idAluguel) {
        return aluguelRepo.findById(idAluguel)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/aluguel/alugar")
    public String alugar(@RequestBody Map<String, String> json) {
        Aluguel aluguel = new Aluguel();
        aluguel.setDthrAluguel(new Date(System.currentTimeMillis()));

        Bicicleta bicicleta;
        try {
            bicicleta = bicicletaRepo.findById(Integer.valueOf(json.get("idBicicleta"))).get();
        } catch (NullPointerException | NumberFormatException e) {
            try {
                bicicleta = bicicletaRepo.findByQrCode(json.get("qrCode"));
            } catch (NullPointerException | NumberFormatException ex) {
                return "Você está tentando encontrar uma bicicleta que não existe.";
            }
        }

        if (bicicleta.getEstadoAtual() == EM_USO) {
            return "Você está tentando alugar uma bicicleta que já está em uso, favor alugar uma bicicleta disponível";
        }

        Vaga vaga;
        try {
            Integer idVaga = vagaRepo.findByBicicleta(bicicleta.getIdBicicleta());
            vaga = vagaRepo.findById(idVaga).get();
        } catch (NullPointerException e) {
            return "Você está tentando encontrar uma vaga que não existe.";
        }

        vaga.alteraDisponibilidadeVaga(vaga);
        vaga.setBicicleta(null);
        bicicleta.setEstadoAtual(EM_USO);

        Usuario usuario;
        try {
            usuario = usuarioRepo.findById(Integer.valueOf(json.get("idUsuario"))).get();
        } catch (NullPointerException e) {
            return "Você está tentando encontrar um usuário que não existe.";
        }

        aluguel.setUsuarioAlugado(usuario);
        aluguel.setBicicletaAlugada(bicicleta);
        bicicletaRepo.save(bicicleta);
        vagaRepo.save(vaga);
        aluguelRepo.save(aluguel);
        return "Bicicleta: " + bicicleta.getIdBicicleta() + " alugada com sucesso";
    }

    @PostMapping(path = "/aluguel/devolver")
    public String devolver(@RequestBody Map<String, String> json) {
        Aluguel aluguel = new Aluguel();
        Bicicleta bicicleta;

        try {
            bicicleta = bicicletaRepo.findById(Integer.valueOf(json.get("idBicicleta"))).get();

            if (bicicleta.getEstadoAtual() == NA_VAGA) {
                return "Você está tentando devolver uma bicicleta que já está na vaga," +
                        " favor devolver a bicicleta que você está usando";
            }

            bicicleta.setEstadoAtual(NA_VAGA);
        } catch (NullPointerException | NumberFormatException e) {
            return "Você está tentando encontrar uma bicicleta que não existe.";
        }


        try {
            Integer idAluguel = aluguelRepo.findByIdUsuarioAndIdBicicleta(Integer.parseInt(json.get("idUsuario")),
                    Integer.parseInt(json.get("idBicicleta")));
            aluguel = aluguelRepo.findById(idAluguel).get();
        } catch (NullPointerException | NumberFormatException e) {
            return "Não conseguiu achar o aluguel";
        }

        aluguel.setDthrDevolucao(new Date(System.currentTimeMillis()));

        Vaga vaga;
        try {
            vaga = vagaRepo.findById(Integer.valueOf(json.get("idVaga"))).get();
            vaga.alteraDisponibilidadeVaga(vaga);
            vagaRepo.save(vaga);
        } catch (NullPointerException | NumberFormatException e) {
            try {
                vaga = vagaRepo.findByQrCode(json.get("qrCode"));
                vaga.alteraDisponibilidadeVaga(vaga);
                vagaRepo.save(vaga);
            } catch (NullPointerException | NumberFormatException ex) {
                return "Você está tentando encontrar uma vaga que não existe.";
            }
        }


        bicicletaRepo.save(bicicleta);
        aluguelRepo.save(aluguel);
        return "Bicicleta: " + bicicleta.getIdBicicleta() + " devolvida com sucesso";

    }



   /* @GetMapping(path = "/aluguel/altstatbike/{idBicicleta}")
    public String alteraEstadoBicicleta(@PathVariable("idBicicleta") Integer idBicicleta) {
        Bicicleta bicicleta = bicicletaRepo.findById(idBicicleta).get();
        if (bicicleta.getEstadoAtual().equals(NA_VAGA)) {
            bicicleta.setEstadoAtual(EM_USO);
        } else {
            bicicleta.setEstadoAtual(NA_VAGA);
        }
        bicicletaRepo.save(bicicleta);
        return "Estado da Bicicleta: " + bicicleta.getIdBicicleta() + " atualizado para: " + bicicleta.getEstadoAtual();
    }*/
}
