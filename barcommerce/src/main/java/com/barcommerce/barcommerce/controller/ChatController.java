package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.ChatRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Chatbot simples para dúvidas sobre o bar.
 */
@RestController
@RequestMapping("/api/chatbot")
public class ChatController {
    @PostMapping
    public ResponseEntity<String> chat(@RequestBody ChatRequestDTO req) {
        String txt = req.getMessage().toLowerCase();
        if (txt.contains("cardápio") || txt.contains("cardapio")) {
            return ResponseEntity.ok(
                    "Nosso cardápio está em /api/produtos - explore bebidas, comidas e sobremesas!"
            );
        }
        return ResponseEntity.ok(
                "Olá! Posso ajudar com reservas, pedidos ou informações sobre o bar. :)"
        );
    }
}