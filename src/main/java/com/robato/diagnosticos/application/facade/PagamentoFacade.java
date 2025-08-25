package com.robato.diagnosticos.application.facade;


import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.robato.diagnosticos.application.dto.ComprovanteDTO;
import com.robato.diagnosticos.domain.composite.ItemExame;
import com.robato.diagnosticos.domain.model.Paciente;
import com.robato.diagnosticos.domain.service.GeradorIDSequencial;
import com.robato.diagnosticos.domain.visitor.CalculadorPrecoVisitor;
import com.robato.diagnosticos.infrastructure.decorator.Desconto;
import com.robato.diagnosticos.infrastructure.decorator.DescontoConvenio;
import com.robato.diagnosticos.infrastructure.decorator.DescontoIdoso;
import com.robato.diagnosticos.infrastructure.decorator.PrecoBase;

@Service
public class PagamentoFacade {

    public ComprovanteDTO gerarComprovante(Paciente paciente, ItemExame exame, boolean aplicaDescontoConvenio, boolean aplicaDescontoIdoso) {
        
        CalculadorPrecoVisitor precoVisitor = new CalculadorPrecoVisitor();
        exame.accept(precoVisitor);
        BigDecimal precoBase = precoVisitor.getPrecoTotal();

        Desconto calculadoraDeDescontos = new PrecoBase();
        
        if (aplicaDescontoConvenio) {
            calculadoraDeDescontos = new DescontoConvenio(calculadoraDeDescontos);
        }
        if (aplicaDescontoIdoso) {
            calculadoraDeDescontos = new DescontoIdoso(calculadoraDeDescontos);
        }

        BigDecimal precoFinal = calculadoraDeDescontos.aplicar(precoBase);
        BigDecimal valorDesconto = precoBase.subtract(precoFinal);
        
        long idComprovante = GeradorIDSequencial.getInstance().proximoId();

        return new ComprovanteDTO(idComprovante, paciente, exame, precoBase, valorDesconto, precoFinal);
    }
}