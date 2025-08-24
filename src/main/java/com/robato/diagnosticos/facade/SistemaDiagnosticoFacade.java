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
import com.robato.diagnosticos.domain.Medico;
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
import com.robato.diagnosticos.prioridade.EstrategiaPrioridade;
import com.robato.diagnosticos.prioridade.EstrategiaPrioridadeRotina;
import com.robato.diagnosticos.prioridade.EstrategiaPrioridadeUrgente;
import com.robato.diagnosticos.prioridade.FilaDePrioridade;
import com.robato.diagnosticos.service.PacienteService;
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
    private final PacienteService pacienteService;
    private final FilaDePrioridade fila = new FilaDePrioridade(new EstrategiaPrioridadeRotina());
    private final SequencialExameGenerator sequencial;
    private final CsvLoader csv;

    public SistemaDiagnosticoFacade(SequencialExameGenerator sequencial, CsvLoader csv,
            PacienteService pacienteService) {
        this.sequencial = sequencial;
        this.csv = csv;
        this.pacienteService = pacienteService;
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
        LaudoCompleto laudoCompleto = construirLaudo(req);
        String formato = req.getFormato() != null ? req.getFormato() : "texto";
        String laudoFormatado = formatarLaudo(laudoCompleto, formato);

        notificarPacienteLaudoPronto(laudoCompleto, req);

        return laudoFormatado;
    }

    public LaudoCompleto construirLaudo(LaudoRequest req) {
        ExameComponent examePrincipal;
        ValidacaoContexto ctx = req.getContexto() != null ? req.getContexto() : new ValidacaoContexto();

        if (req.getTipoExame() == null || req.getTipoExame().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de exame não pode ser nulo ou vazio");
        }

        TipoExame tipoPrincipal = TipoExame.valueOf(req.getTipoExame().toUpperCase());

        if (tipoPrincipal == TipoExame.HEMOGRAMA_AGRUPADO) {
            HemogramaAgrupado exameComposto = (HemogramaAgrupado) ExameFactory.criar(tipoPrincipal);
            if (req.getSubExamesSelecionados() != null && !req.getSubExamesSelecionados().isEmpty()) {
                for (String nomeSubExame : req.getSubExamesSelecionados()) {
                    try {
                        TipoExame tipoSubExame = TipoExame.valueOf(nomeSubExame.toUpperCase());
                        ExameComponent subExame = ExameFactory.criar(tipoSubExame);
                        exameComposto.adicionar(subExame);
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
            examePrincipal = exameComposto;
        } else {
            examePrincipal = ExameFactory.criar(tipoPrincipal);
        }

        validarExame(examePrincipal, ctx);
        int numero = sequencial.proximo();
        String diagnostico = ctx.getDiagnostico() != null ? ctx.getDiagnostico() : "Sem observações.";

        List<ResultadoExameItem> todosOsResultados = ctx.getResultados();
        List<String> subExamesSelecionados = req.getSubExamesSelecionados();
        List<ResultadoExameItem> resultadosFiltrados = new ArrayList<>();

        if (subExamesSelecionados != null && !subExamesSelecionados.isEmpty() && todosOsResultados != null) {
            resultadosFiltrados = todosOsResultados.stream()
                    .filter(resultado -> subExamesSelecionados.contains(resultado.getNomeExame()))
                    .toList();
        } else if (todosOsResultados != null) {
            resultadosFiltrados.addAll(todosOsResultados);
        }

        LaudoCompleto laudo = new LaudoBuilder()
                .numero(numero)
                .paciente(req.getPacienteNome(), req.getConvenio())
                .medico(req.getMedicoSolicitante(), req.getMedicoResponsavel(), req.getCrmResponsavel())
                .resultados(resultadosFiltrados)
                .observacoes(diagnostico)
                .data(LocalDate.now())
                .build();

        // DEPOIS adicionar a imagem se existir
        if (ctx.getImagemBase64() != null) {
            laudo.setImagemBase64(ctx.getImagemBase64());
        }

        return laudo;
    }

    private void notificarPacienteLaudoPronto(LaudoCompleto laudo, LaudoRequest req) {
        String email = null;
        String telefone = null;
        String nomePaciente = req.getPacienteNome();

        if (req.getPacienteId() != null) {
            Paciente paciente = pacienteService.buscarPorId(req.getPacienteId());
            if (paciente != null) {
                email = paciente.getEmail();
                telefone = paciente.getTelefone();
                nomePaciente = paciente.getNome();
            }
        } else {
            email = req.getEmailPaciente();
            telefone = req.getTelefonePaciente();
        }

        String mensagem = String.format(
                "📋 Robato Diagnósticos\n" +
                        "Olá, %s!\n" +
                        "Seu exame está pronto!\n" +
                        "Data: %s\n" +
                        "Resultado: %s\n" +
                        "Acesse o sistema para visualizar o laudo completo.\n" +
                        "Atenciosamente,\nEquipe Robato Diagnósticos",
                nomePaciente,
                laudo.getData(),
                laudo.getObservacoes());

        if (email != null && !email.isEmpty()) {
            assunto.notificarTodos(mensagem, email);
        }

        if (telefone != null && !telefone.isEmpty()) {
            assunto.notificarTodos(mensagem, telefone);
        }
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

    public List<ResultadoExameItem> parseResultadosCsv(InputStream csvInputStream)
            throws IOException, CsvValidationException {
        List<ResultadoExameItem> resultados = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(csvInputStream))) {
            String[] header = reader.readNext();
            if (header == null || !header[0].trim().equalsIgnoreCase("Exame")) {
                throw new CsvValidationException("Formato de CSV inválido.");
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length < 4)
                    continue;

                String nomeExame = nextLine[0].trim();

                try {
                    TipoExame.valueOf(nomeExame.toUpperCase());
                    resultados.add(new ResultadoExameItem(nomeExame, nextLine[1], nextLine[2], nextLine[3], ""));
                } catch (IllegalArgumentException e) {
                }
            }
        }
        return resultados;
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

    public void adicionar(String tipoExame, String paciente, String convenio, EstrategiaPrioridade estrategia) {
        fila.add(paciente, tipoExame, convenio, estrategia);
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

    public Map<String, Paciente> listarPacientes() {
        return csv.listarPacientes();
    }

    public Map<String, Medico> listarMedicos() {
        return csv.listarMedicos();
    }

    public void adicionarPaciente(Paciente novoPaciente) {
        listarPacientes().put(novoPaciente.getNome(), novoPaciente);
    }

    public double calcularCustoTotal(List<TipoExame> tiposExames, boolean comConvenio, boolean ehIdoso) {
        double total = 0.0;
        for (TipoExame tipo : tiposExames) {
            total += 100.0;
        }

        if (comConvenio) {
            total *= 0.85;
        }
        if (ehIdoso) {
            total *= 0.9;
        }

        return total;
    }

    public void registrarPagamento(String pacienteNome, double valorPago) {
        System.out.println("======================================");
        System.out.println("COMPROVANTE DE PAGAMENTO");
        System.out.println("Paciente: " + pacienteNome);
        System.out.printf("Valor Pago: R$ %.2f\n", valorPago);
        System.out.println("Data: " + java.time.LocalDate.now());
        System.out.println("======================================");
    }
}