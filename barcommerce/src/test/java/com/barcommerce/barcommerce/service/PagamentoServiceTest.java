package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.enums.StatusPagamento;
import com.barcommerce.barcommerce.model.Pedido;
import com.barcommerce.barcommerce.repository.PedidoRepository;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PagamentoServiceTest {

    private PagamentoService service;
    private PedidoService pedidoService;
    private PaymentClient client;

    @BeforeEach
    void init() {
        pedidoService = mock(PedidoService.class);
        service = new PagamentoService(pedidoService);
        // injetar um PaymentClient falso
        client = mock(PaymentClient.class);
        service.paymentClient = client;
    }

    @Test
    void whenConfirmarReembolso_thenPedidoMarkedReembolsado() throws Exception {
        Payment payment = mock(Payment.class);
        when(payment.getStatus()).thenReturn("refunded");
        when(payment.getId()).thenReturn(999L);
        when(client.get(999L)).thenReturn(payment);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setIdTransacao("999");
        when(pedidoService.buscarPorIdTransacao("999")).thenReturn(Optional.of(pedido));

        service.confirmarReembolsoPorWebhook(999L);

        assertEquals(StatusPagamento.REEMBOLSADO, pedido.getStatusPagamento());
        verify(pedidoService).atualizarPagamento(pedido);
    }
}
