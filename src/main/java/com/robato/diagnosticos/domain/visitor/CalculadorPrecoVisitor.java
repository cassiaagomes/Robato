package com.robato.diagnosticos.domain.visitor;

import java.math.BigDecimal;

import com.robato.diagnosticos.domain.composite.ExameSimples;
import com.robato.diagnosticos.domain.composite.PacoteExames;

public class CalculadorPrecoVisitor implements ExameVisitor {

    private BigDecimal precoTotal = BigDecimal.ZERO;

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    @Override
    public void visit(ExameSimples exame) {
        precoTotal = precoTotal.add(exame.getPrecoBase());
    }

    @Override
    public void visit(PacoteExames pacote) {
        pacote.getItens().forEach(item -> item.accept(this));
    }
}