package com.robato.diagnosticos.infrastructure.decorator;

import java.math.BigDecimal;

public class DescontoIdoso implements Desconto {

    private final Desconto proximoDesconto;

    public DescontoIdoso(Desconto proximoDesconto) {
        this.proximoDesconto = proximoDesconto;
    }

    @Override
    public BigDecimal aplicar(BigDecimal valorOriginal) {
    
        BigDecimal valorAposProximo = proximoDesconto.aplicar(valorOriginal);
        return valorAposProximo.multiply(new BigDecimal("0.92"));
    }
}