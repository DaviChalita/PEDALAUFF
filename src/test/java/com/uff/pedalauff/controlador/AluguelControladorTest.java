package com.uff.pedalauff.controlador;
import com.uff.pedalauff.modelo.*;
import com.uff.pedalauff.repo.*;
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
import static com.uff.pedalauff.enums.EstadoBicicleta.NA_VAGA;
import static com.uff.pedalauff.enums.TipoUsuario.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(AluguelControlador.class)
public class AluguelControladorTest {


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
    private AluguelControlador controller;

    @Test
    public void alugarBicicletaJaAlugada_falha(){

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setEstadoAtual(EM_USO);
        bicicleta.setQrCode("en2r");

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(bicicleta).when(bicicletaRepo)
                .findByQrCode("en2r");

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.alugar(bicicletaMap))
                .isEqualTo("Você está tentando alugar uma bicicleta que já está em uso," +
                        " favor alugar uma bicicleta disponível");
    }

    @Test
    public void alugarBicicletaInexistente_falha(){

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setEstadoAtual(NA_VAGA);
        bicicleta.setQrCode("en2r");

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(null).when(bicicletaRepo)
                .findByQrCode("en2r");

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.alugar(bicicletaMap))
                .isEqualTo("Você está tentando encontrar uma bicicleta que não existe.");

    }

    @Test
    public void buscarVagaInexistenteAluguel_falha(){

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setEstadoAtual(NA_VAGA);
        bicicleta.setQrCode("en2r");

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(bicicleta).when(bicicletaRepo)
                .findByQrCode("en2r");
        Mockito.doReturn(12).when(vagaRepo)
                .findByBicicleta(1);
        Mockito.doReturn(null).when(vagaRepo)
                .findById(12);

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.alugar(bicicletaMap))
                .isEqualTo("Você está tentando encontrar uma vaga que não existe.");
    }


    @Test
    public void usuarioJaEstaAlugandoBicicleta_falha(){

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setEstadoAtual(NA_VAGA);
        bicicleta.setQrCode("en2r");

        Vaga vaga = new Vaga();

        Usuario usuario = Usuario.builder()
                .idUsuario(2)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(bicicleta).when(bicicletaRepo)
                .findByQrCode("en2r");
        Mockito.doReturn(1).when(vagaRepo)
                .findByBicicleta(1);
        Mockito.doReturn(java.util.Optional.ofNullable(vaga)).when(vagaRepo)
                .findById(1);
        Mockito.doReturn(java.util.Optional.ofNullable(usuario))
                .when(usuarioRepo).findById(3);
        Mockito.doReturn(1)
                .when(usuarioRepo).checkBicicletaNDevolvida(3);


        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.alugar(bicicletaMap))
                .isEqualTo("Usuário precisa devolver uma bicicleta antes de alugar outra!");
    }

    @Test
    public void usuarioInexistente_falha(){

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setEstadoAtual(NA_VAGA);
        bicicleta.setQrCode("en2r");

        Vaga vaga = new Vaga();

        Usuario usuario = Usuario.builder()
                .idUsuario(2)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(bicicleta).when(bicicletaRepo)
                .findByQrCode("en2r");
        Mockito.doReturn(1).when(vagaRepo)
                .findByBicicleta(1);
        Mockito.doReturn(java.util.Optional.ofNullable(vaga)).when(vagaRepo)
                .findById(1);
        Mockito.doReturn(null)
                .when(usuarioRepo).findById(3);

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.alugar(bicicletaMap))
                .isEqualTo("Você está tentando encontrar um usuário que não existe.");
    }

    @Test
    public void alugarBicicleta_sucesso(){

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setEstadoAtual(NA_VAGA);
        bicicleta.setQrCode("en2r");

        Vaga vaga = new Vaga();

        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(bicicleta).when(bicicletaRepo)
                .findByQrCode("en2r");
        Mockito.doReturn(1).when(vagaRepo)
                .findByBicicleta(1);
        Mockito.doReturn(java.util.Optional.ofNullable(vaga)).when(vagaRepo)
                .findById(1);
        Mockito.doReturn(java.util.Optional.ofNullable(usuario))
                .when(usuarioRepo).findById(3);
        Mockito.doReturn(null)
                .when(usuarioRepo).checkBicicletaNDevolvida(3);
        //Mockito.doThrow(new Exception("Testing")).when(usuarioRepo).checkBicicletaNDevolvida(3);

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.alugar(bicicletaMap))
                .isEqualTo("Bicicleta: 1 alugada com sucesso");
    }

    @Test
    public void devolverSemALugar_falha(){

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(null).when(aluguelRepo)
                .findByIdUsuarioAndBikeNDevolvida(3);

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.devolver(bicicletaMap))
                .isEqualTo("Não foi possível encontrar o aluguel" +
                        " atual do usuário logado");
    }

    @Test
    public void bucarAluguel_falha(){
        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(3).when(aluguelRepo)
                .findByIdUsuarioAndBikeNDevolvida(3);
        Mockito.doReturn(null).when(aluguelRepo)
                .findById(3);

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.devolver(bicicletaMap))
                .isEqualTo("Erro ao buscar o aluguel");
    }

    @Test
    public void buscarVagaInexistenteDevolver_falha(){

        Bicicleta bicicleta = new Bicicleta();

        Usuario usuario = new Usuario();

        Aluguel aluguel = new Aluguel();
        aluguel.setIdAluguel(1);
        aluguel.setBicicletaAlugada(bicicleta);
        aluguel.setUsuarioAlugado(usuario);

        Vaga vaga = new Vaga();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(1).when(aluguelRepo)
                .findByIdUsuarioAndBikeNDevolvida(3);
        Mockito.doReturn(java.util.Optional.ofNullable(aluguel))
                .when(aluguelRepo).findById(1);
        Mockito.doReturn(1).when(aluguelRepo)
                .findIdBikeByIdAluguel(1);
        Mockito.doReturn(java.util.Optional.ofNullable(bicicleta)).when(bicicletaRepo)
                .findById(1);
        Mockito.doReturn(vaga).when(vagaRepo)
                .findByQrCode("en");

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.devolver(bicicletaMap))
                .isEqualTo("Você está tentando encontrar " +
                        "uma vaga que não existe.");
    }

    @Test
    public void devolverBicicleta_sucesso(){

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);

        Usuario usuario = new Usuario();

        Aluguel aluguel = new Aluguel();
        aluguel.setIdAluguel(1);
        aluguel.setBicicletaAlugada(bicicleta);
        aluguel.setUsuarioAlugado(usuario);

        Vaga vaga = new Vaga();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(1).when(aluguelRepo)
                .findByIdUsuarioAndBikeNDevolvida(3);
        Mockito.doReturn(java.util.Optional.ofNullable(aluguel))
                .when(aluguelRepo).findById(1);
        Mockito.doReturn(1).when(aluguelRepo)
                .findIdBikeByIdAluguel(1);
        Mockito.doReturn(java.util.Optional.ofNullable(bicicleta)).when(bicicletaRepo)
                .findById(1);
        Mockito.doReturn(vaga).when(vagaRepo)
                .findByQrCode("en2r");

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeVaga", new String( "en2r" ));

        assertThat(controller.devolver(bicicletaMap))
                .isEqualTo("Bicicleta: 1 devolvida com sucesso");
    }

    @Test
    public void alugarOuDevolverSemLogin(){
        Mockito.doReturn(null).when(controller).userId();


        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.devolver(bicicletaMap))
                .isEqualTo("Você não possui acesso para " +
                        "realizar tal ação.");
    }

    @Test
    //Teste que só funciona em mutação
    public void setarDataHoraAluguel_falha(){
        Mockito.doReturn("3").when(controller).userId();

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.alugar(bicicletaMap))
                .isEqualTo("Falha ao setar a Data/Hora do " +
                        "aluguel. Tente novamente");
    }

    @Test
    //Teste que só funciona em mutação
    public void retirarBikeVaga_falha(){
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);
        bicicleta.setEstadoAtual(NA_VAGA);
        bicicleta.setQrCode("en2r");

        Vaga vaga = new Vaga();
        vaga.setBicicleta(bicicleta);

        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(bicicleta).when(bicicletaRepo)
                .findByQrCode("en2r");
        Mockito.doReturn(1).when(vagaRepo)
                .findByBicicleta(1);
        Mockito.doReturn(java.util.Optional.ofNullable(vaga)).when(vagaRepo)
                .findById(1);
        Mockito.doReturn(java.util.Optional.ofNullable(usuario))
                .when(usuarioRepo).findById(3);
        Mockito.doReturn(null)
                .when(usuarioRepo).checkBicicletaNDevolvida(3);
        //Mockito.doThrow(new Exception("Testing")).when(usuarioRepo).checkBicicletaNDevolvida(3);

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.alugar(bicicletaMap))
                .isEqualTo("Falha ao retirar bicicleta da " +
                        "vaga. Tente novamente");
    }

    @Test
    //Teste que só funciona em mutação
    public void setarDataHoraDevolucao_falha(){
        Bicicleta bicicleta = new Bicicleta();

        Usuario usuario = new Usuario();

        Aluguel aluguel = new Aluguel();
        aluguel.setIdAluguel(1);
        aluguel.setBicicletaAlugada(bicicleta);
        aluguel.setUsuarioAlugado(usuario);

        Vaga vaga = new Vaga();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(1).when(aluguelRepo)
                .findByIdUsuarioAndBikeNDevolvida(3);
        Mockito.doReturn(java.util.Optional.ofNullable(aluguel))
                .when(aluguelRepo).findById(1);
        Mockito.doReturn(1).when(aluguelRepo)
                .findIdBikeByIdAluguel(1);
        Mockito.doReturn(java.util.Optional.ofNullable(bicicleta)).when(bicicletaRepo)
                .findById(1);
        Mockito.doReturn(vaga).when(vagaRepo)
                .findByQrCode("en");

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeBicicleta", new String( "en2r" ));

        assertThat(controller.devolver(bicicletaMap))
                .isEqualTo("Falha ao setar a Data/Hora da " +
                        "devolução. Tente novamente");
    }

    @Test
    //Teste que só funciona em mutação
    public void devolverBikeVaga_falha(){
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setIdBicicleta(1);

        Usuario usuario = new Usuario();

        Aluguel aluguel = new Aluguel();
        aluguel.setIdAluguel(1);
        aluguel.setBicicletaAlugada(bicicleta);
        aluguel.setUsuarioAlugado(usuario);

        Vaga vaga = new Vaga();

        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn(1).when(aluguelRepo)
                .findByIdUsuarioAndBikeNDevolvida(3);
        Mockito.doReturn(java.util.Optional.ofNullable(aluguel))
                .when(aluguelRepo).findById(1);
        Mockito.doReturn(1).when(aluguelRepo)
                .findIdBikeByIdAluguel(1);
        Mockito.doReturn(java.util.Optional.ofNullable(bicicleta)).when(bicicletaRepo)
                .findById(1);
        Mockito.doReturn(vaga).when(vagaRepo)
                .findByQrCode("en2r");

        Map<String,String> bicicletaMap = new HashMap<>();
        bicicletaMap.put( "qrCodeVaga", new String( "en2r" ));

        assertThat(controller.devolver(bicicletaMap))
                .isEqualTo("Falha ao devolver bicicleta na " +
                        "vaga. Tente novamente");
    }

}
