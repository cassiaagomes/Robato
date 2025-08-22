package com.robato.diagnosticos.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ValidacaoRequest {
    @NotBlank
    private String tipoExame;

    @NotNull
    private ValidacaoContexto contexto;

    public String getTipoExame() {
        return tipoExame;
    }

    public void setTipoExame(String tipoExame) {
        this.tipoExame = tipoExame;
    }

    public ValidacaoContexto getContexto() {
        return contexto;
    }

    public void setContexto(ValidacaoContexto contexto) {
        this.contexto = contexto;
    }
}