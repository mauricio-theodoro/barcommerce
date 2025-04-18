// src/main/java/com/barcommerce/barcommerce/controller/ClienteController.java
package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.ClienteDTO;
import com.barcommerce.barcommerce.mapper.ClienteMapper;
import com.barcommerce.barcommerce.model.Cliente;
import com.barcommerce.barcommerce.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gerenciamento de clientes.
 * Acesso protegido por JWT; somente ADMIN e GERENTE podem manipular.
 */
@RestController
@RequestMapping("/api/clientes")
@PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os clientes")
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listar() {
        List<ClienteDTO> dtos = service.listarTodos().stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Busca cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ClienteMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria novo cliente")
    @PostMapping
    public ResponseEntity<ClienteDTO> criar(@Valid @RequestBody ClienteDTO dto) {
        Cliente criado = service.criarCliente(ClienteMapper.toEntity(dto));
        URI location = URI.create("/api/clientes/" + criado.getId());
        return ResponseEntity.created(location)
                .body(ClienteMapper.toDTO(criado));
    }

    @Operation(summary = "Atualiza cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO dto) {

        Cliente atualizado = service.atualizarCliente(id, ClienteMapper.toEntity(dto));
        return ResponseEntity.ok(ClienteMapper.toDTO(atualizado));
    }

    @Operation(summary = "Deleta cliente por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
