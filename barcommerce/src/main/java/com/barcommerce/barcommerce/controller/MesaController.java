package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.enums.StatusMesa;
import com.barcommerce.barcommerce.model.Mesa;
import com.barcommerce.barcommerce.service.MesaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @GetMapping
    public List<Mesa> listarTodas() {
        return mesaService.listarTodas();
    }

    @PostMapping
    public Mesa criarMesa(@RequestBody Mesa mesa) {
        return mesaService.criarMesa(mesa);
    }

    @PutMapping("/{id}/status")
    public Mesa atualizarStatus(@PathVariable Long id, @RequestParam StatusMesa status) {
        return mesaService.atualizarStatusMesa(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMesa(@PathVariable Long id) {
        mesaService.deletarMesa(id);
        return ResponseEntity.noContent().build();
    }
}