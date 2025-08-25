package com.robato.diagnosticos.application.dto;

import java.time.LocalDate;

import com.robato.diagnosticos.domain.composite.ItemExame;
import com.robato.diagnosticos.domain.model.Medico;
import com.robato.diagnosticos.domain.model.Paciente;

public record LaudoDTO(
    long id,
    Paciente paciente,
    Medico medicoSolicitante,
    Medico medicoResponsavel,
    LocalDate data,
    String cabecalho,
    String corpo,
    String rodape,
    ItemExame exameRealizado
) {}