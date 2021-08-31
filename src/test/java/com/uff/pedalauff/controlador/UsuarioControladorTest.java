package com.uff.pedalauff.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.repo.UsuarioRepo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.uff.pedalauff.enums.TipoUsuario.ADMIN;
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
    public void consultarUsuarioSemLogin_falha() throws Exception {
        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();
        Mockito.when(usuarioRepo.findById(usuario.getIdUsuario())).thenReturn(java.util.Optional.ofNullable(usuario));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/usuario/seusdados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is("Você não possui acesso para realizar tal ação.")));
    }

    @Test
    public void salvarUsuario_sucesso() throws Exception {
        Usuario usuario = Usuario.builder()
                .idUsuario(5)
                .nome("test")
                .matricula(98456321)
                .email("test@gmail.com")
                .senha("123123123")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.when(usuarioRepo.save(usuario)).thenReturn(usuario);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/usuario/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(usuario));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is("Usuário registrado com sucesso")));
    }

    @Test
    public void salvarUsuario_falha() throws Exception {
        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.when(usuarioRepo.save(ArgumentMatchers.any(Usuario.class))).thenThrow(NullPointerException.class);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/usuario/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(usuario));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is("Já existe usuário cadastrado com esse email e/ou matrícula")));
    }

}
