package com.robato.diagnosticos.prioridade;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class FilaDePrioridade {
    public static class Item {
        private String pacienteNome;
        private String tipoExame;
        private String convenio;
        private EstrategiaPrioridade prioridade; // <-- nova

        public Item(String pacienteNome, String tipoExame, String convenio, EstrategiaPrioridade prioridade) {
            this.pacienteNome = pacienteNome;
            this.tipoExame = tipoExame;
            this.convenio = convenio;
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
        
        public String getConvenio() {
            return convenio;
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

   public void add(String tipoExame, String paciente, String convenio, EstrategiaPrioridade prioridade) {
        fila.add(new Item(paciente, tipoExame, convenio, prioridade));
    }
    

    public Item proximo() {
        return fila.poll();
    }

    public int tamanho() {
        return fila.size();
    }

    public List<Item> listarExames() {
        PriorityQueue<Item> copiaFila = new PriorityQueue<>(fila);
        List<Item> listaOrdenada = new ArrayList<>();
        while (!copiaFila.isEmpty()) {
            listaOrdenada.add(copiaFila.poll());
        }
        return listaOrdenada;
    }
}
