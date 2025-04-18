// src/main/java/com/barcommerce/barcommerce/service/ClienteService.java
package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.model.Cliente;
import com.barcommerce.barcommerce.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    /** Lista todos os clientes. */
    public List<Cliente> listarTodos() {
        return repo.findAll();
    }

    /** Busca cliente por ID. */
    public Optional<Cliente> buscarPorId(Long id) {
        return repo.findById(id);
    }

    /** Cria um novo cliente, evitando e‑mail duplicado. */
    public Cliente criarCliente(Cliente cliente) {
        if (repo.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Já existe cliente com esse e‑mail");
        }
        return repo.save(cliente);
    }

    /** Atualiza cliente existente. */
    public Cliente atualizarCliente(Long id, Cliente dados) {
        return repo.findById(id).map(c -> {
            if (!c.getEmail().equals(dados.getEmail()) && repo.existsByEmail(dados.getEmail())) {
                throw new IllegalArgumentException("E‑mail já cadastrado em outro cliente");
            }
            c.setNome(dados.getNome());
            c.setEmail(dados.getEmail());
            c.setTelefone(dados.getTelefone());
            return repo.save(c);
        }).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    /** Deleta cliente por ID. */
    public void deletarCliente(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }
        repo.deleteById(id);
    }
}
