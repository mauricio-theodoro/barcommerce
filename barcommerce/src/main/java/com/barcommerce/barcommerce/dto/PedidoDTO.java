package com.barcommerce.barcommerce.dto;

import com.barcommerce.barcommerce.enums.StatusPedido;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferência de dados de Pedido.
 */
public class PedidoDTO {

    private Long id;

    @NotNull(message = "ID da mesa é obrigatório.")
    private Long mesaId;

    @NotEmpty(message = "Pedido deve ter ao menos um item.")
    private List<ItemPedidoDTO> itens;

    private StatusPedido status;
    private BigDecimal total;
    private LocalDateTime dataHora;

    public PedidoDTO() {
        // Construtor para desserialização
    }

    public PedidoDTO(Long id, Long mesaId, List<ItemPedidoDTO> itens,
                     StatusPedido status, BigDecimal total, LocalDateTime dataHora) {
        this.id = id;
        this.mesaId = mesaId;
        this.itens = itens;
        this.status = status;
        this.total = total;
        this.dataHora = dataHora;
    }

    // Getters e Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getMesaId() { return mesaId; }

    public void setMesaId(Long mesaId) { this.mesaId = mesaId; }

    public List<ItemPedidoDTO> getItens() { return itens; }

    public void setItens(List<ItemPedidoDTO> itens) { this.itens = itens; }

    public StatusPedido getStatus() { return status; }

    public void setStatus(StatusPedido status) { this.status = status; }

    public BigDecimal getTotal() { return total; }

    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDateTime getDataHora() { return dataHora; }

    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
