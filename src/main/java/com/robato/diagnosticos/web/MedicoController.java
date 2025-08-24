package com.robato.diagnosticos.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.robato.diagnosticos.domain.Medico;
import com.robato.diagnosticos.service.MedicoService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/medicos")
public class MedicoController {
    
    private final MedicoService medicoService;
    
    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
        // Inicializa alguns dados de exemplo
        medicoService.salvar(new Medico(null, "Dr. João Silva", "12345-SP", "Cardiologia", "joao@email.com", "11999999999"));
        medicoService.salvar(new Medico(null, "Dra. Maria Santos", "54321-MG", "Pediatria", "maria@email.com", "11888888888"));
    }
    
    @GetMapping
    public String listarMedicos(Model model, HttpServletRequest request) {
        model.addAttribute("medicos", medicoService.listarTodos());
        model.addAttribute("currentUri", request.getRequestURI()); // ← ADICIONE ESTA LINHA
        return "medicos";
    }
    
    @PostMapping("/salvar")
    public String salvarMedico(@ModelAttribute Medico medico, RedirectAttributes redirectAttributes) {
        medicoService.salvar(medico);
        redirectAttributes.addFlashAttribute("sucesso", "Médico adicionado com sucesso!");
        return "redirect:/medicos";
    }
    
    @PostMapping("/atualizar")
    public String atualizarMedico(@ModelAttribute Medico medico, RedirectAttributes redirectAttributes) {
        medicoService.salvar(medico);
        redirectAttributes.addFlashAttribute("sucesso", "Médico atualizado com sucesso!");
        return "redirect:/medicos";
    }
    
    @PostMapping("/excluir/{id}")
    public String excluirMedico(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        medicoService.excluir(id);
        redirectAttributes.addFlashAttribute("sucesso", "Médico excluído com sucesso!");
        return "redirect:/medicos";
    }
    
    @PostMapping("/importar-csv")
    public String importarCsv(@RequestParam("arquivo") MultipartFile arquivo, 
                             RedirectAttributes redirectAttributes) {
        try {
            medicoService.importarCsv(arquivo);
            redirectAttributes.addFlashAttribute("sucesso", "CSV importado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao importar: " + e.getMessage());
        }
        return "redirect:/medicos";
    }
}