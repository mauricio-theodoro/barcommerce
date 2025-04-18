package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.MovimentacaoCaixaDTO;
import com.barcommerce.barcommerce.service.CaixaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/caixa")
@PreAuthorize("hasRole('GERENTE')")
public class CaixaController {

    private final CaixaService caixaService;

    public CaixaController(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    @Operation(summary = "Abre o caixa")
    @PostMapping("/abertura")
    public ResponseEntity<MovimentacaoCaixaDTO> abrirCaixa(
            @RequestParam BigDecimal saldoInicial) {
        return ResponseEntity.ok(caixaService.abrirCaixa(saldoInicial));
    }

    @Operation(summary = "Fecha o caixa")
    @PostMapping("/fechamento")
    public ResponseEntity<MovimentacaoCaixaDTO> fecharCaixa() {
        return ResponseEntity.ok(caixaService.fecharCaixa());
    }
}