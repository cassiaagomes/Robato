package com.robato.diagnosticos.domain.composite;

import java.math.BigDecimal;

public class Glicose extends ExameSimples {
    public Glicose() {
        super("Glicose", CategoriaExame.LABORATORIAL, new BigDecimal("25.00"));
    }
}
