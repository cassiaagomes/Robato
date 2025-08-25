package com.robato.diagnosticos.application.facade;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.robato.diagnosticos.application.dto.LaudoDTO;
import com.robato.diagnosticos.domain.composite.ItemExame;
import com.robato.diagnosticos.domain.model.Medico;
import com.robato.diagnosticos.domain.model.Paciente;
import com.robato.diagnosticos.domain.service.GeradorIDSequencial;
import com.robato.diagnosticos.domain.visitor.GeradorLaudoVisitor;
import com.robato.diagnosticos.domain.visitor.ValidadorVisitor;
import com.robato.diagnosticos.infrastructure.observer.AssuntoNotificacao;

@Service
public class ProcessamentoLaudoFacade {

    private final AssuntoNotificacao assuntoNotificacao;

    public ProcessamentoLaudoFacade(AssuntoNotificacao assuntoNotificacao) {
        this.assuntoNotificacao = assuntoNotificacao;
    }

    public LaudoDTO processarEGerarLaudo(Paciente paciente, Medico solicitante, Medico responsavel, ItemExame exame) {
        ValidadorVisitor validador = new ValidadorVisitor();
        exame.accept(validador);

        if (!validador.isValido()) {
            throw new IllegalStateException("Validação do exame falhou: " + String.join(", ", validador.getErros()));
        }

        GeradorLaudoVisitor geradorCorpo = new GeradorLaudoVisitor();
        exame.accept(geradorCorpo);
        String corpoDoLaudo = geradorCorpo.getCorpoDoLaudo();

        long idLaudo = GeradorIDSequencial.getInstance().proximoId();
        
        String cabecalho = construirCabecalho(idLaudo, paciente, solicitante);
        String rodape = construirRodape(responsavel);
        
        LaudoDTO laudo = new LaudoDTO(
            idLaudo,
            paciente,
            solicitante,
            responsavel,
            LocalDate.now(),
            cabecalho,
            corpoDoLaudo,
            rodape,
            exame
        );

        String mensagem = "Seu laudo para o exame '" + exame.getNome() + "' está pronto.";
        assuntoNotificacao.notificarObservadores(mensagem, paciente.getEmail());

        return laudo;
    }

    private String construirCabecalho(long id, Paciente paciente, Medico solicitante) {
        return String.format(
            "Laudo Nº: %d | Paciente: %s | Convênio: %s | Médico Solicitante: %s",
            id,
            paciente.getNome(),
            paciente.getConvenio() != null ? paciente.getConvenio() : "Particular",
            solicitante.getNome()
        );
    }

    private String construirRodape(Medico responsavel) {
        return String.format(
            "Médico Responsável: %s | CRM: %s",
            responsavel.getNome(),
            responsavel.getCrm()
        );
    }
}