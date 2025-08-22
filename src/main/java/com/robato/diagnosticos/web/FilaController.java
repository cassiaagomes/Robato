package com.robato.diagnosticos.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.robato.diagnosticos.service.FilaService;

@Controller
public class FilaController {

    private final FilaService filaService;

    public FilaController(FilaService filaService) {
        this.filaService = filaService;
    }

    @PostMapping("/fila/adicionar")
    public String adicionarFila(@RequestParam Long pacienteId,
            @RequestParam String pacienteNome,
            @RequestParam String tipoExame,
            @RequestParam String prioridade) {
        filaService.adicionarExame(pacienteId, pacienteNome, tipoExame, prioridade);
        return "redirect:/fila?mensagemSucesso=Exame adicionado+à+fila";
    }

    @GetMapping("/fila")
    public String verFila(Model model,
            @RequestParam(required = false) String mensagemSucesso) {
        model.addAttribute("exames", filaService.listarExames());
        model.addAttribute("tamanho", filaService.tamanhoFila());
        model.addAttribute("mensagemSucesso", mensagemSucesso);
        return "fila";
    }
}
