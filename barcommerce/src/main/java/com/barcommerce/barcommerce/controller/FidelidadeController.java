package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.ClienteDTO;
import com.barcommerce.barcommerce.dto.ClienteRankingDTO;
import com.barcommerce.barcommerce.dto.PontuacaoEventoDTO;
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

    public FidelidadeController(ClienteFidelidadeRepository repo,
                                PontuacaoEventoRepository eventoRepo) {
        this.repo = repo;
        this.eventoRepo = eventoRepo;
    }

    @Operation(summary = "Consulta saldo de pontos de um cliente")
    @GetMapping("/{clienteId}")
    public ResponseEntity<Integer> getPontos(@PathVariable Long clienteId) {
        return repo.findByClienteId(clienteId)
                .map(f -> ResponseEntity.ok(f.getPontos()))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/fidelidade/ranking?limit=10
     * retorna top N clientes por pontos.
     */
    @Operation(summary = "Ranking de clientes por saldo de pontos")
    @GetMapping("/ranking")
    public ResponseEntity<List<ClienteRankingDTO>> ranking(
            @RequestParam(defaultValue = "10") int limit) {
        List<ClienteRankingDTO> clientes = repo.findAll().stream()
                .sorted((a, b) -> b.getPontos() - a.getPontos())
                .limit(limit)
                .map(f -> {
                    var cliente = f.getCliente();
                    return new ClienteRankingDTO(
                            cliente.getId(),
                            cliente.getNome(),
                            cliente.getEmail(),
                            cliente.getTelefone(),
                            cliente.getDataNascimento(),
                            f.getPontos()
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /api/fidelidade/historico/{clienteId}
     * retorna histórico de eventos de pontuação.
     */
    @Operation(summary = "Histórico de pontuação do cliente")
    @GetMapping("/historico/{clienteId}")
    public ResponseEntity<List<PontuacaoEventoDTO>> historico(@PathVariable Long clienteId) {
        List<PontuacaoEventoDTO> eventos = eventoRepo
                .findByClienteIdOrderByTimestampDesc(clienteId)
                .stream()
                .map(e -> new PontuacaoEventoDTO(
                        e.getId(),
                        e.getCliente().getId(),
                        e.getPontosAlterados(),
                        e.getMotivo(),
                        e.getTimestamp()
                ))
                .toList();
        return ResponseEntity.ok(eventos);
    }
}
