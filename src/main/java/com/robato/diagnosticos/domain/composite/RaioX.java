package com.robato.diagnosticos.domain.composite;

import java.math.BigDecimal;

public class RaioX extends ExameSimples {
    public RaioX() {
        super("Raio-X", CategoriaExame.IMAGEM, new BigDecimal("120.00"));
    }
}