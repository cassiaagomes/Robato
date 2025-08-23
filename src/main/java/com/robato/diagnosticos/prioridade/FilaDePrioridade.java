package com.robato.diagnosticos.prioridade;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class FilaDePrioridade {
    public static class Item {
        private String pacienteNome;
        private String tipoExame;
        private EstrategiaPrioridade prioridade; // <-- nova

        public Item(String pacienteNome, String tipoExame, EstrategiaPrioridade prioridade) {
            this.pacienteNome = pacienteNome;
            this.tipoExame = tipoExame;
            this.prioridade = prioridade;
        }

        public String getTipoExame() {
            return tipoExame;
        }

        public String getPaciente() {
            return pacienteNome;
        }

        public EstrategiaPrioridade getPrioridade() {
            return prioridade;
        }

        public String getPrioridadeComoString() {
            if (this.prioridade instanceof EstrategiaPrioridadeUrgente) {
                return "URGENTE";
            }
            if (this.prioridade instanceof EstrategiaPrioridadePoucoUrgente) { 
                return "POUCO_URGENTE";
            }
            return "ROTINA";
        }
    }

    private EstrategiaPrioridade estrategia;
    private final PriorityQueue<Item> fila = new PriorityQueue<Item>(
            Comparator.comparingInt((Item i) -> i.getPrioridade().determinarPrioridade(i.getTipoExame()))
                    .reversed() 
    );

    public FilaDePrioridade(EstrategiaPrioridade e) {
        this.estrategia = e;
    }

    public void setEstrategia(EstrategiaPrioridade e) {
        this.estrategia = e;
    }

   public void add(String tipoExame, String paciente, EstrategiaPrioridade prioridade) {
        fila.add(new Item(paciente, tipoExame, prioridade));
    }
    

    public Item proximo() {
        return fila.poll();
    }

    public int tamanho() {
        return fila.size();
    }

    public List<Item> listarExames() {
        return new ArrayList<>(fila);
    }
}
