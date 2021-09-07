package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Posto;
import com.uff.pedalauff.repo.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(PostoControlador.class)
public class PostoControladorTest {

    @Mock
    private AluguelRepo aluguelRepo;
    @Mock
    private BicicletaRepo bicicletaRepo;
    @Mock
    private UsuarioRepo usuarioRepo;
    @Mock
    private VagaRepo vagaRepo;
    @Mock
    private PostoRepo postoRepo;

    @MockBean
    AluguelRepo aluguelRepoMock;

    @MockBean
    BicicletaRepo bicicletaRepoMock;

    @MockBean
    UsuarioRepo usuarioRepoMock;

    @MockBean
    VagaRepo vagaRepoMock;

    @MockBean
    PostoRepo postoRepoMock;

    @InjectMocks
    @Spy
    private PostoControlador controller;

    @Test
    public void salvarPostoSemLogin_falha(){

        Posto posto = new Posto();
        Mockito.doReturn(null).when(controller).userId();
        Mockito.doReturn(null).when(controller).userLvl();

        assertThat(controller.salvar(posto))
                .isEqualTo("Você não possui acesso para " +
                        "realizar tal ação.");
    }

    @Test
    public void salvarPosto_sucesso(){

        Posto posto = new Posto();
        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();

        assertThat(controller.salvar(posto))
                .isEqualTo("Posto criado com sucesso.");
    }

    @Test
    public void consultarPostoSemLogin_falha(){

        Posto posto = new Posto();
        Mockito.doReturn(null).when(controller).userId();

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.consultar(bicicletaMap))
                .isEqualTo("Você não possui acesso para " +
                        "realizar tal ação.");
    }

    @Test
    public void consultarPostoInexistente_falha(){

        Posto posto = new Posto();
        posto.setIdPosto(1);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(null).when(postoRepo).findById(1);

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.consultar(bicicletaMap))
                .isEqualTo("Posto buscado não existe");
    }

    @Test
    public void consultarPostoSucesso(){

        Posto posto = new Posto();
        posto.setIdPosto(1);

        List<Boolean> bicicletasDisp = new ArrayList<>();
        bicicletasDisp.add(true);
        bicicletasDisp.add(false);
        bicicletasDisp.add(true);
        bicicletasDisp.add(true);

        List<Boolean> vagasDisp = new ArrayList<>();
        vagasDisp.add(true);
        vagasDisp.add(false);
        vagasDisp.add(true);
        vagasDisp.add(true);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(java.util.Optional.ofNullable(posto))
                .when(postoRepo).findById(1);
        Mockito.doReturn(bicicletasDisp)
                .when(vagaRepo).qtdBicicletasDisp(1);
        Mockito.doReturn(vagasDisp)
                .when(vagaRepo).qtdVagasDisp(1);


        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "idPosto", new String( "1" ));

        assertThat(controller.consultar(bicicletaMap))
                .isEqualTo("Posto: 1 tem 3 bicicleta(s) disponivel(is) " +
                        "para aluguel e 3 vaga(s) disponível(is).");
    }
}
