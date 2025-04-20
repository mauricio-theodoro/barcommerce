// src/main/java/com/barcommerce/barcommerce/mapper/ClienteMapper.java
package com.barcommerce.barcommerce.mapper;

import com.barcommerce.barcommerce.dto.ClienteDTO;
import com.barcommerce.barcommerce.model.Cliente;

/**
 * Converte entre Cliente (entidade JPA) e ClienteDTO.
 * <ul>
 *   <li>toEntity: adiciona também senha e dataNascimento, para criação/atualização.</li>
 *   <li>toDTO: não mapeia a senha (hash) — evita vazamento de credenciais.</li>
 * </ul>
 */
public class ClienteMapper {

    private ClienteMapper() { /* utilitário estático — não instanciar */ }

    /**
     * Popula entidade a partir do DTO recebido no body da requisição.
     * A senha deve ser criptografada em ClienteService antes do save().
     */
    public static Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }
        Cliente c = new Cliente();
        c.setId(dto.getId());
        c.setNome(dto.getNome());
        c.setEmail(dto.getEmail());
        c.setTelefone(dto.getTelefone());
        // campo de texto plano; service é responsável por criptografar
        c.setSenha(dto.getSenha());
        c.setDataNascimento(dto.getDataNascimento());
        return c;
    }

    /**
     * Gera DTO de resposta a partir da entidade.
     * Não inclui senha (mesmo hashed) por segurança.
     */
    public static ClienteDTO toDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }
        ClienteDTO dto = new ClienteDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setDataNascimento(entity.getDataNascimento());
        // senha não é copiada para a resposta
        return dto;
    }
}
