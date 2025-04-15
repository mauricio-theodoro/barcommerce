package com.barcommerce.barcommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * Serviço responsável por salvar arquivos enviados via Multipart.
 * Mantém lógica de geração de nome único e criação de diretórios.
 */
@Service
public class FileStorageService {

    // Será injetado de application.yml: app.upload.dir ->> Theodoro
    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Salva o arquivo na pasta configurada e retorna a URL relativa.
     * @param file Multipart enviado pelo cliente
     * @return caminho relativo para acesso (ex: /uploads/produtos/uuid_nome.jpg)
     */
    public String store(MultipartFile file) throws IOException {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path target = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(target);
            Path destination = target.resolve(filename);
            file.transferTo(destination);
            return "/uploads/produtos/" + filename;
        } catch (IOException e) {
            throw new IOException("Falha ao salvar arquivo: " + e.getMessage(), e);
        }
    }
}
