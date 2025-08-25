package com.robato.diagnosticos.application.dto;


import java.math.BigDecimal;

import com.robato.diagnosticos.domain.composite.ItemExame;
import com.robato.diagnosticos.domain.model.Paciente;

public record ComprovanteDTO(
    long id,
    Paciente paciente,
    ItemExame exame,
    BigDecimal precoBase,
    BigDecimal valorDesconto,
    BigDecimal precoFinal
) {}
