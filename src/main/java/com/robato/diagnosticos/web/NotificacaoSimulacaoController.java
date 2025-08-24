package com.robato.diagnosticos.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.robato.diagnosticos.notificacao.AssuntoNotificacao;
import com.robato.diagnosticos.notificacao.NotificadorEmail;
import com.robato.diagnosticos.notificacao.NotificadorWhatsapp;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/notificacoes")
public class NotificacaoSimulacaoController {
    
    private final AssuntoNotificacao assuntoNotificacao;
    private final NotificadorEmail notificadorEmail;
    private final NotificadorWhatsapp notificadorWhatsapp;
    
    public NotificacaoSimulacaoController() {
        this.notificadorEmail = new NotificadorEmail();
        this.notificadorWhatsapp = new NotificadorWhatsapp();
        
        this.assuntoNotificacao = new AssuntoNotificacao();
        assuntoNotificacao.adicionar(notificadorEmail);
        assuntoNotificacao.adicionar(notificadorWhatsapp);
    }
    
    // CORRIGIDO: Mapeamento para a raiz /notificacoes
    @GetMapping
    public String telaSimulacao(Model model, HttpServletRequest request) {
        model.addAttribute("notificacaoRequest", new NotificacaoRequest());
        model.addAttribute("emails", notificadorEmail.getHistorico());
        model.addAttribute("whatsapps", notificadorWhatsapp.getHistorico());
        model.addAttribute("currentUri", request.getRequestURI());
        return "notificacoes"; // ← Deve retornar o nome do template SEM extensão
    }
    
    // CORRIGIDO: Redirecionamentos
    @PostMapping("/enviar")
    public String enviarNotificacao(@ModelAttribute NotificacaoRequest request) {
        assuntoNotificacao.notificarTodos(request.getMensagem(), request.getDestino());
        return "redirect:/notificacoes"; // ← Redirect para a raiz
    }
    
    @PostMapping("/enviar-email")
    public String enviarApenasEmail(@ModelAttribute NotificacaoRequest request) {
        notificadorEmail.atualizar(request.getMensagem(), request.getDestino());
        return "redirect:/notificacoes"; // ← Redirect para a raiz
    }
    
    @PostMapping("/enviar-whatsapp")
    public String enviarApenasWhatsapp(@ModelAttribute NotificacaoRequest request) {
        notificadorWhatsapp.atualizar(request.getMensagem(), request.getDestino());
        return "redirect:/notificacoes"; // ← Redirect para a raiz
    }
    
    @PostMapping("/limpar")
    public String limparHistorico() {
        notificadorEmail.limparHistorico();
        notificadorWhatsapp.limparHistorico();
        return "redirect:/notificacoes"; // ← Redirect para a raiz
    }
    
    public static class NotificacaoRequest {
        private String destino;
        private String mensagem;
        
        public String getDestino() { return destino; }
        public void setDestino(String destino) { this.destino = destino; }
        
        public String getMensagem() { return mensagem; }
        public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    }
}