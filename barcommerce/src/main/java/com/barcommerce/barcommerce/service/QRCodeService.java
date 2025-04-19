package com.barcommerce.barcommerce.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Gera QR Codes para identificação de mesas.
 * A URL base é lida de application.yml (pode ser http://localhost:8080 em dev).
 */
@Service
public class QRCodeService {

    private final String baseUrl;

    public QRCodeService(@Value("${app.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Retorna um PNG em bytes contendo o QR Code que aponta para a URL de check‑in da mesa.
     *
     * @param mesaId  ID da mesa
     * @param largura largura do QR Code em pixels
     * @param altura  altura do QR Code em pixels
     * @return array de bytes do PNG
     * @throws Exception em caso de falha de geração
     */
    public byte[] gerarQRCodeParaMesa(Long mesaId, int largura, int altura) throws Exception {
        // Monta a URL completa em dev: http://localhost:8080/api/mesas/{id}/checkin
        String url = baseUrl + "/api/clientes/mesa/" + mesaId;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, largura, altura);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
            return baos.toByteArray();
        }
    }
}
