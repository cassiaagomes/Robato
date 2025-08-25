package com.robato.diagnosticos.infrastructure.file;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.robato.diagnosticos.domain.builder.PedidoExameBuilder;
import com.robato.diagnosticos.domain.composite.ExameSimples;
import com.robato.diagnosticos.domain.composite.ItemExame;
import com.robato.diagnosticos.domain.factory.ExameFactory;


public class LeitorExameCSV {

    /**
     * Lê um arquivo CSV com resultados e o transforma em um único ItemExame (PacoteExames).
     * @param arquivo O arquivo MultipartFile enviado pelo usuário.
     * @return Um único ItemExame (PacoteExames) contendo todos os exames lidos do CSV.
     * @throws Exception Se o formato do CSV for inválido ou ocorrer um erro de leitura.
     */
    public ItemExame lerResultados(MultipartFile arquivo) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(arquivo.getInputStream(), StandardCharsets.UTF_8))) {
            String[] cabecalho = reader.readNext();
            validarCabecalho(cabecalho);

            String[] linha;
            PedidoExameBuilder builder = new PedidoExameBuilder("Pedido Importado do Arquivo: " + arquivo.getOriginalFilename());

            while ((linha = reader.readNext()) != null) {
                if (linha.length < 4) continue;

                String nomeExame = linha[0].trim();
                String resultado = linha[1].trim();
                String unidade = linha[2].trim();
                String valorReferencia = linha[3].trim();
                
                // 1. Usa a Factory para obter o "molde" do exame com seu preço e categoria corretos.
                ExameSimples exameModelo = ExameFactory.criar(nomeExame);

                // 2. Prepara os atributos lidos do CSV.
                Map<String, Object> atributos = new HashMap<>();
                atributos.put("resultado", resultado);
                atributos.put("unidade", unidade);
                atributos.put("valorReferencia", valorReferencia);

                // 3. Usa o Builder para adicionar o exame ao pacote, passando os dados do modelo e os resultados.
                builder.adicionarExameSimples(
                    exameModelo.getNome(),
                    exameModelo.getCategoria(),
                    exameModelo.getPrecoBase().toString(),
                    atributos
                );
            }
            return builder.build();
        }
    }

    private void validarCabecalho(String[] cabecalho) throws Exception {
        if (cabecalho == null || cabecalho.length < 4 ||
            !"Exame".equalsIgnoreCase(cabecalho[0].trim()) ||
            !"Resultado".equalsIgnoreCase(cabecalho[1].trim()) ||
            !"Unidade".equalsIgnoreCase(cabecalho[2].trim()) ||
            !"ValoresReferencia".equalsIgnoreCase(cabecalho[3].trim())) {
            throw new Exception("Formato de cabeçalho do CSV inválido. As colunas esperadas são: Exame, Resultado, Unidade, ValoresReferencia");
        }
    }
}
