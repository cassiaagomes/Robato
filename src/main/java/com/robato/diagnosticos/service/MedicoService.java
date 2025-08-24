package com.robato.diagnosticos.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.robato.diagnosticos.domain.Medico;

@Service
public class MedicoService {
    
    private final Map<Long, Medico> medicos = new HashMap<>();
    private final AtomicLong contadorId = new AtomicLong(1);
    
    public List<Medico> listarTodos() {
        return new ArrayList<>(medicos.values());
    }
    
    public Medico salvar(Medico medico) {
        if (medico.getId() == null) {
            medico.setId(contadorId.getAndIncrement());
        }
        medicos.put(medico.getId(), medico);
        return medico;
    }
    
    public void excluir(Long id) {
        medicos.remove(id);
    }
    
    public Medico buscarPorId(Long id) {
        return medicos.get(id);
    }
    
    public boolean existePorCrm(String crm) {
        return medicos.values().stream()
                .anyMatch(medico -> medico.getCrm().equalsIgnoreCase(crm));
    }
    
    public List<Medico> importarCsv(MultipartFile arquivo) throws Exception {
        List<Medico> medicosImportados = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            String linha;
            boolean primeiraLinha = true;
            
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // Pula cabeçalho
                }
                
                String[] dados = linha.split(",");
                if (dados.length >= 2) {
                    String nome = dados[0].trim();
                    String crm = dados[1].trim();
                    String especialidade = dados.length > 2 ? dados[2].trim() : "";
                    String email = dados.length > 3 ? dados[3].trim() : "";
                    String telefone = dados.length > 4 ? dados[4].trim() : "";
                    
                    // Verifica se CRM já existe
                    if (!existePorCrm(crm)) {
                        Medico medico = new Medico();
                        medico.setId(contadorId.getAndIncrement());
                        medico.setNome(nome);
                        medico.setCrm(crm);
                        medico.setEspecialidade(especialidade);
                        medico.setEmail(email);
                        medico.setTelefone(telefone);
                        
                        medicos.put(medico.getId(), medico);
                        medicosImportados.add(medico);
                    }
                }
            }
        }
        
        return medicosImportados;
    }
    
    public void inicializarDadosExemplo() {
        if (medicos.isEmpty()) {
            salvar(new Medico(null, "Dr. João Silva", "12345-SP", "Cardiologia", "joao@email.com", "11999999999"));
            salvar(new Medico(null, "Dra. Maria Santos", "54321-MG", "Pediatria", "maria@email.com", "11888888888"));
            salvar(new Medico(null, "Dr. Pedro Costa", "67890-RJ", "Ortopedia", "pedro@email.com", "11777777777"));
        }
    }
}
