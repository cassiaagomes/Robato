package com.robato.diagnosticos.domain.composite;

import java.math.BigDecimal;

public class Ureia extends ExameSimples {
    public Ureia() {
        super("Ureia", CategoriaExame.LABORATORIAL, new BigDecimal("30.00"));
    }
}