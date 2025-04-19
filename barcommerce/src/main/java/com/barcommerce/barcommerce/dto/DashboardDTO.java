package com.barcommerce.barcommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DashboardDTO {
    private BigDecimal totalVendas;
    private long totalClientes;
    private long pagamentosPendentes;
    private long pagamentosRecusados;
    private long pagamentosAprovados;
    private long pagamentosReembolsados;
}
