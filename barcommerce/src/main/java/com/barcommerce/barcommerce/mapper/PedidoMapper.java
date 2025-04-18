package com.barcommerce.barcommerce.mapper;

import com.barcommerce.barcommerce.dto.PedidoDTO;
import com.barcommerce.barcommerce.model.Cliente;
import com.barcommerce.barcommerce.model.ItemPedido;
import com.barcommerce.barcommerce.model.Mesa;
import com.barcommerce.barcommerce.model.Pedido;

import java.util.stream.Collectors;

/**
 * Converte entre Pedido (entidade) e PedidoDTO.
 */
public class PedidoMapper {

    /** Converte de DTO para entidade Pedido */
    public static Pedido toEntity(PedidoDTO dto) {
        Pedido p = new Pedido();
        p.setId(dto.getId());

        // Vincula apenas o ID do cliente (evita buscar o objeto completo aqui)
        Cliente c = new Cliente();
        c.setId(dto.getClienteId());
        p.setCliente(c);

        // Vincula apenas o ID da mesa
        Mesa m = new Mesa();
        m.setId(dto.getMesaId());
        p.setMesa(m);

        // Define o status do pedido (ou usa o default da entidade)
        p.setStatus(dto.getStatus() != null ? dto.getStatus() : p.getStatus());

        // Converte cada ItemPedidoDTO → ItemPedido, e define o pedido pai
        var itens = dto.getItens().stream()
                .map(ItemPedidoMapper::toEntity) // Certifique-se que este método existe
                .peek(item -> item.setPedido(p)) // Importante: item precisa ter setPedido()
                .collect(Collectors.toList());

        p.setItens(itens);

        return p;
    }

    /** Converte de entidade Pedido para DTO */
    public static PedidoDTO toDTO(Pedido p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(p.getId());
        dto.setClienteId(p.getCliente().getId());
        dto.setMesaId(p.getMesa().getId());
        dto.setStatus(p.getStatus());
        dto.setTotal(p.getTotal());
        dto.setDataHora(p.getDataHora());

        // Converte cada ItemPedido → ItemPedidoDTO
        var itensDto = p.getItens().stream()
                .map(ItemPedidoMapper::toDTO) // Certifique-se que este método existe
                .collect(Collectors.toList());

        dto.setItens(itensDto);

        return dto;
    }
}
