package com.barcommerce.barcommerce.repository;

import com.barcommerce.barcommerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

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


}