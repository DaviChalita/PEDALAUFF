package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Posto;
import com.uff.pedalauff.repo.AluguelRepo;
import com.uff.pedalauff.repo.BicicletaRepo;
import com.uff.pedalauff.repo.UsuarioRepo;
import com.uff.pedalauff.repo.VagaRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
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

    @MockBean
    AluguelRepo aluguelRepoMock;

    @MockBean
    BicicletaRepo bicicletaRepoMock;

    @MockBean
    UsuarioRepo usuarioRepoMock;

    @MockBean
    VagaRepo vagaRepoMock;

    @InjectMocks
    @Spy
    private PostoControlador controller;

    @Test
    public void consultarBicicletaSemLogin_falha(){

        Posto posto = new Posto();
        Mockito.doReturn(3).when(controller).userId();
        //Mockito.doReturn(null).when(controller).userLvl();

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.salvar(posto))
                .isEqualTo("Você não possui acesso para " +
                        "realizar tal ação.");
    }
}
