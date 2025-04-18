package com.barcommerce.barcommerce.mapper;

import com.barcommerce.barcommerce.dto.ClienteDTO;
import com.barcommerce.barcommerce.model.Cliente;

/**
 * Converte entre Cliente (entidade) e ClienteDTO.
 */
public class ClienteMapper {

    public static Cliente toEntity(ClienteDTO dto) {
        if (dto == null) return null;
        Cliente c = new Cliente();
        c.setId(dto.getId());
        c.setNome(dto.getNome());
        c.setEmail(dto.getEmail());
        c.setTelefone(dto.getTelefone());
        return c;
    }

    public static ClienteDTO toDTO(Cliente entity) {
        if (entity == null) return null;
        return new ClienteDTO(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone()
        );
    }
}
