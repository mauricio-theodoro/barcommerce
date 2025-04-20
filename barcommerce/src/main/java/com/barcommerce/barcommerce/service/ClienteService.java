// src/main/java/com/barcommerce/barcommerce/service/ClienteService.java
package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.model.Cliente;
import com.barcommerce.barcommerce.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repo;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
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

        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);

        return repo.save(cliente);
    }

    /** Atualiza cliente existente. */
    public Cliente atualizarCliente(Long id, Cliente dados) {
        return repo.findById(id).map(c -> {
            if (!c.getEmail().equals(dados.getEmail()) && repo.existsByEmail(dados.getEmail())) {
                throw new IllegalArgumentException("E-mail já cadastrado em outro cliente");
            }
            c.setNome(dados.getNome());
            c.setEmail(dados.getEmail());
            c.setTelefone(dados.getTelefone());

            // Se quiser permitir atualização de senha, criptografe também aqui:
            if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
                String novaSenhaCriptografada = passwordEncoder.encode(dados.getSenha());
                c.setSenha(novaSenhaCriptografada);
            }

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
