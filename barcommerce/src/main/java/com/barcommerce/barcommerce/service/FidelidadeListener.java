
package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.dto.PontuacaoUpdateDTO;
import com.barcommerce.barcommerce.events.PedidoFechadoEvent;
import com.barcommerce.barcommerce.model.Cliente;
import com.barcommerce.barcommerce.model.ClienteFidelidade;
import com.barcommerce.barcommerce.model.PontuacaoEvento;
import com.barcommerce.barcommerce.repository.ClienteFidelidadeRepository;
import com.barcommerce.barcommerce.repository.ClienteRepository;
import com.barcommerce.barcommerce.repository.PontuacaoEventoRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Ao receber PedidoFechadoEvent, adiciona 1 ponto de fidelidade
 * ao cliente que realizou o pedido.
 */
@Component
public class FidelidadeListener {

    private final ClienteFidelidadeRepository fidelidadeRepo;
    private final ClienteRepository clienteRepo;
    private final PontuacaoEventoRepository eventoRepo;
    private final SimpMessagingTemplate messagingTemplate;

    public FidelidadeListener(ClienteRepository clienteRepo,
                              PontuacaoEventoRepository eventoRepo,
                              SimpMessagingTemplate messagingTemplate,
                              ClienteFidelidadeRepository fidelidadeRepo) {
        this.clienteRepo = clienteRepo;
        this.eventoRepo = eventoRepo;
        this.messagingTemplate = messagingTemplate;
        this.fidelidadeRepo = fidelidadeRepo;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPedidoFechado(PedidoFechadoEvent event) {
        Long clienteId = event.getPedido().getCliente().getId();

        // 1) Carrega ou cria o registro de fidelidade
        ClienteFidelidade f = fidelidadeRepo.findByClienteId(clienteId)
                .orElseGet(() -> new ClienteFidelidade(event.getPedido().getCliente()));

        // 2) Incrementa saldo e salva
        f.setPontos(f.getPontos() + 1);
        fidelidadeRepo.save(f);

        // 3) Persiste histórico de pontos
        PontuacaoEvento ev = new PontuacaoEvento(event.getPedido().getCliente(), 1, "Pedido fechado");
        eventoRepo.save(ev);

        // 4) Emite evento via WebSocket
        messagingTemplate.convertAndSend(
                "/topic/pontos/" + clienteId,
                new PontuacaoUpdateDTO(clienteId, f.getPontos())
        );
    }
}
