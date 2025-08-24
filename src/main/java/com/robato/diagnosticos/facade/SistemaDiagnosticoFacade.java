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
import com.robato.diagnosticos.prioridade.EstrategiaPrioridade;
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

        // Verificação de segurança para o tipoExame
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
                        System.out.println("Tipo de exame não encontrado: " + nomeSubExame + " - Será ignorado");
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

        // Debug: mostrar o que estamos recebendo
        System.out.println(">>> Subexames selecionados: " + subExamesSelecionados);
        System.out.println(">>> Total de resultados no contexto: " +
                (todosOsResultados != null ? todosOsResultados.size() : 0));

        if (subExamesSelecionados != null && !subExamesSelecionados.isEmpty() && todosOsResultados != null) {
            resultadosFiltrados = todosOsResultados.stream()
                    .filter(resultado -> {
                        boolean contem = subExamesSelecionados.contains(resultado.getNomeExame());
                        if (!contem) {
                            System.out.println(">>> Exame filtrado: " + resultado.getNomeExame() +
                                    " (não está em: " + subExamesSelecionados + ")");
                        }
                        return contem;
                    })
                    .collect(java.util.stream.Collectors.toList());

            System.out.println(">>> Resultados após filtro: " + resultadosFiltrados.size());
        } else if (todosOsResultados != null) {
            resultadosFiltrados.addAll(todosOsResultados);
            System.out.println(">>> Todos os resultados incluídos (sem filtro): " + resultadosFiltrados.size());
        }

        // Se não há resultados, criar lista vazia para evitar null pointer
        if (resultadosFiltrados == null) {
            resultadosFiltrados = new ArrayList<>();
        }

        // Debug: mostrar os resultados que serão incluídos no laudo
        if (!resultadosFiltrados.isEmpty()) {
            System.out.println(">>> Exames que serão incluídos no laudo:");
            for (ResultadoExameItem item : resultadosFiltrados) {
                System.out.println(">>> - " + item.getNomeExame() + ": " + item.getValor() + " " + item.getUnidade());
            }
        } else {
            System.out.println(">>> Nenhum exame será incluído no laudo");
        }

        return new LaudoBuilder()
                .numero(numero)
                .paciente(req.getPacienteNome(), req.getConvenio())
                .medico(req.getMedicoSolicitante(), req.getMedicoResponsavel(), req.getCrmResponsavel())
                .resultados(resultadosFiltrados)
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
                    System.out.println("Exame ignorado (não corresponde ao enum): " + nomeExame);
                }
            }
        }
        System.out.println(">>> CSV parseado: " + resultados.size() + " resultados válidos");
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

    public Map<String, com.robato.diagnosticos.domain.Paciente> listarPacientes() {
        return csv.listarPacientes();
    }

    public Map<String, com.robato.diagnosticos.domain.Medico> listarMedicos() {
        return csv.listarMedicos();
    }

    public void adicionarPaciente(Paciente novoPaciente) {
        listarPacientes().put(novoPaciente.getNome(), novoPaciente);
    }

    // Na sua SistemaDiagnosticoFacade, adicione:
    public double calcularCustoTotal(List<TipoExame> tiposExames, boolean comConvenio, boolean ehIdoso) {
        // Implemente a lógica de cálculo de custos aqui
        double total = 0.0;
        for (TipoExame tipo : tiposExames) {
            total += 100.0; // Valor exemplo - ajuste conforme necessário
        }

        // Aplicar descontos
        if (comConvenio) {
            total *= 0.85; // 15% de desconto
        }
        if (ehIdoso) {
            total *= 0.9; // 10% de desconto para idosos
        }

        return total;
    }

    public void registrarPagamento(String pacienteNome, double valorPago) {
        // Em um sistema real, isso salvaria no banco de dados.
        // Para este projeto, podemos apenas simular com uma saída no console.
        System.out.println("======================================");
        System.out.println("COMPROVANTE DE PAGAMENTO");
        System.out.println("Paciente: " + pacienteNome);
        System.out.printf("Valor Pago: R$ %.2f\n", valorPago);
        System.out.println("Data: " + java.time.LocalDate.now());
        System.out.println("======================================");
    }
}