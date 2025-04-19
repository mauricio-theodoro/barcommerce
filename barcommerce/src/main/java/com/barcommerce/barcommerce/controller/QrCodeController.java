package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.service.MesaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints relacionados ao fluxo de QR‑Code e check‑in de mesas.
 */
@RestController
@RequestMapping("/api/qr")
public class QrCodeController {

    private final MesaService mesaService;

    public QrCodeController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @PostMapping("/entrada")
    @Operation(summary = "Check‑in de cliente via QR Code")
    public ResponseEntity<String> escanearQrCode(
            @RequestParam Long mesaId,
            @RequestParam Long clienteId,
            @RequestParam String deviceId,
            @RequestParam(defaultValue = "false") boolean anfitriao,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude
    ) {
        // 1) Se vier latitude/longitude, valida proximidade
        mesaService.validarDistancia(mesaId, latitude, longitude);

        // 2) Associa cliente à mesa
        mesaService.atribuirCliente(mesaId, clienteId);

        // 3) Registra sessão temporária para este dispositivo
        mesaService.registrarSessao(mesaId, deviceId, anfitriao);

        return ResponseEntity.ok("Check‑in realizado com sucesso.");
    }
}
