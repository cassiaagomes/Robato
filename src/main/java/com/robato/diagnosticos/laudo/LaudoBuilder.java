package com.robato.diagnosticos.laudo;

import java.time.LocalDate;
import java.util.List;

import com.robato.diagnosticos.web.dto.ResultadoExameItem;

public class LaudoBuilder {
    private final LaudoCompleto l = new LaudoCompleto();

    public LaudoBuilder numero(int n) {
        l.setNumeroExame(n);
        return this;
    }

    public LaudoBuilder paciente(String nome, String convenio) {
        l.setPaciente(nome);
        l.setConvenio(convenio);
        return this;
    }

    public LaudoBuilder medico(String solicitante, String responsavel, String crm) {
        l.setMedicoSolicitante(solicitante);
        l.setMedicoResponsavel(responsavel);
        l.setCrmResponsavel(crm);
        return this;
    }

    
    public LaudoBuilder resultados(List<ResultadoExameItem> resultados) {
        l.setResultados(resultados);
        return this;
    }
    

    public LaudoBuilder observacoes(String observacoes) {
        l.setObservacoes(observacoes);
        return this;
    }


    public LaudoBuilder data(LocalDate dt) {
        l.setData(dt);
        return this;
    }

    public LaudoCompleto build() {
        return l;
    }
}
