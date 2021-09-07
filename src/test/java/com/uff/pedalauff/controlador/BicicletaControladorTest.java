package com.uff.pedalauff.controlador;

import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.modelo.Vaga;
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

import static com.uff.pedalauff.enums.EstadoBicicleta.EM_USO;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(BicicletaControlador.class)
public class BicicletaControladorTest {

    @Mock
    private AluguelRepo aluguelRepo;
    @Mock
    private BicicletaRepo bicicletaRepo;
    @Mock
    private UsuarioRepo usuarioRepo;
    @Mock
    private VagaRepo vagaRepo;

    @MockBean
    Bicicleta bike;

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
    private BicicletaControlador controller;

    @Test
    public void consultarBicicletaSemLogin_falha() {

        Mockito.doReturn(null).when(controller).userId();
        Mockito.doReturn(null).when(controller).userLvl();

        Map<String, String> bicicletaMap = new HashMap<>();
        bicicletaMap.put("qrCodeBicicleta", new String("en2r"));

        assertThat(controller.consultar(bicicletaMap))
                .isEqualTo("Você não possui acesso para " +
                        "realizar tal ação.");
    }

    @Test
    public void consultarBicicletaIdInvalido_falha() {

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(null).when(bicicletaRepo).findByQrCode("en2r");

        Map<String, String> bicicletaMap = new HashMap<>();
        bicicletaMap.put("qrCodeBicicleta", new String("en2r"));

        assertThat(controller.consultar(bicicletaMap))
                .isEqualTo("Favor inserir um id de bicicleta válido");
    }

    @Test
    public void consultarBicicleta_sucesso() {

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setQrCode("en2r");
        bicicleta.setEstadoAtual(EM_USO);

        Vaga vaga = new Vaga();
        vaga.setQrCode("0zm7");

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(bicicleta).when(bicicletaRepo).findByQrCode("en2r");
        Mockito.doReturn(java.util.Optional.ofNullable(vaga)).when(vagaRepo).findById(1);

        Map<String, String> bicicletaMap = new HashMap<>();
        bicicletaMap.put("qrCodeBicicleta", new String("en2r"));

        assertThat(controller.consultar(bicicletaMap))
                .isEqualTo("QRCode da bicicleta buscada: en2r\n" +
                        "Estado da bicicleta: EM_USO\n" +
                        "E se encontra na vaga de QRCode: 0zm7");
    }

    @Test
    public void salvarBicicleta_sucesso() {

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setQrCode("en2r");
        bicicleta.setEstadoAtual(EM_USO);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(bicicleta).when(bicicletaRepo).findByQrCode("en2r");
        Mockito.doReturn("abcd").when(controller).geraQrCodeAleatorio();

        Map<String, String> bicicletaMap = new HashMap<>();
        bicicletaMap.put("qrCodeBicicleta", new String("en2r"));

        assertThat(controller.salvar(bicicleta))
                .isEqualTo("O QR Code da bicicleta criada é igual a: abcd");
    }

    @Test
    public void consertarBicicletaInexistente_falha() {

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setQrCode("en2r");
        bicicleta.setEstadoAtual(EM_USO);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(null).when(bicicletaRepo).findByQrCode("en2r");

        Map<String, String> bicicletaMap = new HashMap<>();
        bicicletaMap.put("qrCodeBicicleta", new String("en2r"));

        assertThat(controller.consertarBicicleta(bicicletaMap))
                .isEqualTo("Favor inserir o QRCode de uma bicicleta que existe");
    }

    @Test
    public void consertarBicicletaAlugada_falha() {

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setQrCode("en2r");
        bicicleta.setEstadoAtual(EM_USO);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(bicicleta).when(bicicletaRepo)
                .findByQrCode("en2r");
        Mockito.doReturn(1).when(vagaRepo)
                .findByBicicletaQrCode("en2r");
        Mockito.doReturn(null).when(vagaRepo)
                .findById(1);

        Map<String, String> bicicletaMap = new HashMap<>();
        bicicletaMap.put("qrCodeBicicleta", new String("en2r"));

        assertThat(controller.consertarBicicleta(bicicletaMap))
                .isEqualTo("Bicicleta não está em nenhuma vaga, " +
                        "favor devolvê-la a uma vaga antes de consertá-la");
    }

    @Test
    public void consertarBicicleta_sucesso() {

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setQrCode("en2r");
        bicicleta.setEstadoAtual(EM_USO);

        Vaga vaga = new Vaga();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(bicicleta).when(bicicletaRepo)
                .findByQrCode("en2r");
        Mockito.doReturn(1).when(vagaRepo)
                .findByBicicletaQrCode("en2r");
        Mockito.doReturn(java.util.Optional.ofNullable(vaga)).when(vagaRepo)
                .findById(1);

        Map<String, String> bicicletaMap = new HashMap<>();
        bicicletaMap.put("qrCodeBicicleta", new String("en2r"));

        assertThat(controller.consertarBicicleta(bicicletaMap))
                .isEqualTo("Status da bicicleta atualizado com sucesso.");
    }

}
