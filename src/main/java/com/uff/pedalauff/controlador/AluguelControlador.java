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

    //todo criar alugar (sem data de devolucao) e criar devolucao
    @PostMapping(path = "/aluguel/alugar")
    public String alugar(@RequestBody Map<String, Integer> json) {
        Aluguel aluguel = new Aluguel();
        aluguel.setDthrAluguel(new Date(System.currentTimeMillis()));
        aluguel.setDthrDevolucao(new Date(System.currentTimeMillis()));

        Bicicleta bicicleta;
        try {
            bicicleta = bicicletaRepo.findById(json.get("idBicicleta")).get();
        } catch (NullPointerException e) {
            return "Você está tentando encontrar uma bicicleta que não existe.";
        }

        if (bicicleta.getEstadoAtual() == EM_USO) {
            return "Você está tentando alugar uma bicicleta que já está em uso, favor alugar uma bicicleta disponível";
        }

        Vaga vaga;
        try {
            vaga = vagaRepo.findByBicicleta(bicicleta.getIdBicicleta());
        } catch (NullPointerException e) {
            return "Você está tentando encontrar uma vaga que não existe.";
        }

        vaga.alteraDisponibilidadeVaga(vaga);
        bicicleta.setEstadoAtual(EM_USO);

        Usuario usuario;
        try {
            usuario = usuarioRepo.findById(json.get("idUsuario")).get();
        }
        catch (NullPointerException e) {
            return "Você está tentando encontrar um usuário que não existe.";
        }

        aluguel.setUsuarioAlugado(usuario);
        aluguel.setBicicletaAlugada(bicicleta);
        bicicletaRepo.save(bicicleta);
        vagaRepo.save(vaga);
        aluguelRepo.save(aluguel);
        return "Bicicleta: " + bicicleta.getIdBicicleta() + " alugada com sucesso";
    }

    @GetMapping(path = "/aluguel/altstatbike/{idBicicleta}")
    public String alteraEstadoBicicleta(@PathVariable("idBicicleta") Integer idBicicleta) {
        Bicicleta bicicleta = bicicletaRepo.findById(idBicicleta).get();
        if (bicicleta.getEstadoAtual().equals(NA_VAGA)) {
            bicicleta.setEstadoAtual(EM_USO);
        } else {
            bicicleta.setEstadoAtual(NA_VAGA);
        }
        bicicletaRepo.save(bicicleta);
        return "Estado da Bicicleta: " + bicicleta.getIdBicicleta() + " atualizado para: " + bicicleta.getEstadoAtual();
    }
}
