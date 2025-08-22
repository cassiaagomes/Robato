package com.robato.diagnosticos.prioridade;
import java.util.Comparator; import java.util.PriorityQueue;
public class FilaDePrioridade {
    public static class Item{ public final String tipoExame; public final String paciente; public final int prioridade; public Item(String tipoExame,String paciente,int prioridade){this.tipoExame=tipoExame;this.paciente=paciente;this.prioridade=prioridade;} }
    private EstrategiaPrioridade estrategia; private final PriorityQueue<Item> fila = new PriorityQueue<>(Comparator.comparingInt(i->i.prioridade));
    public FilaDePrioridade(EstrategiaPrioridade e){ this.estrategia=e; }
    public void setEstrategia(EstrategiaPrioridade e){ this.estrategia=e; }
    public void adicionar(String tipoExame,String paciente){ int p = estrategia.determinarPrioridade(tipoExame); fila.add(new Item(tipoExame,paciente,p)); }
    public Item proximo(){ return fila.poll(); }
    public int tamanho(){ return fila.size(); }
}
