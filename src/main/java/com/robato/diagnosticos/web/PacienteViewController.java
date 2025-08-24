package com.robato.diagnosticos.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            @RequestParam String convenio,
            RedirectAttributes redirectAttributes) {
        Paciente novoPaciente = new Paciente(null, nome, email, telefone, convenio, "ATIVO");
        pacienteService.adicionarPaciente(novoPaciente);
        redirectAttributes.addAttribute("adicionado", true);
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
            @RequestParam(required = false) String convenio,
            RedirectAttributes redirectAttributes) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente != null) {
            paciente.setNome(nome);
            paciente.setEmail(email);
            paciente.setTelefone(telefone);
            paciente.setConvenio(convenio);
            pacienteService.atualizarPaciente(paciente);
            redirectAttributes.addAttribute("atualizado", true);
        }
        return "redirect:/pacientes";
    }

    @PostMapping("/pacientes/excluir/{id}")
    public String excluirPaciente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pacienteService.excluirPaciente(id);
        redirectAttributes.addAttribute("excluido", true);
        return "redirect:/pacientes";
    }

    // Método para importar CSV (NOVO)
    @PostMapping("/pacientes/importar-csv")
    public String importarCsv(@RequestParam("arquivo") MultipartFile arquivo,
            RedirectAttributes redirectAttributes) {
        try {
            pacienteService.importarCsv(arquivo);
            redirectAttributes.addAttribute("importado", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao importar: " + e.getMessage());
        }
        return "redirect:/pacientes";
    }
}