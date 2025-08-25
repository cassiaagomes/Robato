package com.robato.diagnosticos.infrastructure.observer;

public interface Notificador {
    void enviar(String mensagem, String destinatario);
}
