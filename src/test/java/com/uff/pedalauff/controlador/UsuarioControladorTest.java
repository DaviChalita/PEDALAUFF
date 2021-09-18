package com.uff.pedalauff.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uff.pedalauff.modelo.Usuario;
import com.uff.pedalauff.repo.UsuarioRepo;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.uff.pedalauff.enums.TipoUsuario.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioControlador.class)
public class UsuarioControladorTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Mock
    private UsuarioRepo repo;

    @InjectMocks
    @Spy
    private UsuarioControlador controller;

    @MockBean
    UsuarioRepo usuarioRepo;

    @Test
    public void consultarUsuario_sucesso() {

        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.doReturn(java.util.Optional.ofNullable(usuario)).when(repo).findById(3);
        Mockito.doReturn("3").when(controller).userId();
        assertThat(controller.consultar())
                .isEqualTo("Dados do usuário logado: \n" +
                        "Nome: Admin\n" +
                        "Matrícula: 98456321\n" +
                        "Email: admin@gmail.com");
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

        Map<String, String> example = new HashMap<>();
        example.put("senha", new String("Admin"));
        example.put("email", new String("admin@gmail.com"));

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

        Map<String, String> example = new HashMap<>();
        example.put("senha", new String("Amin"));
        example.put("email", new String("adm@gmail.com"));

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
    public void verTodosUsuarios_sucesso() throws Exception {

        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();
        Usuario usuario2 = Usuario.builder()
                .idUsuario(3)
                .nome("Joao")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();
        Usuario usuario3 = Usuario.builder()
                .idUsuario(3)
                .nome("Maria")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Iterable<Usuario> usuarios = Arrays.asList(usuario, usuario2, usuario3);

        Mockito.doReturn(usuarios).when(repo).findAll();
        Mockito.doReturn("3").when(controller).userId();
        Mockito.doReturn("ADMIN").when(controller).userLvl();
        assertThat(controller.verTodosUsuarios()).isEqualTo("Lista de Usuários:\n" +
                "Usuario: 3\n" +
                "Nome do usuario: Admin\n" +
                "Nível de acesso: ADMIN\n" +
                "Email do usuário: admin@gmail.com\n" +
                "Matrícula do usuário: 98456321\n" +
                "Usuario: 3\n" +
                "Nome do usuario: Joao\n" +
                "Nível de acesso: ADMIN\n" +
                "Email do usuário: admin@gmail.com\n" +
                "Matrícula do usuário: 98456321\n" +
                "Usuario: 3\n" +
                "Nome do usuario: Maria\n" +
                "Nível de acesso: ADMIN\n" +
                "Email do usuário: admin@gmail.com\n" +
                "Matrícula do usuário: 98456321\n");
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

    @Test
    public void realizarLogout_sucesso() {
        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.doReturn(java.util.Optional.ofNullable(usuario)).when(repo).findById(3);
        Mockito.doReturn("3").when(controller).userId();
        assertThat(controller.logout())
                .isEqualTo("Usuário deslogado com sucesso");
    }

    @Test
    public void realizarLogout_falha() {
        Usuario usuario = Usuario.builder()
                .idUsuario(3)
                .nome("Admin")
                .matricula(98456321)
                .email("admin@gmail.com")
                .senha("987456321")
                .tipoUsuario(ADMIN)
                .build();

        Mockito.doReturn(java.util.Optional.ofNullable(usuario)).when(repo).findById(3);
        Mockito.doReturn(null).when(controller).userId();
        assertThat(controller.logout())
                .isEqualTo("Você não está logado");
    }
}
