package com.barcommerce.barcommerce.events;

import org.springframework.context.ApplicationEvent;
import com.barcommerce.barcommerce.model.Pedido;

/**
 * Publicado sempre que um Pedido é fechado (status FECHADO).
 */
public class PedidoFechadoEvent extends ApplicationEvent {
    private final Pedido pedido;

    public PedidoFechadoEvent(Object source, Pedido pedido) {
        super(source);
        this.pedido = pedido;
    }

    public Pedido getPedido() {
        return pedido;
    }
}
