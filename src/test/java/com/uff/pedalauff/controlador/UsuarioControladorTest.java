package com.uff.pedalauff.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.repo.UsuarioRepo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.uff.pedalauff.enums.TipoUsuario.ADMIN;
import static com.uff.pedalauff.enums.TipoUsuario.NORMAL;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioControlador.class)
public class UsuarioControladorTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UsuarioRepo usuarioRepo;


    @Test
    public void salvarUsuario_sucesso() throws Exception {
        Usuario usuario1 = Usuario.builder()
                .idUsuario(5)
                .nome("test")
                .matricula(98456321)
                .email("test@gmail.com")
                .senha("123123123")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.when(usuarioRepo.save(usuario1)).thenReturn(usuario1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/usuario/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(usuario1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is("Usuário registrado com sucesso")));
    }

    @Test
    public void salvarUsuario_falha() throws Exception {
        Usuario usuario1 = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();
        Exception e = new Exception();
        Mockito.when(usuarioRepo.save(ArgumentMatchers.any(Usuario.class))).thenThrow(NullPointerException.class);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/usuario/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(usuario1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is("Já existe usuário cadastrado com esse email e/ou matrícula")));
    }

}
