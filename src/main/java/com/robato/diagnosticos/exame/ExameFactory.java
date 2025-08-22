package com.robato.diagnosticos.exame;

public class ExameFactory {
    public static ExameComponent criar(TipoExame tipo) {
        return switch (tipo) {
            case HEMOGRAMA_AGRUPADO -> new HemogramaAgrupado();
            case RAIOX -> new RaioX();
            case RESSONANCIA -> new RessonanciaMagnetica();
            case HEMOGRAMA_COMPLETO -> new HemogramaCompleto();
            case GLICEMIA -> new Glicemia();
            case SOROLOGIA -> new Sorologia();
            case UREIA -> new Ureia();
            case CREATININA -> new Creatinina();
            case COLESTEROL_TOTAL -> new ColesterolTotal();
            case HDL -> new HDL();
            case LDL -> new LDL();
            case TRIGLICERIDEOS -> new Triglicerideos();
            case TGO_AST -> new TgoAst();
            case TGP_ALT -> new TgpAlt();
            case GAMA_GT -> new GamaGt();
            case BILIRRUBINA_TOTAL -> new BilirrubinaTotal();
            case SODIO -> new Sodio();
            case POTASSIO -> new Potassio();
            case CALCIO -> new Calcio();
            case FOSFORO -> new Fosforo();
            
            default -> throw new IllegalArgumentException("Tipo de exame desconhecido ou não implementado na fábrica: " + tipo);
        };
    }
}
