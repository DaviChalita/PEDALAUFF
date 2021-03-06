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

    @CrossOrigin
    @PostMapping(path = "/aluguel/alugar")
    public String alugar(@RequestBody Map<String, String> json) {
        if (userIdent != null) {
            Aluguel aluguel = new Aluguel();
            aluguel.setDthrAluguel(new Date(System.currentTimeMillis()));

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
            vaga.setBicicleta(null);
            bicicleta.setEstadoAtual(EM_USO);

            Usuario usuario;
            try {
                System.out.println("User ident: " + userIdent);
                usuario = usuarioRepo.findById(Integer.valueOf(userIdent)).get();
                System.out.println("Usuario: " + usuario.getNome());
                try {
                    int idAluguel = usuarioRepo.checkBicicletaNDevolvida(usuario.getIdUsuario());
                    return "Usuário precisa devolver uma bicicleta antes de alugar outra!";
                } catch (NullPointerException | AopInvocationException e) {
                    System.out.println("Usuário não está alugando nenhuma bicicleta no momento");
                }

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
        return LOGAR_NO_SITE;
    }

    @CrossOrigin
    @PostMapping(path = "/aluguel/devolver")
    public String devolver(@RequestBody Map<String, String> json) {
        if (userIdent != null) {
            Aluguel aluguel;
            Bicicleta bicicleta;
            Integer idAluguel;

            try {
                idAluguel = aluguelRepo.findByIdUsuarioAndBikeNDevolvida(Integer.parseInt(userIdent));
                if(idAluguel == null){
                    return "Não foi possível encontrar o aluguel atual do usuário logado";
                }
                aluguel = aluguelRepo.findById(idAluguel).get();
                aluguel.setDthrDevolucao(new Date(System.currentTimeMillis()));
            } catch (NullPointerException | IllegalArgumentException e) {
                return "Erro ao buscar o aluguel";
            }

            bicicleta = bicicletaRepo.findById(aluguelRepo.findIdBikeByIdAluguel(idAluguel)).get();
            bicicleta.setEstadoAtual(NA_VAGA);

            Vaga vaga;

                try {
                    vaga = vagaRepo.findByQrCode(json.get("qrCodeVaga"));
                    vaga.alteraDisponibilidadeVaga(vaga);
                    vaga.setBicicleta(bicicleta);
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
