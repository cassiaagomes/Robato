package com.robato.diagnosticos.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.robato.diagnosticos.domain.model.Medico;
import com.robato.diagnosticos.domain.repository.MedicoRepository;
import com.robato.diagnosticos.infrastructure.file.CsvImporter;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoRepository medicoRepository;
    private final CsvImporter csvImporter;

    public MedicoController(MedicoRepository medicoRepository, CsvImporter csvImporter) {
        this.medicoRepository = medicoRepository;
        this.csvImporter = csvImporter;
    }

    @GetMapping
    public String listarMedicos(Model model) {
        model.addAttribute("medicos", medicoRepository.listarTodos());
        return "medicos";
    }

    @PostMapping("/salvar")
    public String salvarMedico(@ModelAttribute Medico medico, RedirectAttributes redirectAttributes) {
        medicoRepository.salvar(medico);
        redirectAttributes.addFlashAttribute("sucesso", "Médico salvo com sucesso!");
        return "redirect:/medicos";
    }
    

    @PostMapping("/importar-csv")
    public String importarMedicos(@RequestParam("arquivo") MultipartFile arquivo, RedirectAttributes redirectAttributes) {
        try {
            csvImporter.importarMedicos(arquivo);
            redirectAttributes.addFlashAttribute("sucesso", "Médicos importados com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Falha ao importar CSV: " + e.getMessage());
        }
        return "redirect:/medicos";
    }
}
