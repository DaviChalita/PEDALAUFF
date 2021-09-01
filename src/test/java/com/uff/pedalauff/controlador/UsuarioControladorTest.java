package com.uff.pedalauff.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.repo.UsuarioRepo;
import org.aspectj.lang.annotation.Before;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    private UsuarioRepo UsuarioRepoSpy;
    private UsuarioControlador usuarioControlador;

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

    @Test
    public void login_sucesso() throws Exception {
        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Optional<Usuario> userOpt = Optional.of(usuario);

        Map<String,String> example = new HashMap<>();
        example.put( "senha", new String( "Admin" ));
        example.put( "email", new String( "admin@gmail.com" ));

        Mockito.when(usuarioRepo.findByEmailAndSenha("admin@gmail.com", "Admin")).thenReturn(3);
        Mockito.when(usuarioRepo.findById(3)).thenReturn(userOpt);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/usuario/logar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(example));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    public void login_falha() throws Exception {
        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Amin")
                .matricula(98456321)
                .email("adm@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Optional<Usuario> userOpt = Optional.of(usuario);

        Map<String,String> example = new HashMap<>();
        example.put( "senha", new String( "Amin" ));
        example.put( "email", new String( "adm@gmail.com" ));

        System.out.println(example);
        Mockito.when(usuarioRepo.findByEmailAndSenha("adm@gmail.com", "Amin")).thenReturn(null);
        Mockito.when(usuarioRepo.findById(3)).thenReturn(userOpt);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/usuario/logar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(example));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is("Email e/ou senha inválidos")));
    }

    @Test
    public void consultarUsuarioSemLogin_falha() throws Exception {
        UsuarioControlador usuarioControlador = new UsuarioControlador();
        UsuarioControlador spyUsuario = Mockito.spy(usuarioControlador);

        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.when(spyUsuario.userId()).thenReturn(null);
        Mockito.when(usuarioRepo.findById(usuario.getIdUsuario())).thenReturn(Optional.ofNullable(usuario));
        assertThat(spyUsuario.consultar()).isEqualTo("Você não possui acesso para realizar tal ação.");
    }

    @Test
    public void consultarUsuarioInexistente_falha() throws Exception {
        UsuarioControlador usuarioControlador = new UsuarioControlador();
        UsuarioControlador spyUsuario = Mockito.spy(usuarioControlador);

        Usuario usuario = Usuario.builder()
                .idUsuario(10)
                .nome("joao")
                .matricula(98400321)
                .email("joao@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.when(spyUsuario.userId()).thenReturn("10");
        Mockito.when(usuarioRepo.findById(usuario.getIdUsuario())).thenReturn(Optional.ofNullable(usuario));
        assertThat(spyUsuario.consultar()).isEqualTo("Usuário buscado não existe");
    }

    @Test
    public void consultarUsuario_sucesso() throws Exception {
        // Teste não mocka usuarioRepo.findById(3)
        UsuarioControlador usuarioControlador = new UsuarioControlador();
        UsuarioControlador spyUsuario = Mockito.spy(usuarioControlador);

        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Optional<Usuario> userOpt = Optional.of(usuario);
        Mockito.when(spyUsuario.userId()).thenReturn("3");
        Mockito.when(usuarioRepo.findById(3)).thenReturn(userOpt);
        assertThat(spyUsuario.consultar()).isEqualTo("Usuário uscado não existe");
    }

    @Test
    public void verTodosUsuariosSemLogin_falha() throws Exception {
        UsuarioControlador usuarioControlador = new UsuarioControlador();
        UsuarioControlador spyUsuario = Mockito.spy(usuarioControlador);

        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Iterable<Usuario> usuarios
                = Arrays.asList(usuario);

        Mockito.when(spyUsuario.userId()).thenReturn("1");
        Mockito.when(usuarioRepo.findById(usuario.getIdUsuario())).thenReturn(Optional.ofNullable(usuario));
        assertThat(spyUsuario.verTodosUsuarios()).isEqualTo("Você não possui acesso para realizar tal ação.");

    }

    @Before("")
    public void init() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioControlador).setViewResolvers(resolver).build();
    }

    @Test
    public void verTodosUsuarios_sucesso() throws Exception {
        UsuarioControlador usuarioControlador = new UsuarioControlador();
        UsuarioControlador spyUsuario = Mockito.spy(usuarioControlador);
        UsuarioRepo spyRepo = Mockito.spy(UsuarioRepo.class);

        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Iterable<Usuario> usuarios = Arrays.asList(usuario);

        Mockito.when(spyUsuario.userId()).thenReturn("1");
        Mockito.when(spyUsuario.userLvl()).thenReturn("ADMIN");
        Mockito.when(spyRepo.findAll()).thenReturn(usuarios);
        assertThat(spyUsuario.verTodosUsuarios()).isEqualTo("Você possui acesso para realizar tal ação.");

    }


}
