package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.dto.DashboardDTO;
import com.barcommerce.barcommerce.enums.StatusPagamento;
import com.barcommerce.barcommerce.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
}