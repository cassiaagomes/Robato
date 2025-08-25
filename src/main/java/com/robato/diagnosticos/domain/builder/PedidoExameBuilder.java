package com.robato.diagnosticos.domain.builder;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import com.robato.diagnosticos.domain.composite.CategoriaExame;
import com.robato.diagnosticos.domain.composite.ExameSimples;
import com.robato.diagnosticos.domain.composite.ItemExame;
import com.robato.diagnosticos.domain.composite.PacoteExames;

public class PedidoExameBuilder {

    private final PacoteExames pedido;

    public PedidoExameBuilder(String nomePedido) {
        if (nomePedido == null || nomePedido.isBlank()) {
            throw new IllegalArgumentException("O nome do pedido não pode ser vazio.");
        }
        this.pedido = new PacoteExames(nomePedido);
    }

    public PedidoExameBuilder adicionarExameSimples(String nome, CategoriaExame categoria, String preco, Map<String, Object> atributos) {
        Objects.requireNonNull(nome, "Nome do exame não pode ser nulo.");
        Objects.requireNonNull(categoria, "Categoria do exame não pode ser nula.");
        Objects.requireNonNull(preco, "Preço do exame não pode ser nulo.");

        if (nome.isBlank()) {
            throw new IllegalArgumentException("O nome do exame não pode ser vazio.");
        }
        
        BigDecimal precoDecimal;
        try {
            precoDecimal = new BigDecimal(preco);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O preço do exame deve ser um número válido.");
        }

        if (precoDecimal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço do exame não pode ser negativo.");
        }

        ExameSimples exame = new ExameSimples(nome, categoria, precoDecimal);
        if (atributos != null) {
            atributos.forEach(exame.getAtributos()::put);
        }
        
        this.pedido.adicionar(exame);
        return this;
    }

    public ItemExame build() {
        if (pedido.getItens().isEmpty()) {
            throw new IllegalStateException("Não é possível construir um pedido de exame vazio.");
        }
        return this.pedido;
    }
}
