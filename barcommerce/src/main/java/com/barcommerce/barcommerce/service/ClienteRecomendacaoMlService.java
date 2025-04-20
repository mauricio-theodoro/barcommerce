package com.barcommerce.barcommerce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.Collections;
import java.util.List;

/**
 * Cliente HTTP para comunicação com o micro‑serviço de ML
 * que gera recomendações colaborativas de produtos.
 *
 * Se o serviço estiver indisponível, devolve lista vazia
 * (fallback), permitindo que o app continue funcionando.
 */
@Component
public class ClienteRecomendacaoMlService {

    private static final Logger log = LoggerFactory.getLogger(ClienteRecomendacaoMlService.class);

    private final WebClient webClient;

    public ClienteRecomendacaoMlService(WebClient.Builder builder,
                                        @Value("${servico.ml.url}") String urlServicoMl) {
        this.webClient = builder
                .baseUrl(urlServicoMl)
                .build();
    }

    /**
     * Solicita ao micro‑serviço de ML uma lista de IDs de produtos
     * recomendados, ordenados por relevância para o cliente.
     *
     * Se der qualquer falha de conexão ou timeout, retorna lista vazia.
     *
     * @param idCliente identificador do cliente
     * @param limite    número máximo de recomendações
     * @return lista de IDs de produtos (pode ser vazia)
     */
    public List<Long> buscarIdsProdutosRecomendados(Long idCliente, int limite) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/recommend")
                            .queryParam("clienteId", idCliente)
                            .queryParam("limit", limite)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                    .block();
        } catch (WebClientRequestException ex) {
            log.warn("Serviço de ML indisponível em {} → fallback para lista vazia", webClient, ex);
            return Collections.emptyList();
        }
    }
}
