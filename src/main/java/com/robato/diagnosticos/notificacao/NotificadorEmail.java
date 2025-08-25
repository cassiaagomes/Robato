package com.robato.diagnosticos.notificacao;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificadorEmail implements ObservadorNotificacao {

    private final JavaMailSender mailSender;

    public NotificadorEmail(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void atualizar(String mensagem, String destino) {
        if (destino == null || !destino.contains("@")) {
            System.err.println("[EMAIL] Falha ao enviar: destino '" + destino + "' é inválido.");
            return;
        }

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(destino);
            mail.setSubject("Seu Laudo de Exame está Pronto!");
            mail.setText(mensagem);

            mailSender.send(mail);
            System.out.println("[EMAIL] Notificação enviada com sucesso para: " + destino);

        } catch (Exception e) {
            System.err.println("[EMAIL] Erro ao enviar notificação para " + destino + ": " + e.getMessage());
        }
    }
}