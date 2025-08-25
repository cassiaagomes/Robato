package com.robato.diagnosticos.domain.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.robato.diagnosticos.domain.service.GeradorIDSequencial;
import com.robato.diagnosticos.domain.visitor.ExameVisitor;

public class PacoteExames implements ItemExame {
    private final long id;
    private final String nome;
    private final List<ItemExame> itens = new ArrayList<>();

    public PacoteExames(String nome) {
        this.id = GeradorIDSequencial.getInstance().proximoId();
        this.nome = nome;
    }

    @Override
    public long getId() { 
        return id; 
    }
    
    @Override
    public String getNome() { 
        return nome; 
    }

    @Override
    public CategoriaExame getCategoria() {
        return CategoriaExame.LABORATORIAL;
    }

    @Override
    public BigDecimal getPrecoBase() {
        return itens.stream()
                .map(ItemExame::getPrecoBase)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void accept(ExameVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void adicionar(ItemExame item) {
        if (item.getCategoria() != CategoriaExame.LABORATORIAL) {
            throw new IllegalArgumentException("Apenas exames da categoria LABORATORIAL podem ser adicionados a um pacote.");
        }
        this.itens.add(item);
    }

    @Override
    public List<ItemExame> getItens() {
        return new ArrayList<>(itens);
    }
}
