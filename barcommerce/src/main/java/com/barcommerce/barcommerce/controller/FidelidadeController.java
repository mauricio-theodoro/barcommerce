package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.model.ClienteFidelidade;
import com.barcommerce.barcommerce.repository.ClienteFidelidadeRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Exposição de pontos de fidelidade.
 */
@RestController
@RequestMapping("/api/fidelidade")
public class FidelidadeController {

    private final ClienteFidelidadeRepository repo;

    public FidelidadeController(ClienteFidelidadeRepository repo) {
        this.repo = repo;
    }

    @Operation(summary = "Consulta saldo de pontos de um cliente")
    @GetMapping("/{clienteId}")
    public ResponseEntity<Integer> getPontos(@PathVariable Long clienteId) {
        Optional<ClienteFidelidade> opt = repo.findByClienteId(clienteId);
        return opt
                .map(f -> ResponseEntity.ok(f.getPontos()))
                .orElse(ResponseEntity.notFound().build());
    }
}
