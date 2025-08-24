package com.robato.diagnosticos.notificacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
@Component
public class AssuntoNotificacao {
    private final List<ObservadorNotificacao> obs = new ArrayList<>();

    public void adicionar(ObservadorNotificacao o) {
        obs.add(o);
    }

    public void remover(ObservadorNotificacao o) {
        obs.remove(o);
    }

    public void notificarTodos(String mensagem, String destino) {
        for (var o : obs)
            o.atualizar(mensagem, destino);
    }
}
