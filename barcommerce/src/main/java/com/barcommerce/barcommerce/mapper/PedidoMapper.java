package com.barcommerce.barcommerce.mapper;

import com.barcommerce.barcommerce.dto.PedidoDTO;
import com.barcommerce.barcommerce.model.Cliente;
import com.barcommerce.barcommerce.model.Mesa;
import com.barcommerce.barcommerce.model.Pedido;

import java.util.stream.Collectors;

/**
 * Converte entre Pedido (entidade) e PedidoDTO.
 */
public class PedidoMapper {

    /**
     * Converte de DTO para entidade Pedido.
     * — Vincula apenas IDs de Cliente e Mesa (busca real é feita no service).
     * — Mapeia lista de ItemPedido via ItemPedidoMapper e define relacionamento
     *   bidirecional com o pedido.
     */
    public static Pedido toEntity(PedidoDTO dto) {
        Pedido p = new Pedido();
        p.setId(dto.getId());

        // Vincula apenas o ID do cliente (evita carregar objeto completo aqui)
        Cliente c = new Cliente();
        c.setId(dto.getClienteId());
        p.setCliente(c);

        // Vincula apenas o ID da mesa
        Mesa m = new Mesa();
        m.setId(dto.getMesaId());
        p.setMesa(m);

        // Se veio um status no DTO, usa-o; senão preserva o default da entidade
        if (dto.getStatus() != null) {
            p.setStatus(dto.getStatus());
        }

        // Converte cada ItemPedidoDTO → ItemPedido e associa ao pedido
        var itens = dto.getItens().stream()
                .map(ItemPedidoMapper::toEntity)
                .peek(item -> item.setPedido(p))
                .collect(Collectors.toList());
        p.setItens(itens);

        return p;
    }

    /**
     * Converte de entidade Pedido para DTO.
     * — Extrai IDs de Cliente e Mesa.
     * — Copia status, total e dataHora já calculados no entity.
     * — Converte lista de ItemPedido → ItemPedidoDTO.
     */
    public static PedidoDTO toDTO(Pedido p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(p.getId());
        dto.setClienteId(p.getCliente().getId());
        dto.setMesaId(p.getMesa().getId());
        dto.setStatus(p.getStatus());
        dto.setTotal(p.getTotal());
        dto.setDataHora(p.getDataHora());

        var itensDto = p.getItens().stream()
                .map(ItemPedidoMapper::toDTO)
                .collect(Collectors.toList());
        dto.setItens(itensDto);

        return dto;
    }
}
