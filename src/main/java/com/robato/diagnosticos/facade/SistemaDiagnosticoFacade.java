package com.robato.diagnosticos.facade;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.robato.diagnosticos.core.SequencialExameGenerator;
import com.robato.diagnosticos.desconto.Desconto;
import com.robato.diagnosticos.desconto.DescontoConvenio;
import com.robato.diagnosticos.desconto.DescontoIdoso;
import com.robato.diagnosticos.desconto.DescontoOutubroRosa;
import com.robato.diagnosticos.domain.Paciente;
import com.robato.diagnosticos.exame.ExameComponent;
import com.robato.diagnosticos.exame.ExameFactory;
import com.robato.diagnosticos.exame.HemogramaAgrupado;
import com.robato.diagnosticos.exame.TipoExame;
import com.robato.diagnosticos.infra.CsvLoader;
import com.robato.diagnosticos.laudo.FormatoLaudo;
import com.robato.diagnosticos.laudo.HtmlFormato;
import com.robato.diagnosticos.laudo.Laudo;
import com.robato.diagnosticos.laudo.LaudoBuilder;
import com.robato.diagnosticos.laudo.LaudoCompleto;
import com.robato.diagnosticos.laudo.PdfFormato;
import com.robato.diagnosticos.laudo.TextoFormato;
import com.robato.diagnosticos.notificacao.AssuntoNotificacao;
import com.robato.diagnosticos.notificacao.NotificadorEmail;
import com.robato.diagnosticos.notificacao.NotificadorWhatsapp;
import com.robato.diagnosticos.prioridade.EstrategiaPrioridadeRotina;
import com.robato.diagnosticos.prioridade.EstrategiaPrioridadeUrgente;
import com.robato.diagnosticos.prioridade.FilaDePrioridade;
import com.robato.diagnosticos.validacao.ValidadorCreatinina;
import com.robato.diagnosticos.validacao.ValidadorExame;
import com.robato.diagnosticos.validacao.ValidadorGlicose;
import com.robato.diagnosticos.validacao.ValidadorImplantesMetalicos;
import com.robato.diagnosticos.validacao.ValidadorPrecoPositivo;
import com.robato.diagnosticos.validacao.ValidadorRaioX;
import com.robato.diagnosticos.validacao.ValidadorRessonancia;
import com.robato.diagnosticos.web.dto.LaudoRequest;
import com.robato.diagnosticos.web.dto.ResultadoExameItem;
import com.robato.diagnosticos.web.dto.ValidacaoContexto;

@Service
public class SistemaDiagnosticoFacade {

    private final AssuntoNotificacao assunto = new AssuntoNotificacao();
    private final FilaDePrioridade fila = new FilaDePrioridade(new EstrategiaPrioridadeRotina());
    private final SequencialExameGenerator sequencial;
    private final CsvLoader csv;

    public SistemaDiagnosticoFacade(SequencialExameGenerator sequencial, CsvLoader csv) {
        this.sequencial = sequencial;
        this.csv = csv;
        assunto.adicionar(new NotificadorEmail());
        assunto.adicionar(new NotificadorWhatsapp());
    }

    public void validarExame(ExameComponent exame, ValidacaoContexto contexto) {
        ValidadorExame v1 = new ValidadorPrecoPositivo();
        ValidadorExame v2 = new ValidadorImplantesMetalicos();
        ValidadorExame v3 = new ValidadorGlicose();
        ValidadorExame v4 = new ValidadorCreatinina();
        ValidadorExame v5 = new ValidadorRaioX();
        ValidadorExame v6 = new ValidadorRessonancia();
        v1.setProximo(v2).setProximo(v3).setProximo(v4).setProximo(v5).setProximo(v6);
        v1.validar(exame, contexto);
    }

    public String gerarLaudo(LaudoRequest req) {
        ExameComponent examePrincipal;
        ValidacaoContexto ctx = req.getContexto() != null ? req.getContexto() : new ValidacaoContexto();
        TipoExame tipoPrincipal = TipoExame.valueOf(req.getTipoExame().toUpperCase());

        // --- CORREÇÃO FINAL AQUI ---
        // A condição foi ajustada para verificar o tipo de exame COMPOSTO.
        if (tipoPrincipal == TipoExame.HEMOGRAMA_AGRUPADO) {
            HemogramaAgrupado exameComposto = (HemogramaAgrupado) ExameFactory.criar(tipoPrincipal);
            if (req.getSubExamesSelecionados() != null && !req.getSubExamesSelecionados().isEmpty()) {
                for (String nomeSubExame : req.getSubExamesSelecionados()) {
                    TipoExame tipoSubExame = TipoExame.valueOf(nomeSubExame.toUpperCase());
                    ExameComponent subExame = ExameFactory.criar(tipoSubExame);
                    exameComposto.adicionar(subExame);
                }
            }
            examePrincipal = exameComposto;
        } else {
            examePrincipal = ExameFactory.criar(tipoPrincipal);
        }

        validarExame(examePrincipal, ctx);
        int numero = sequencial.proximo();

        String diagnostico = ctx.getDiagnostico() != null ? ctx.getDiagnostico() : "Sem observações.";

        LaudoCompleto l = new LaudoBuilder()
                .numero(numero)
                .paciente(req.getPacienteNome(), req.getConvenio())
                .medico(req.getMedicoSolicitante(), req.getMedicoResponsavel(), req.getCrmResponsavel())
                .resultados(ctx.getResultados())
                .observacoes(diagnostico)
                .data(LocalDate.now())
                .build();

        FormatoLaudo fmt = switch (req.getFormato().toLowerCase()) {
            case "html" -> new HtmlFormato();
            case "pdf" -> new PdfFormato();
            default -> new TextoFormato();
        };
        Laudo laudo = new Laudo(fmt);
        return laudo.gerarLaudo(l);
    }

    public LaudoCompleto construirLaudo(LaudoRequest req) {
        ExameComponent examePrincipal;
        ValidacaoContexto ctx = req.getContexto() != null ? req.getContexto() : new ValidacaoContexto();
        TipoExame tipoPrincipal = TipoExame.valueOf(req.getTipoExame().toUpperCase());

        if (tipoPrincipal == TipoExame.HEMOGRAMA_AGRUPADO) {
            HemogramaAgrupado exameComposto = (HemogramaAgrupado) ExameFactory.criar(tipoPrincipal);
            if (req.getSubExamesSelecionados() != null && !req.getSubExamesSelecionados().isEmpty()) {
                for (String nomeSubExame : req.getSubExamesSelecionados()) {
                    TipoExame tipoSubExame = TipoExame.valueOf(nomeSubExame.toUpperCase());
                    ExameComponent subExame = ExameFactory.criar(tipoSubExame);
                    exameComposto.adicionar(subExame);
                }
            }
            examePrincipal = exameComposto;
        } else {
            examePrincipal = ExameFactory.criar(tipoPrincipal);
        }

        validarExame(examePrincipal, ctx);
        int numero = sequencial.proximo();
        String diagnostico = ctx.getDiagnostico() != null ? ctx.getDiagnostico() : "Sem observações.";

        return new LaudoBuilder()
                .numero(numero)
                .paciente(req.getPacienteNome(), req.getConvenio())
                .medico(req.getMedicoSolicitante(), req.getMedicoResponsavel(), req.getCrmResponsavel())
                .resultados(ctx.getResultados())
                .observacoes(diagnostico)
                .data(LocalDate.now())
                .build();
    }

    public String formatarLaudo(LaudoCompleto laudoCompleto, String formato) {
        FormatoLaudo fmt = switch (formato.toLowerCase()) {
            case "html" -> new HtmlFormato();
            case "pdf" -> new PdfFormato();
            default -> new TextoFormato();
        };
        Laudo laudo = new Laudo(fmt);
        return laudo.gerarLaudo(laudoCompleto);
    }

    public LaudoRequest parseCsvParaLaudoRequest(InputStream csvInputStream)
            throws IOException, CsvValidationException {
        LaudoRequest request = new LaudoRequest();
        ValidacaoContexto contexto = new ValidacaoContexto();

        try (CSVReader reader = new CSVReader(new InputStreamReader(csvInputStream))) {
            List<ResultadoExameItem> resultados = new ArrayList<>();
            String[] header = reader.readNext();

            if (header != null && header.length > 1 && header[0].trim().equalsIgnoreCase("chave")) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine.length < 2)
                        continue;
                    String chave = nextLine[0].trim();
                    String valor = nextLine[1].trim();

                    switch (chave) {
                        case "tipoExame":
                            request.setTipoExame(valor);
                            break;
                        case "formato":
                            request.setFormato(valor);
                            break;
                        case "pacienteNome":
                            request.setPacienteNome(valor);
                            break;
                        case "convenio":
                            request.setConvenio(valor);
                            break;
                        case "medicoSolicitante":
                            request.setMedicoSolicitante(valor);
                            break;
                        case "medicoResponsavel":
                            request.setMedicoResponsavel(valor);
                            break;
                        case "crmResponsavel":
                            request.setCrmResponsavel(valor);
                            break;
                        case "contexto.valor":
                            contexto.setValor(Double.parseDouble(valor));
                            break;
                        case "contexto.unidade":
                            contexto.setUnidade(valor);
                            break;
                        case "contexto.idade":
                            contexto.setIdade(Integer.parseInt(valor));
                            break;
                        case "contexto.sexo":
                            contexto.setSexo(valor);
                            break;
                        case "contexto.pacienteComImplantesMetalicos":
                            contexto.setPacienteComImplantesMetalicos(Boolean.parseBoolean(valor));
                            break;
                        case "contexto.radiologistaAssinatura":
                            contexto.setRadiologistaAssinatura(valor);
                            break;
                        case "contexto.protocolo":
                            contexto.setProtocolo(valor);
                            break;
                        case "contexto.usouContraste":
                            contexto.setUsouContraste(Boolean.parseBoolean(valor));
                            break;
                        case "contexto.contraste":
                            contexto.setContraste(valor);
                            break;
                        case "contexto.doseContraste":
                            contexto.setDoseContraste(valor);
                            break;
                    }
                }
            } else if (header != null && header.length > 1 && header[0].trim().equalsIgnoreCase("Exame")) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine.length < 4)
                        continue;
                    resultados.add(new ResultadoExameItem(nextLine[0], nextLine[1], nextLine[2], nextLine[3]));
                }
                contexto.setResultados(resultados);
            }
        }
        request.setContexto(contexto);
        return request;
    }

    public void notificarPaciente(String mensagem, String destino) {
        assunto.notificarTodos(mensagem, destino);
    }

    public double aplicarDescontos(double precoBase, List<Desconto> descontos) {
        double valor = precoBase;
        for (Desconto d : descontos)
            valor = d.aplicar(valor);
        return valor;
    }

    public List<Desconto> construirDescontos(boolean convenio, boolean idoso, boolean outubroRosa) {
        List<Desconto> ds = new ArrayList<>();
        if (convenio)
            ds.add(new DescontoConvenio(0.15));
        if (idoso)
            ds.add(new DescontoIdoso());
        if (outubroRosa)
            ds.add(new DescontoOutubroRosa());
        return ds;
    }

    public void configurarFilaUrgente() {
        fila.setEstrategia(new EstrategiaPrioridadeUrgente());
    }

    public void configurarFilaRotina() {
        fila.setEstrategia(new EstrategiaPrioridadeRotina());
    }

    public void adicionarNaFila(String tipoExame, String paciente) {
        fila.adicionar(tipoExame, paciente);
    }

    public FilaDePrioridade.Item proximoDaFila() {
        return fila.proximo();
    }

    public int tamanhoFila() {
        return fila.tamanho();
    }

    public List<FilaDePrioridade.Item> listarExamesEmFila() {
        return fila.listarExames();
    }

    public Map<String, com.robato.diagnosticos.domain.Paciente> listarPacientes() {
        return csv.listarPacientes();
    }

    public Map<String, com.robato.diagnosticos.domain.Medico> listarMedicos() {
        return csv.listarMedicos();
    }

    public void adicionarPaciente(Paciente novoPaciente) {
        listarPacientes().put(novoPaciente.getNome(), novoPaciente);
    }
}