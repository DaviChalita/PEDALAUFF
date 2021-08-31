package com.uff.pedalauff.controlador;
import com.uff.pedalauff.modelo.*;
import com.uff.pedalauff.repo.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static com.uff.pedalauff.enums.EstadoBicicleta.EM_USO;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@WebMvcTest //Para está classe rodar em um contexto
public class AluguelControladorTest {


    String texto = "Você está tentando alugar uma bicicleta que já está em uso, favor alugar uma bicicleta disponível";

    @Autowired
    private AluguelControlador aluguelControlador;

    @MockBean
    private AluguelRepo aluguelRepo;

    @MockBean
    private BicicletaRepo bicicletaRepo;

    @MockBean
    private UsuarioRepo usuarioRepo;

    @MockBean
    private VagaRepo vagaRepo;

    @MockBean
    private PostoRepo postoRepo;

    @BeforeEach
    //Adiciona no contexto somente o AluguelControladorTest
    //ignora os outros controladores
    public void setup(){
        standaloneSetup(this.aluguelControlador);
    }

    @Test
    public void deveRetornarErro_quandoAlugarBicicletaJaAlugada(){


    }
}
