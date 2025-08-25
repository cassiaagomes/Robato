package com.robato.diagnosticos.domain.visitor;


import com.robato.diagnosticos.domain.composite.ExameSimples;
import com.robato.diagnosticos.domain.composite.PacoteExames;

public interface ExameVisitor {
    void visit(ExameSimples exame);
    void visit(PacoteExames pacote);
}
