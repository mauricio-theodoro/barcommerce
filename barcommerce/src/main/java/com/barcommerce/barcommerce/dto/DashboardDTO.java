package com.barcommerce.barcommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DashboardDTO {
    private BigDecimal totalVendas;
    private Long totalClientesAtivos;  // renomeado para maior clareza
}
