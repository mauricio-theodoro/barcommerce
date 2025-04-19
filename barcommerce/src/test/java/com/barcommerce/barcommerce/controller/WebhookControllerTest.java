package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.service.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WebhookControllerTest {

    private MockMvc mockMvc;
    private PagamentoService service;

    @BeforeEach
    void setup() {
        service = Mockito.mock(PagamentoService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new WebhookController(service)).build();
    }

    @Test
    void whenPagamentoWebhook_thenOk() throws Exception {
        String json = "{\"data\":{\"id\":123}}";
        mockMvc.perform(post("/api/webhooks/mercadopago/pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        Mockito.verify(service).confirmarPorWebhook(123L);
    }

    @Test
    void whenReembolsoWebhook_thenOk() throws Exception {
        String json = "{\"data\":{\"id\":456}}";
        mockMvc.perform(post("/api/webhooks/mercadopago/reembolso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        Mockito.verify(service).confirmarReembolsoPorWebhook(456L);
    }
}