package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.DashboardDTO;
import com.barcommerce.barcommerce.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@SecurityRequirement(name = "bearerAuth")              // para Swagger
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "Retorna métricas do dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")     // só ADMIN e GERENTE
    @GetMapping
    public ResponseEntity<DashboardDTO> obterMetricas() {
        return ResponseEntity.ok(dashboardService.obterMetricas());
    }
}
