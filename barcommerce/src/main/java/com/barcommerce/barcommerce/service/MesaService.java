package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.enums.StatusMesa;
import com.barcommerce.barcommerce.model.Mesa;
import com.barcommerce.barcommerce.repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
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
}