package com.robato.diagnosticos.infrastructure.decorator;


import java.math.BigDecimal;

public interface Desconto {
    BigDecimal aplicar(BigDecimal valorOriginal);
}
