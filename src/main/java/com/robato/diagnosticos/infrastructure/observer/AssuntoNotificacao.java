package com.robato.diagnosticos.infrastructure.observer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AssuntoNotificacao {

    private final List<Notificador> observadores = new ArrayList<>();

    public void adicionar(Notificador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void remover(Notificador observador) {
        observadores.remove(observador);
    }

    public void notificarObservadores(String mensagem, String destinatario) {
        if (destinatario == null || destinatario.isBlank()) {
            return;
        }
        for (Notificador obs : observadores) {
            try {
                obs.enviar(mensagem, destinatario);
            } catch (Exception e) {
                System.err.println("Falha ao enviar notificação via " + obs.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }
}