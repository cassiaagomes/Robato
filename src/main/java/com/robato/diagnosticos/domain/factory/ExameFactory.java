package com.robato.diagnosticos.domain.factory;

import com.robato.diagnosticos.domain.composite.ExameSimples;
import com.robato.diagnosticos.domain.composite.Glicose;
import com.robato.diagnosticos.domain.composite.HemogramaCompleto;
import com.robato.diagnosticos.domain.composite.RaioX;
import com.robato.diagnosticos.domain.composite.Ureia;

/**
 * Padrão Factory: Centraliza a criação de objetos de exame.
 * Atua como um catálogo de todos os tipos de exames conhecidos pelo sistema,
 * fornecendo instâncias "modelo" com seus preços e categorias corretos.
 */
public class ExameFactory {

    public static ExameSimples criar(String nomeExame) {
        if (nomeExame == null || nomeExame.isBlank()) {
            throw new IllegalArgumentException("O nome do exame não pode ser nulo ou vazio.");
        }

        switch (nomeExame.toUpperCase().trim()) {
            case "HEMOGRAMA COMPLETO":
                return new HemogramaCompleto();
            case "GLICOSE":
                return new Glicose();
            case "UREIA":
                return new Ureia();
            case "RAIO-X":
                return new RaioX();
            // Adicione outros exames aqui conforme necessário
            default:
                throw new IllegalArgumentException("Tipo de exame desconhecido ou não suportado pela fábrica: " + nomeExame);
        }
    }
}