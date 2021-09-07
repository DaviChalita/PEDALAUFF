package com.uff.pedalauff.controlador;


import com.uff.pedalauff.modelo.Bicicleta;
import com.uff.pedalauff.modelo.Posto;
import com.uff.pedalauff.modelo.Vaga;
import com.uff.pedalauff.repo.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static com.uff.pedalauff.enums.EstadoBicicleta.EM_USO;
import static com.uff.pedalauff.enums.EstadoBicicleta.NA_VAGA;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(VagaControlador.class)
public class VagaControladorTest {

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
    private VagaControlador controller;

    @Test
    public void verTodasVagasSemLogin_falha(){

        Mockito.doReturn(null).when(controller).userId();
        Mockito.doReturn(null).when(controller).userLvl();

        assertThat(controller.verTodasVagas())
                .isEqualTo("Você não possui acesso para " +
                        "realizar tal ação.");
    }

    @Test
    public void verTodasVagas_sucesso(){
        Posto posto1 = new Posto();
        posto1.setIdPosto(1);
        posto1.setCampus("Praia Vermelha");
        posto1.setEndereco("rua x");

        Posto posto2 = new Posto();
        posto1.setIdPosto(2);
        posto1.setCampus("Gragoata");
        posto1.setEndereco("rua y");

        Bicicleta bike = new Bicicleta();
        bike.setQrCode("bk123");

        Vaga vaga1 = new Vaga();
        vaga1.setIdVaga(1);
        vaga1.setQrCode("abc123");
        vaga1.setDisponibilidade(true);
        vaga1.setPosto(posto1);
        vaga1.setBicicleta(bike);

        Vaga vaga2 = new Vaga();
        vaga2.setIdVaga(2);
        vaga2.setQrCode("def123");
        vaga2.setDisponibilidade(true);
        vaga2.setPosto(posto2);

        List<Vaga> vagas = new ArrayList<>();
        vagas.add(vaga1);
        vagas.add(vaga2);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(vagas).when(vagaRepo).findAll();

        assertThat(controller.verTodasVagas())
                .isEqualTo("Lista de Vagas:\n" +
                        "Vaga: 1\n" +
                        "QRCode da vaga: abc123\n" +
                        "Endereço e campus do posto aonde a vaga se localiza: \n" +
                        "rua y, Gragoata\n" +
                        "QRCode da bicicleta nessa vaga: bk123\n" +
                        "\n" +
                        "Vaga: 2\n" +
                        "QRCode da vaga: def123\n" +
                        "Endereço e campus do posto aonde a vaga se localiza: \n" +
                        "null, null\n" +
                        "Não há bicicleta nessa vaga\n" +
                        "\n");
    }

    @Test
    public void salvarVagaSemLogin_falha(){

        Mockito.doReturn(null).when(controller).userId();
        Mockito.doReturn(null).when(controller).userLvl();

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "idPosto", new String( "1" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.salvar(vagaMap))
                .isEqualTo("Você não possui acesso para " +
                        "realizar tal ação.");
    }

    @Test
    public void salvarVagaSemQrCode_falha(){
        NullPointerException e = new NullPointerException();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doThrow(e).when(controller).geraQrCodeAleatorio();

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "idPosto", new String( "1" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.salvar(vagaMap))
                .isEqualTo("Vaga está sendo criada sem Qr Code");
    }

    @Test
    public void salvarVagaSemVaga_falha(){

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "idPosto", new String( "1" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.salvar(vagaMap))
                .isEqualTo("Posto inserido não existe");
    }

    @Test
    public void salvarVagaComPostoInexistente_falha(){
        NoSuchElementException e = new NoSuchElementException();
        Posto posto = new Posto();
        posto.setIdPosto(1);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();

        Mockito.doThrow(e).when(postoRepo).findById(1);

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "idPosto", new String( "1" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.salvar(vagaMap))
                .isEqualTo("Posto inserido não existe");
    }

    @Test
    public void salvarVagaComBicicletaIvalida_falha(){
        Posto posto = new Posto();
        posto.setIdPosto(1);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(java.util.Optional.ofNullable(posto))
                .when(postoRepo).findById(1);
        Mockito.doReturn(null)
                .when(bicicletaRepo).findByQrCode("abc123");

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "idPosto", new String( "1" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.salvar(vagaMap))
                .isEqualTo("QRCode da bicicleta inserido é inválido");
    }

    @Test
    public void salvarVagaComBicicletaInexistente_falha(){
        NoSuchElementException e = new NoSuchElementException();

        Posto posto = new Posto();
        posto.setIdPosto(1);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(java.util.Optional.ofNullable(posto))
                .when(postoRepo).findById(1);
        Mockito.doThrow(e)
                .when(bicicletaRepo).findByQrCode("abc123");

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "idPosto", new String( "1" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.salvar(vagaMap))
                .isEqualTo("Bicicleta inserida não existe");
    }

    @Test
    public void salvarVaga_sucesso(){
        Posto posto = new Posto();
        posto.setIdPosto(1);
        Bicicleta bicicleta = new Bicicleta();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn("test123").when(controller).geraQrCodeAleatorio();
        Mockito.doReturn(java.util.Optional.ofNullable(posto))
                .when(postoRepo).findById(1);
        Mockito.doReturn(bicicleta)
                .when(bicicletaRepo).findByQrCode("abc123");

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "idPosto", new String( "1" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.salvar(vagaMap))
                .isEqualTo("Vaga com o QRCode: test123 criada com sucesso");
    }

    @Test
    public void inserirBikeNaVagaSemLogin_falha(){

        Mockito.doReturn(null).when(controller).userId();
        Mockito.doReturn(null).when(controller).userLvl();

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "qrCodeVaga", new String( "vaga123" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.insereBikeNaVaga(vagaMap))
                .isEqualTo("Você não possui acesso para " +
                        "realizar tal ação.");
    }

    @Test
    public void inserirBikeNaVagaIvalida_falha(){
        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(null).when(vagaRepo).findByQrCode("vaga123");

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "qrCodeVaga", new String( "vaga123" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.insereBikeNaVaga(vagaMap))
                .isEqualTo("Favor inserir um QRCode de uma vaga válida");
    }

    @Test
    public void inserirBikeNaVagaOcupada_falha(){
        Vaga vaga = new Vaga();
        vaga.setDisponibilidade(false);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(vaga).when(vagaRepo).findByQrCode("vaga123");

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "qrCodeVaga", new String( "vaga123" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.insereBikeNaVaga(vagaMap))
                .isEqualTo("Vaga já possui uma bicicleta");
    }

    @Test
    public void inserirBikeNaVagaSemQrCodeDaVaga_falha(){
        Vaga vaga = new Vaga();
        vaga.setDisponibilidade(false);
        NumberFormatException e = new NumberFormatException();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doThrow(e).when(vagaRepo).findByQrCode("vaga123");

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "qrCodeVaga", new String( "vaga123" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.insereBikeNaVaga(vagaMap))
                .isEqualTo("Favor inserir o QRCode da vaga");
    }

    @Test
    public void inserirBikeInvalidaNaVaga_falha(){
        Vaga vaga = new Vaga();
        vaga.setDisponibilidade(true);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(vaga).when(vagaRepo).findByQrCode("vaga123");
        Mockito.doReturn(null).when(bicicletaRepo).findByQrCode("abc123");

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "qrCodeVaga", new String( "vaga123" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.insereBikeNaVaga(vagaMap))
                .isEqualTo("Favor inserir um QRCode de uma bicicleta válida");
    }

    @Test
    public void inserirBikeQueJaTaNaVaga_falha(){
        Vaga vaga = new Vaga();
        vaga.setDisponibilidade(true);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setEstadoAtual(NA_VAGA);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(vaga).when(vagaRepo).findByQrCode("vaga123");
        Mockito.doReturn(bicicleta).when(bicicletaRepo).findByQrCode("abc123");

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "qrCodeVaga", new String( "vaga123" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.insereBikeNaVaga(vagaMap))
                .isEqualTo("Bicicleta buscada já está em uma vaga");
    }

    @Test
    public void inserirBikeSemQrCode_falha(){
        NumberFormatException e = new NumberFormatException();
        Vaga vaga = new Vaga();
        vaga.setDisponibilidade(true);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setEstadoAtual(NA_VAGA);

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(vaga).when(vagaRepo).findByQrCode("vaga123");
        Mockito.doThrow(e).when(bicicletaRepo).findByQrCode("abc123");

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "qrCodeVaga", new String( "vaga123" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.insereBikeNaVaga(vagaMap))
                .isEqualTo("Você está tentando inserir uma bicicleta " +
                        "na vaga, especifique o QRCode da bicicleta");
    }

    @Test
    public void inserirBike_sucesso(){
        Vaga vaga = new Vaga();
        vaga.setDisponibilidade(true);

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setEstadoAtual(EM_USO);
        bicicleta.setQrCode("abc123");

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        Mockito.doReturn(vaga).when(vagaRepo).findByQrCode("vaga123");
        Mockito.doReturn(bicicleta).when(bicicletaRepo).findByQrCode("abc123");

        Map<String,String> vagaMap = new HashMap<>();
        vagaMap.put( "qrCodeVaga", new String( "vaga123" ));
        vagaMap.put( "qrCodeBicicleta", new String( "abc123" ));

        assertThat(controller.insereBikeNaVaga(vagaMap))
                .isEqualTo("Bicicleta: abc123 inserida na vaga com sucesso");
    }
}
