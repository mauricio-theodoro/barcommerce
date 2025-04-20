package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.dto.DashboardDTO;
import com.barcommerce.barcommerce.enums.StatusPagamento;
import com.barcommerce.barcommerce.enums.StatusPedido;
import com.barcommerce.barcommerce.model.Pedido;
import com.barcommerce.barcommerce.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final PedidoRepository repo;

    public DashboardDTO obterMetricas() {
        BigDecimal totalVendas = repo.calcularTotalVendas();
        long totalClientes = repo.contarClientesAtivos();
        long pend = repo.countByStatusPagamento(StatusPagamento.PENDENTE);
        long rec = repo.countByStatusPagamento(StatusPagamento.RECUSADO);
        long apr = repo.countByStatusPagamento(StatusPagamento.APROVADO);
        long ref = repo.countByStatusPagamento(StatusPagamento.REEMBOLSADO);

        return new DashboardDTO(
                totalVendas, totalClientes,
                pend, rec, apr, ref
        );
    }

    public BigDecimal totalVendasEntre(LocalDateTime start, LocalDateTime end) {
        return repo.calcularTotalVendasEntre(start, end);
    }

    public Map<StatusPedido, Long> pedidosPorStatus() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(Pedido::getStatus, Collectors.counting()));
    }


    public BigDecimal ticketMedio() {
        BigDecimal total = repo.calcularTotalVendas();
        long count = repo.count();
        return (count>0) ? total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
    }
}