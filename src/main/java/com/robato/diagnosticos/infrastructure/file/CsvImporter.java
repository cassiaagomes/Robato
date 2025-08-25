package com.robato.diagnosticos.infrastructure.file;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.robato.diagnosticos.domain.model.Medico;
import com.robato.diagnosticos.domain.model.Paciente;
import com.robato.diagnosticos.domain.repository.MedicoRepository;
import com.robato.diagnosticos.domain.repository.PacienteRepository;

@Component
public class CsvImporter {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public CsvImporter(PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    public void importarPacientes(MultipartFile arquivo) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            String linha;
            br.readLine(); // Pula cabeçalho

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 1) {
                    String nome = dados[0].trim();
                    String email = dados.length > 1 ? dados[1].trim() : "";
                    String telefone = dados.length > 2 ? dados[2].trim() : "";
                    String convenio = dados.length > 3 ? dados[3].trim() : "";
                    
                    if (pacienteRepository.buscarPorNome(nome).isEmpty()) {
                        pacienteRepository.salvar(new Paciente(null, nome, email, telefone, convenio, null));
                    }
                }
            }
        }
    }

    public void importarMedicos(MultipartFile arquivo) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            String linha;
            br.readLine(); // Pula cabeçalho

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 2) {
                    String nome = dados[0].trim();
                    String crm = dados[1].trim();
                    
                    if (!medicoRepository.existePorCrm(crm)) {
                        medicoRepository.salvar(new Medico(null, nome, crm));
                    }
                }
            }
        }
    }
}
