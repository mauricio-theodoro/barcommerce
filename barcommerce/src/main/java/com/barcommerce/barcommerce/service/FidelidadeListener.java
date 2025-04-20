
package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.dto.PontuacaoUpdateDTO;
import com.barcommerce.barcommerce.events.PedidoFechadoEvent;
import com.barcommerce.barcommerce.model.Cliente;
import com.barcommerce.barcommerce.model.PontuacaoEvento;
import com.barcommerce.barcommerce.repository.ClienteRepository;
import com.barcommerce.barcommerce.repository.PontuacaoEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ao receber PedidoFechadoEvent, adiciona 1 ponto de fidelidade
 * ao cliente que realizou o pedido.
 */
@Component
public class FidelidadeListener {

    @Autowired
    private final ClienteRepository clienteRepo;

    @Autowired
    private final PontuacaoEventoRepository eventoRepo;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public FidelidadeListener(ClienteRepository clienteRepo, PontuacaoEventoRepository eventoRepo) {
        this.clienteRepo = clienteRepo;
        this.eventoRepo = eventoRepo;
    }

    @EventListener
    @Transactional
    public void onPedidoFechado(PedidoFechadoEvent event) {
        Cliente cliente = event.getPedido().getCliente();
        // adiciona 1 ponto
        cliente.setPontosFidelidade(cliente.getPontosFidelidade() + 1);
        clienteRepo.save(cliente);
        // aqui poderíamos também enviar notificação via WebSocket, e‑mail etc.

        // registra histórico
        PontuacaoEvento ev = new PontuacaoEvento(cliente, 1, "Pedido fechado");
        eventoRepo.save(ev);

        // envia via WebSocket para subscribers em /topic/pontos/{clienteId}
        messagingTemplate.convertAndSend(
                "/topic/pontos/" + cliente.getId(),
                new PontuacaoUpdateDTO(cliente.getId(), cliente.getPontosFidelidade())
        );
        // TODO: verificar limites para emitir vouchers ou badges
    }
}
