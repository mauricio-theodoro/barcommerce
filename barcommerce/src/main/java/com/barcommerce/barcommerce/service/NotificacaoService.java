package com.barcommerce.barcommerce.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Envia notificações por e‑mail/WebSocket de forma assíncrona.
 */
@Service
public class NotificacaoService {

    @Async("taskExecutor")
    public void enviarNotificacaoFidelidade(Long clienteId, int pontos) {
        // lógica de e-mail ou WebSocket
    }
}