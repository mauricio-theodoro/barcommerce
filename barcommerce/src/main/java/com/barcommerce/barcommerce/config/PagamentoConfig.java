package com.barcommerce.barcommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.mercadopago.MercadoPagoConfig;

@Configuration
public class PagamentoConfig {

    public PagamentoConfig(@Value("${mercadopago.access.token}") String accessToken) {
        MercadoPagoConfig.setAccessToken(accessToken); // Inicializa o SDK
    }
}