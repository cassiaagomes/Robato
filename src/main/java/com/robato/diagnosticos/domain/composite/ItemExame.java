package com.robato.diagnosticos.domain.composite;

import java.math.BigDecimal;
import java.util.List;

import com.robato.diagnosticos.domain.visitor.ExameVisitor;

public interface ItemExame {
    long getId();
    String getNome();
    CategoriaExame getCategoria();
    BigDecimal getPrecoBase();
    
    void accept(ExameVisitor visitor);

    default void adicionar(ItemExame item) {
        throw new UnsupportedOperationException("Esta operação não é suportada por este tipo de exame.");
    }

    default List<ItemExame> getItens() {
        throw new UnsupportedOperationException("Esta operação não é suportada por este tipo de exame.");
    }
}
