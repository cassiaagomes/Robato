package com.robato.diagnosticos.domain.composite;

import java.math.BigDecimal;

public class HemogramaCompleto extends ExameSimples {
    public HemogramaCompleto() {
        super("Hemograma Completo", CategoriaExame.LABORATORIAL, new BigDecimal("70.00"));
    }
}
