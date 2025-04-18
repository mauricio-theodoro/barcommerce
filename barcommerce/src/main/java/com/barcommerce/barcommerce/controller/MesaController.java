package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.enums.StatusMesa;
import com.barcommerce.barcommerce.model.Mesa;
import com.barcommerce.barcommerce.service.MesaService;
import com.barcommerce.barcommerce.service.QRCodeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService mesaService;
    private final QRCodeService qrService;

    public MesaController(MesaService mesaService, QRCodeService qrService) {
        this.mesaService = mesaService;
        this.qrService = qrService;
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

    /**
     * Retorna a imagem PNG do QR Code para a mesa.
     */
    @GetMapping("/{id}/qrcode")
    public void downloadQRCode(@PathVariable Long id, HttpServletResponse response) throws Exception {
        byte[] png = qrService.gerarQRCodeParaMesa(id, 250, 250);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        try (OutputStream os = response.getOutputStream()) {
            os.write(png);
        }
    }

    /**
     * Quando o cliente escaneia o QR, atribuimos a mesa ao cliente.
     */
    @PostMapping("/{id}/checkin")
    public void checkinMesa(
            @PathVariable Long id,
            @RequestParam Long clienteId) {
        mesaService.atribuirCliente(id, clienteId);
    }
}