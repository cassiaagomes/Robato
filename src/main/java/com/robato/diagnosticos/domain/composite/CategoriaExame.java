package com.robato.diagnosticos.domain.composite;

public enum CategoriaExame {
    LABORATORIAL("Exame de laboratório"),
    IMAGEM("Exame de imagem");

    private final String descricao;

    CategoriaExame(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
