package com.uff.pedalauff.controlador;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest
public class StatusControladorTest {

    private StatusController statusController;
    private MockMvc mockMvc;

    public StatusControladorTest(){
        statusController = new StatusController();
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(statusController)
                .build();
    }

    public void quandoChamadoRetornaOnline() {

    }
}
