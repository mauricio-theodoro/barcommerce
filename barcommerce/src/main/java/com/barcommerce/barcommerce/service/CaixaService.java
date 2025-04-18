package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.dto.MovimentacaoCaixaDTO;
import com.barcommerce.barcommerce.model.MovimentacaoCaixa;
import com.barcommerce.barcommerce.repository.CaixaRepository;
import com.barcommerce.barcommerce.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Serviço responsável por toda a lógica de abertura e fechamento de caixa.
 */
@Service
public class CaixaService {

    private final CaixaRepository caixaRepo;
    private final PedidoRepository pedidoRepo;

    public CaixaService(CaixaRepository caixaRepo,
                        PedidoRepository pedidoRepo) {
        this.caixaRepo = caixaRepo;
        this.pedidoRepo = pedidoRepo;
    }

    /**
     * Abre um novo caixa, registra saldoInicial e data de abertura.
     */
    public MovimentacaoCaixaDTO abrirCaixa(BigDecimal saldoInicial) {
        MovimentacaoCaixa caixa = new MovimentacaoCaixa();
        caixa.setSaldoInicial(saldoInicial);
        caixa.setAbertura(LocalDateTime.now());
        MovimentacaoCaixa salvo = caixaRepo.save(caixa);
        return toDTO(salvo);
    }

    /**
     * Fecha o caixa mais recente ainda não fechado, soma vendas e calcula saldoFinal.
     */
    public MovimentacaoCaixaDTO fecharCaixa() {
        MovimentacaoCaixa caixa = caixaRepo.findTopByOrderByAberturaDesc()
                .orElseThrow(() -> new EntityNotFoundException("Nenhum caixa aberto"));
        if (caixa.getFechamento() != null) {
            throw new IllegalStateException("Caixa já foi fechado");
        }
        caixa.setFechamento(LocalDateTime.now());
        BigDecimal vendas = pedidoRepo.calcularTotalVendasEntre(
                caixa.getAbertura(), caixa.getFechamento());
        caixa.setSaldoFinal(caixa.getSaldoInicial().add(vendas));
        MovimentacaoCaixa fechado = caixaRepo.save(caixa);
        return toDTO(fechado);
    }

    private MovimentacaoCaixaDTO toDTO(MovimentacaoCaixa c) {
        return new MovimentacaoCaixaDTO(
                c.getId(),
                c.getSaldoInicial(),
                c.getSaldoFinal(),
                c.getAbertura(),
                c.getFechamento()
        );
    }
}
