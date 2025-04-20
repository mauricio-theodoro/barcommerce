package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.RecomendacaoDTO;
import com.barcommerce.barcommerce.service.RecomendacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST para recomendações personalizadas.
 */
@RestController
@RequestMapping("/api/recommendations")
public class RecomendacaoController {
    private final RecomendacaoService service;

    public RecomendacaoController(RecomendacaoService service) {
        this.service = service;
    }

    /**
     * GET /api/recommendations?clienteId={id}&limit={n}
     * Retorna `limit` principais produtos recomendados para o cliente.
     */
    @GetMapping
    public ResponseEntity<List<RecomendacaoDTO>> get(
            @RequestParam Long clienteId,
            @RequestParam(defaultValue = "3") int limit) {
        return ResponseEntity.ok(
                service.getRecommendationsForCliente(clienteId, limit)
        );
    }
}
