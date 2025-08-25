package com.robato.diagnosticos.web.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.robato.diagnosticos.domain.repository.MedicoRepository;
import com.robato.diagnosticos.domain.repository.PacienteRepository;
import com.robato.diagnosticos.domain.service.FilaDePrioridadeExames;

@Controller
@RequestMapping("/")
public class DashboardController {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final FilaDePrioridadeExames filaDePrioridade;

    public DashboardController(
        PacienteRepository pacienteRepository,
        MedicoRepository medicoRepository,
        FilaDePrioridadeExames filaDePrioridade
    ) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.filaDePrioridade = filaDePrioridade;
    }

    @GetMapping
    public String dashboard(Model model) {
        Map<String, Object> stats = new HashMap<>();
        
        // Coleta os dados das fontes corretas na nova arquitetura
        stats.put("pacientesNaFila", filaDePrioridade.tamanho());
        stats.put("totalPacientes", pacienteRepository.listarTodos().size());
        stats.put("medicosAtivos", medicoRepository.listarTodos().size());
        // A métrica de "Laudos Gerados" exigiria um repositório de laudos.
        // Por enquanto, usaremos um valor estático como placeholder.
        stats.put("laudosNoMes", 78);

        model.addAttribute("stats", stats);
        return "dashboard";
    }
}