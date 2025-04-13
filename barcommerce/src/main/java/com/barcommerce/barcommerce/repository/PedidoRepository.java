package com.barcommerce.barcommerce.repository;

import com.barcommerce.barcommerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}