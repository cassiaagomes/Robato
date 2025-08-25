package com.robato.diagnosticos.web.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.robato.diagnosticos.domain.factory.ExameFactory;
import com.robato.diagnosticos.domain.model.Paciente;
import com.robato.diagnosticos.domain.repository.PacienteRepository;
import com.robato.diagnosticos.infrastructure.file.CsvImporter;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;
    private final CsvImporter csvImporter;

    public PacienteController(PacienteRepository pacienteRepository, CsvImporter csvImporter) {
        this.pacienteRepository = pacienteRepository;
        this.csvImporter = csvImporter;
    }

    @GetMapping
    public String listarPacientes(Model model) {
        model.addAttribute("pacientes", pacienteRepository.listarTodos());
        model.addAttribute("tipos", ExameFactory.listarTipos()); 
        return "pacientes";
    }

    @PostMapping("/salvar")
    public String salvarPaciente(@ModelAttribute Paciente paciente, RedirectAttributes redirectAttributes) {
        pacienteRepository.salvar(paciente);
        redirectAttributes.addFlashAttribute("sucesso", "Paciente salvo com sucesso!");
        return "redirect:/pacientes";
    }
    


    @PostMapping("/importar-csv")
    public String importarPacientes(@RequestParam("arquivo") MultipartFile arquivo, RedirectAttributes redirectAttributes) {
        try {
            csvImporter.importarPacientes(arquivo);
            redirectAttributes.addFlashAttribute("sucesso", "Pacientes importados com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Falha ao importar CSV: " + e.getMessage());
        }
        return "redirect:/pacientes";
    }
     public static class PacienteDTO {
        private Long id;
        private String nome;
        private String email;
        private String telefone;
        private String convenio;
        private LocalDate dataNascimento;
        
        // Getters e Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        public String getConvenio() { return convenio; }
        public void setConvenio(String convenio) { this.convenio = convenio; }
        public LocalDate getDataNascimento() { return dataNascimento; }
        public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    }
}