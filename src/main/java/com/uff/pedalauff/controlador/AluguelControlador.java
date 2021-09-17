package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Aluguel;
import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.modelo.Vaga;
import com.uff.pedalauff.repo.AluguelRepo;
import com.uff.pedalauff.repo.BicicletaRepo;
import com.uff.pedalauff.repo.UsuarioRepo;
import com.uff.pedalauff.repo.VagaRepo;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.uff.pedalauff.consts.PedalaUffConstants.LOGAR_NO_SITE;
import static com.uff.pedalauff.controlador.UsuarioControlador.userIdent;
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

    public String userId(){ return userIdent;}
    public String userLvl(){ return userIdent;}

    @CrossOrigin
    @PostMapping(path = "/aluguel/alugar")
    public String alugar(@RequestBody Map<String, String> json) {
        userIdent = userId();
        if (userIdent != null) {
            Integer idAluguel;
            Aluguel aluguel = new Aluguel();
            aluguel.setDthrAluguel(new Date(System.currentTimeMillis()));
            if (aluguel.getDthrAluguel() == null){
                return "Falha ao setar a Data/Hora do aluguel. Tente novamente";
            }

            Bicicleta bicicleta;
            try {
                bicicleta = bicicletaRepo.findByQrCode(json.get("qrCodeBicicleta"));
                if (bicicleta.getEstadoAtual() == EM_USO) {
                    return "Você está tentando alugar uma bicicleta que já está em uso, favor alugar uma bicicleta disponível";
                }
            } catch (NullPointerException | NumberFormatException ex) {
                return "Você está tentando encontrar uma bicicleta que não existe.";
            }

            Vaga vaga;
            try {
                Integer idVaga = vagaRepo.findByBicicleta(bicicleta.getIdBicicleta());
                vaga = vagaRepo.findById(idVaga).get();
            } catch (NullPointerException e) {
                return "Você está tentando encontrar uma vaga que não existe.";
            }

            vaga.alteraDisponibilidadeVaga(vaga);
            if (vaga.get_disponibilidade() == false){
                return "Falha ao retirar bicicleta da vaga. Tente novamente";
            }

            vaga.setBicicleta(null);
            if (vaga.getBicicleta() != null){
                return "Falha ao retirar bicicleta da vaga. Tente novamente";
            }

            bicicleta.setEstadoAtual(EM_USO);
            if (bicicleta.getEstadoAtual() != EM_USO){
                return "Falha ao retirar bicicleta da vaga. Tente novamente";
            }

            Usuario usuario;
            try {
                usuario = usuarioRepo.findById(Integer.valueOf(userIdent)).get();
                try {
                    Integer id = usuario.getIdUsuario();
                    idAluguel = usuarioRepo.checkBicicletaNDevolvida(id);
                    if (idAluguel != null){
                        return "Usuário precisa devolver uma bicicleta antes de alugar outra!";
                    }
                } catch (NullPointerException | AopInvocationException e) {
                    System.out.println("Usuário não está alugando nenhuma bicicleta no momento");
                }

            } catch (NullPointerException e) {
                return "Você está tentando encontrar um usuário que não existe.";
            }

            aluguel.setUsuarioAlugado(usuario);
            if (aluguel.getUsuarioAlugado() == null){
                return "Falha ao retirar bicicleta da vaga. Tente novamente";
            }
            aluguel.setBicicletaAlugada(bicicleta);
            bicicletaRepo.save(bicicleta);
            vagaRepo.save(vaga);
            aluguelRepo.save(aluguel);
            return "Bicicleta: " + bicicleta.getIdBicicleta() + " alugada com sucesso";
        }
        return LOGAR_NO_SITE;
    }

    @CrossOrigin
    @PostMapping(path = "/aluguel/devolver")
    public String devolver(@RequestBody Map<String, String> json) {
        userIdent = userId();
        if (userIdent != null) {
            Aluguel aluguel;
            Bicicleta bicicleta;
            Integer idAluguel;

            try {
                Integer id = Integer.parseInt(userIdent);
                idAluguel = aluguelRepo.findByIdUsuarioAndBikeNDevolvida(id);
                if(idAluguel == null){
                    return "Não foi possível encontrar o aluguel atual do usuário logado";
                }
                aluguel = aluguelRepo.findById(idAluguel).get();
                aluguel.setDthrDevolucao(new Date(System.currentTimeMillis()));
                if (aluguel.getDthrDevolucao() == null){
                    return "Falha ao setar a Data/Hora da devolução. Tente novamente";
                }
            } catch (NullPointerException | IllegalArgumentException e) {
                return "Erro ao buscar o aluguel";
            }

            bicicleta = bicicletaRepo.findById(aluguelRepo.findIdBikeByIdAluguel(idAluguel)).get();
            bicicleta.setEstadoAtual(NA_VAGA);
            if (bicicleta.getEstadoAtual() != NA_VAGA){
                return "Falha ao devolver bicicleta na vaga. Tente novamente";
            }

            Vaga vaga;

                try {
                    String qrCode = json.get("qrCodeVaga");
                    vaga = vagaRepo.findByQrCode(qrCode);
                    vaga.alteraDisponibilidadeVaga(vaga);
                    if (vaga.get_disponibilidade() == false){
                        return "Falha ao devolver bicicleta na vaga. Tente novamente";
                    }
                    vaga.setBicicleta(bicicleta);
                    if (vaga.getBicicleta() == null){
                        return "Falha ao devolver bicicleta na vaga. Tente novamente";
                    }
                    vagaRepo.save(vaga);
                } catch (NullPointerException | NumberFormatException ex) {
                    return "Você está tentando encontrar uma vaga que não existe.";

            }


            bicicletaRepo.save(bicicleta);
            aluguelRepo.save(aluguel);
            return "Bicicleta: " + bicicleta.getIdBicicleta() + " devolvida com sucesso";

        }
        return LOGAR_NO_SITE;
    }

}
