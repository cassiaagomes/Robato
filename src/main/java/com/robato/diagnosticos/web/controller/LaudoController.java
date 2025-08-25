package com.robato.diagnosticos.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.robato.diagnosticos.application.dto.LaudoDTO;
import com.robato.diagnosticos.application.facade.ProcessamentoLaudoFacade; // Import para as strategies
import com.robato.diagnosticos.domain.composite.ItemExame;
import com.robato.diagnosticos.domain.model.Medico;
import com.robato.diagnosticos.domain.model.Paciente;
import com.robato.diagnosticos.domain.repository.MedicoRepository;
import com.robato.diagnosticos.domain.repository.PacienteRepository;
import com.robato.diagnosticos.domain.service.FilaDePrioridadeExames;
import com.robato.diagnosticos.infrastructure.strategy.GeradorFormato;
import com.robato.diagnosticos.infrastructure.strategy.GeradorHTML;
import com.robato.diagnosticos.infrastructure.strategy.GeradorPDF;
import com.robato.diagnosticos.infrastructure.strategy.GeradorTXT;

@Controller
@RequestMapping("/laudos")
public class LaudoController {

    private final ProcessamentoLaudoFacade processamentoFacade;
    private final FilaDePrioridadeExames fila;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;


    public LaudoController(ProcessamentoLaudoFacade facade, FilaDePrioridadeExames fila, PacienteRepository pRepo, MedicoRepository mRepo) {
        this.processamentoFacade = facade;
        this.fila = fila;
        this.pacienteRepository = pRepo;
        this.medicoRepository = mRepo;
    }

    @PostMapping("/processar")
    public ResponseEntity<byte[]> processarProximoDaFila(@RequestParam String formato) {
        FilaDePrioridadeExames.ItemFila itemFila = fila.proximo();
        if (itemFila == null) {
            // Em uma aplicação real, retornaria um erro mais amigável
            throw new RuntimeException("A fila de exames está vazia.");
        }

        ItemExame exame = itemFila.exame();
        
        // Simulação: busca o primeiro paciente e médicos para o laudo
        Paciente paciente = pacienteRepository.listarTodos().get(0);
        Medico solicitante = medicoRepository.listarTodos().get(0);
        Medico responsavel = medicoRepository.listarTodos().get(1);

        // A fachada orquestra todo o processamento complexo
        LaudoDTO laudo = processamentoFacade.processarEGerarLaudo(paciente, solicitante, responsavel, exame);

        // O Strategy é selecionado para gerar o formato de saída
        GeradorFormato strategy = switch (formato.toLowerCase()) {
            case "pdf" -> new GeradorPDF();
            case "html" -> new GeradorHTML();
            default -> new GeradorTXT();
        };

        byte[] arquivoBytes = strategy.gerar(laudo);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, strategy.getMimeType());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=laudo-" + laudo.id() + "." + formato.toLowerCase());

        return new ResponseEntity<>(arquivoBytes, headers, HttpStatus.OK);
    }
}
