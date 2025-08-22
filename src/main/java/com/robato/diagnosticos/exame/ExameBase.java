package com.robato.diagnosticos.exame;

import java.util.UUID;

public abstract class ExameBase implements ExameComponent {
    protected String id;
    protected String nome;
    protected double preco;

    protected ExameBase(String nome, double preco) {
        this.id = UUID.randomUUID().toString(); // gera identificador único
        this.nome = nome;
        this.preco = preco;
    }

    @Override
    public double calcularPreco() {
        return preco;
    }

    @Override
    public String obterDescricao() {
        return nome;
    }

    public String getId() {
        return id;
    }

    public double getPreco() {
        return preco;
    }

    public String getNome() {
        return nome;
    }
}
