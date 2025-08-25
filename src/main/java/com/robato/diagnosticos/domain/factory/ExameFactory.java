package com.robato.diagnosticos.domain.factory;


import java.util.List;

import com.robato.diagnosticos.domain.composite.ExameSimples;
import com.robato.diagnosticos.domain.composite.Glicose;
import com.robato.diagnosticos.domain.composite.HemogramaCompleto;
import com.robato.diagnosticos.domain.composite.RaioX;
import com.robato.diagnosticos.domain.composite.Ureia;

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
            default:
                throw new IllegalArgumentException("Tipo de exame desconhecido ou não suportado: " + nomeExame);
        }
    }

    public static List<String> listarTipos() {
        return List.of(
            "Hemograma Completo",
            "Glicose",
            "Ureia",
            "Raio-X"
        );
    }
}