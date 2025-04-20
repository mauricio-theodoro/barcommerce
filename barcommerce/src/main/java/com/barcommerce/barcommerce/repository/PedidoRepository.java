package com.barcommerce.barcommerce.repository;

import com.barcommerce.barcommerce.enums.StatusPagamento;
import com.barcommerce.barcommerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.barcommerce.barcommerce.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Busca um pedido pelo ID da transação retornado pelo gateway.
     */
    Optional<Pedido> findByIdTransacao(String idTransacao);

    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.status = 'ENTREGUE'")
    BigDecimal calcularTotalVendas();

    @Query("SELECT COUNT(DISTINCT p.cliente) FROM Pedido p")
    Long contarClientesAtivos();

    /** Soma total dos pedidos entregues num intervalo */
    @Query("SELECT COALESCE(SUM(p.total),0) " +
            "FROM Pedido p " +
            "WHERE p.status = 'ENTREGUE' " +
            "AND p.dataHora BETWEEN :start AND :end")
    BigDecimal calcularTotalVendasEntre(LocalDateTime start, LocalDateTime end);

    List<Pedido> findByStatusPagamento(StatusPagamento statusPagamento);
    long countByStatusPagamento(StatusPagamento statusPagamento);

    /**
     * Consulta todos pedidos de um cliente com determinado status.
     */
    List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status);
}