package com.robato.diagnosticos.exame;

public class ExameFactory {
    public static ExameComponent criar(TipoExame tipo) {
        return switch (tipo) {
            case HEMOGRAMA_AGRUPADO -> new HemogramaAgrupado();
            case RAIO_X -> new RaioX();
            case RESSONANCIA -> new RessonanciaMagnetica();
            case HEMOGRAMA_COMPLETO -> new HemogramaCompleto();
            case GLICOSE -> new Glicose();
            case SOROLOGIA -> new Sorologia();
            case UREIA -> new Ureia();
            case CREATININA -> new Creatinina();
            case HDL -> new HDL();
            case LDL -> new LDL();
            case TRIGLICERIDEOS -> new Triglicerideos();
            case TGO_AST -> new TgoAst();
            case TGP_ALT -> new TgpAlt();
            case SODIO -> new Sodio();
            case POTASSIO -> new Potassio();
            case HEMOGRAMA -> new Hemograma();
            
            default -> throw new IllegalArgumentException("Tipo de exame desconhecido ou não implementado na fábrica: " + tipo);
        };
    }
}
