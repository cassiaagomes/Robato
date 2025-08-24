package com.robato.diagnosticos.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.robato.diagnosticos.domain.Paciente;

@Service
public class PacienteService {

    private final Map<Long, Paciente> pacientes = new LinkedHashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    public PacienteService() {
        // Inicializa com 3 pacientes de exemplo
        inicializarDadosExemplo();
    }

    public List<Paciente> listarPacientes() {
        return new ArrayList<>(pacientes.values()); 
    }

    public Paciente adicionarPaciente(Paciente paciente) {
        long id = contador.getAndIncrement();
        paciente.setId(id);
        pacientes.put(id, paciente);
        return paciente;
    }

    public Paciente buscarPorNome(String nome) {
        return pacientes.values().stream()
                .filter(paciente -> paciente.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    public Paciente buscarPorId(Long id) {
        return pacientes.get(id);
    }

    public Paciente atualizarPaciente(Paciente pacienteAtualizado) {
        if (pacienteAtualizado.getId() == null || !pacientes.containsKey(pacienteAtualizado.getId())) {
            throw new IllegalArgumentException("Paciente com ID inválido ou não encontrado.");
        }
        pacientes.put(pacienteAtualizado.getId(), pacienteAtualizado);
        return pacienteAtualizado;
    }

    public Paciente excluirPaciente(Long id) {
        return pacientes.remove(id);
    }

    public boolean existePaciente(Long id) {
        return pacientes.containsKey(id);
    }

    public boolean existePorNome(String nome) {
        return pacientes.values().stream()
                .anyMatch(paciente -> paciente.getNome().equalsIgnoreCase(nome));
    }


    public List<Paciente> importarCsv(MultipartFile arquivo) throws Exception {
        List<Paciente> pacientesImportados = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            String linha;
            boolean primeiraLinha = true;
            
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; 
                }
                
                String[] dados = linha.split(",");
                if (dados.length >= 1) {
                    String nome = dados[0].trim();
                    String email = dados.length > 1 ? dados[1].trim() : "";
                    String telefone = dados.length > 2 ? dados[2].trim() : "";
                    String convenio = dados.length > 3 ? dados[3].trim() : "";
                    
                    if (!existePorNome(nome)) {
                        Paciente paciente = new Paciente();
                        paciente.setId(contador.getAndIncrement());
                        paciente.setNome(nome);
                        paciente.setEmail(email);
                        paciente.setTelefone(telefone);
                        paciente.setConvenio(convenio);
                        paciente.setStatus("ATIVO");
                        
                        pacientes.put(paciente.getId(), paciente);
                        pacientesImportados.add(paciente);
                    }
                }
            }
        }
        
        return pacientesImportados;
    }

    private void inicializarDadosExemplo() {
        if (pacientes.isEmpty()) {
            adicionarPaciente(new Paciente(null, "João Silva", "joao@email.com", "11999999999", "Unimed", "ATIVO"));
            adicionarPaciente(new Paciente(null, "Maria Santos", "maria@email.com", "11888888888", "Amil", "ATIVO"));
            adicionarPaciente(new Paciente(null, "Pedro Costa", "pedro@email.com", "11777777777", "Bradesco Saúde", "ATIVO"));
        }
    }
}