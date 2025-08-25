package com.robato.diagnosticos.domain.composite;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.robato.diagnosticos.domain.service.GeradorIDSequencial;
import com.robato.diagnosticos.domain.visitor.ExameVisitor;

public class ExameSimples implements ItemExame {
    private final long id;
    private final String nome;
    private final CategoriaExame categoria;
    private final BigDecimal precoBase;
    private final Map<String, Object> atributos;

    public ExameSimples(String nome, CategoriaExame categoria, BigDecimal precoBase) {
        this.id = GeradorIDSequencial.getInstance().proximoId();
        this.nome = nome;
        this.categoria = categoria;
        this.precoBase = precoBase;
        this.atributos = new HashMap<>();
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
        return categoria; 
    }

    @Override
    public BigDecimal getPrecoBase() { 
        return precoBase; 
    }

    public Map<String, Object> getAtributos() { 
        return atributos; 
    }

    @Override
    public void accept(ExameVisitor visitor) {
        visitor.visit(this);
    }
}