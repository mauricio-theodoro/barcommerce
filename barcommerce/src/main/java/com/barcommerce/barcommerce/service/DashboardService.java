package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.dto.DashboardDTO;
import com.barcommerce.barcommerce.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PedidoRepository pedidoRepository;

    public DashboardDTO obterMetricas() {
        BigDecimal totalVendas = pedidoRepository.calcularTotalVendas();
        Long totalClientes = pedidoRepository.contarClientesAtivos();
        // Adicione mais métricas conforme necessário
        return new DashboardDTO(totalVendas, totalClientes);
    }
}