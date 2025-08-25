package com.robato.diagnosticos.infrastructure.decorator;

import java.math.BigDecimal;

public class PrecoBase implements Desconto {

    @Override
    public BigDecimal aplicar(BigDecimal valorOriginal) {
        return valorOriginal;
    }
}
