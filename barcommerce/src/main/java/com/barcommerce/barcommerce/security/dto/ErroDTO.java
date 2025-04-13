package com.barcommerce.barcommerce.security.dto;

import java.util.List;

/**
 * DTO para mensagens de erro padronizadas.
 */
public class ErroDTO {

    private List<String> erros;

    public ErroDTO(String mensagem) {
        this.erros = List.of(mensagem);
    }

    public ErroDTO(List<String> erros) {
        this.erros = erros;
    }

    public List<String> getErros() {
        return erros;
    }

    public void setErros(List<String> erros) {
        this.erros = erros;
    }
}
