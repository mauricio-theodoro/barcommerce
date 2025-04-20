package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.ClienteDTO;
import com.barcommerce.barcommerce.mapper.ClienteMapper;
import com.barcommerce.barcommerce.model.ClienteFidelidade;
import com.barcommerce.barcommerce.model.PontuacaoEvento;
import com.barcommerce.barcommerce.repository.ClienteFidelidadeRepository;
import com.barcommerce.barcommerce.repository.ClienteRepository;
import com.barcommerce.barcommerce.repository.PontuacaoEventoRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Exposição de pontos de fidelidade.
 */
@RestController
@RequestMapping("/api/fidelidade")
public class FidelidadeController {

    private final ClienteFidelidadeRepository repo;
    private final PontuacaoEventoRepository eventoRepo;
    private final ClienteRepository clienteRepo;

    public FidelidadeController(ClienteFidelidadeRepository repo,
                                PontuacaoEventoRepository eventoRepo,
                                ClienteRepository clienteRepo) {
        this.repo = repo;
        this.eventoRepo = eventoRepo;
        this.clienteRepo = clienteRepo;
    }

    @Operation(summary = "Consulta saldo de pontos de um cliente")
    @GetMapping("/{clienteId}")
    public ResponseEntity<Integer> getPontos(@PathVariable Long clienteId) {
        Optional<ClienteFidelidade> opt = repo.findByClienteId(clienteId);
        return opt
                .map(f -> ResponseEntity.ok(f.getPontos()))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/fidelidade/ranking?limit=10
     * retorna top N clientes por pontos.
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<ClienteDTO>> ranking(
            @RequestParam(defaultValue = "10") int limit) {
        List<ClienteDTO> top = clienteRepo.findAll().stream()
                .sorted((a,b) -> b.getPontosFidelidade() - a.getPontosFidelidade())
                .limit(limit)
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(top);
    }

    /**
     * GET /api/fidelidade/historico/{clienteId}
     * retorna histórico de eventos de pontuação.
     */
    @GetMapping("/historico/{clienteId}")
    public ResponseEntity<List<PontuacaoEvento>> historico(
            @PathVariable Long clienteId) {
        List<PontuacaoEvento> eventos = eventoRepo
                .findByClienteIdOrderByTimestampDesc(clienteId);
        return ResponseEntity.ok(eventos);
    }
}
