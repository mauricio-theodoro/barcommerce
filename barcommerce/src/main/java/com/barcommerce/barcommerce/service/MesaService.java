package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.enums.StatusMesa;
import com.barcommerce.barcommerce.model.Cliente;
import com.barcommerce.barcommerce.model.Mesa;
import com.barcommerce.barcommerce.repository.ClienteRepository;
import com.barcommerce.barcommerce.repository.MesaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;
    private final ClienteRepository clienteRepository;

    public MesaService(MesaRepository mesaRepository, ClienteRepository clienteRepository) {
        this.mesaRepository = mesaRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    public Mesa criarMesa(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public Mesa atualizarStatusMesa(Long id, StatusMesa novoStatus) {
        return mesaRepository.findById(id).map(mesa -> {
            mesa.setStatus(novoStatus);
            return mesaRepository.save(mesa);
        }).orElseThrow(() -> new RuntimeException("Mesa não encontrada"));
    }

    public void deletarMesa(Long id) {
        mesaRepository.deleteById(id);
    }

    /**
     * Atribui uma mesa a um cliente após scan do QR.
     */
    public void atribuirCliente(Long mesaId, Long clienteId) {
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new EntityNotFoundException("Mesa não encontrada"));
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        cliente.setMesa(mesa);
        clienteRepository.save(cliente);
    }
}