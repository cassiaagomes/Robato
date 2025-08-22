package com.robato.diagnosticos.infra;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.robato.diagnosticos.domain.Medico;
import com.robato.diagnosticos.domain.Paciente;

import jakarta.annotation.PostConstruct;

@Component
public class CsvLoader {
    private final Map<String, Paciente> pacientes = new HashMap<>();
    private final Map<String, Medico> medicos = new HashMap<>();

    @PostConstruct
    public void init() {
        carregarPacientes("data/pacientes.csv");
        carregarMedicos("data/medicos.csv");
    }

    private void carregarPacientes(String path) {
    try {
        var res = new ClassPathResource(path);
        if (!res.exists()) {
            System.err.println("Arquivo de pacientes não encontrado em: " + path);
            return;
        }

        try (var br = new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] p = line.split(",", -1);

                if (p.length < 6) {
                    System.err.println("Linha mal formatada no CSV de pacientes: " + line);
                    continue; 
                }
                Paciente pac = new Paciente(
                    Long.parseLong(p[0].trim()),      
                    p[1].trim(),                      
                    p[2].trim(),                      
                    p[3].trim(),                      
                    p[4].trim(),                      
                    p[5].trim()                       
                );
                
                pacientes.put(pac.getNome(), pac);
            }
        }
    } catch (Exception e) {
        System.err.println("Erro fatal ao carregar pacientes do CSV.");
        e.printStackTrace();
    }
}

    private void carregarMedicos(String path) {
        try {
            var res = new ClassPathResource(path);
            if (!res.exists()) return;
            try (var br = new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty() || line.startsWith("#")) continue;
                    String[] p = line.split(",", -1);
                    Medico m = new Medico(p[0].trim(), p[1].trim());
                    medicos.put(m.getNome(), m);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public Paciente obterPaciente(String nome) { return pacientes.get(nome); }
    public Medico obterMedico(String nome) { return medicos.get(nome); }
    public Map<String,Paciente> listarPacientes(){ return Map.copyOf(pacientes); }
    public Map<String,Medico> listarMedicos(){ return Map.copyOf(medicos); }
}
