package com.robato.diagnosticos.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.robato.diagnosticos.domain.Paciente;
import com.robato.diagnosticos.service.PacienteService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PacienteViewController {

    private final PacienteService pacienteService;

    public PacienteViewController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/pacientes/salvar")
    public String salvarPaciente(@RequestParam String nome,
            @RequestParam String email,
            @RequestParam String telefone,
            @RequestParam String convenio) {
        Paciente novoPaciente = new Paciente(null, nome, email, telefone, convenio, "ATIVO");
        pacienteService.adicionarPaciente(novoPaciente);
        return "redirect:/pacientes";
    }

    @GetMapping("/pacientes")
    public String listarPacientes(Model model, HttpServletRequest request) {
        model.addAttribute("pacientes", pacienteService.listarPacientes());
        model.addAttribute("currentUri", request.getRequestURI());
        List<String> tipos = List.of("HEMOGRAMA", "UREIA", "GLICOSE", "RAIO-X", "RESSONANCIA");
        model.addAttribute("tipos", tipos);

        return "pacientes";
    }

    @GetMapping("/pacientes/editar/{id}")
    public String editarPaciente(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return "redirect:/pacientes";
        }
        model.addAttribute("paciente", paciente);
        return "editar-paciente";
    }

    @PostMapping("/pacientes/atualizar")
    public String atualizarPaciente(@RequestParam Long id,
            @RequestParam String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String convenio) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente != null) {
            paciente.setNome(nome);
            paciente.setEmail(email);
            paciente.setTelefone(telefone);
            paciente.setConvenio(convenio);
            pacienteService.atualizarPaciente(paciente);
        }
        return "redirect:/pacientes";
    }

    @PostMapping("/pacientes/excluir/{id}")
    public String excluirPaciente(@PathVariable Long id) {
        pacienteService.excluirPaciente(id);
        return "redirect:/pacientes";
    }

}
