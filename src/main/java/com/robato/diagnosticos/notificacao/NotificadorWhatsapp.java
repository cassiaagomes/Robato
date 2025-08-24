package com.robato.diagnosticos.notificacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class NotificadorWhatsapp implements ObservadorNotificacao {
    private List<String> historico = new ArrayList<>();

    public void atualizar(String mensagem, String destino) {
        String notificacao = String.format(
                "[SIMULAÇÃO WHATSAPP] Para: %s - Mensagem: %s",
                destino, mensagem);

        historico.add(notificacao);
        System.out.println(notificacao);
    }

    public List<String> getHistorico() {
        return new ArrayList<>(historico);
    }

    public void limparHistorico() {
        historico.clear();
    }
}