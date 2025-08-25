# Sistema de Gerenciamento de Exames Médicos - Robato Diagnósticos

## Equipe

- Cássia dos Santos de A Gomes  
- Pedro Henrique Barbosa  

---

## Visão Geral do Projeto

Este projeto, desenvolvido para a disciplina de **Padrões de Projeto de Software**, tem como objetivo criar um Sistema de Gerenciamento de Exames Médicos e Emissão de Laudos para a empresa fictícia **Robato Diagnósticos**.

O sistema foi projetado para ser **flexível, reutilizável e de fácil manutenção**, com suporte para múltiplos tipos de exames, formatos de laudo, regras de validação e notificações simplificadas. O foco está na **modelagem dos exames, validação e geração de laudos**.

---

## Padrões de Projeto Aplicados

### R2: Estruturação e Composição de Exames (incluindo subexames)

**Padrão Aplicado:** Composite  

**Aplicação:**  
Permite tratar exames individuais (folhas) e agrupamentos de exames (compósitos, como o Hemograma Agrupado) de forma uniforme. Um Hemograma pode ser composto por múltiplos subexames (Hemograma Completo, Glicemia, Sorologia), formando uma estrutura de árvore.

**Estrutura:**
- **Interface `ExameComponent`:** Interface comum para todos os exames, com operações como `calcularPreco()` e `obterDescricao()`.
- **Classe Abstrata `ExameBase`:** Implementa atributos e comportamentos comuns às folhas, estendendo `ExameComponent`.
- **Classes Concretas (Folhas):** `HemogramaCompleto`, `Glicemia`, `Sorologia`, `RaioX`, `RessonanciaMagnetica`.
- **Classe `HemogramaAgrupado` (Composto):** Contém outros `ExameComponents` e delega operações de cálculo e descrição.

**Justificativa:**  
Proporciona uma hierarquia flexível de exames, permitindo manipular exames simples e complexos de forma consistente.

---

### Criação Flexível de Exames

**Padrão Aplicado:** Factory (ou Simple Factory / Factory Method simplificado)  

**Aplicação:**  
Criação de instâncias dos diversos tipos de exames de forma centralizada e desacoplada do cliente.

**Estrutura:**
- **Classe `ExameFactory`:** Possui o método `criarExame()` que retorna a instância correta de `ExameComponent`.

**Justificativa:**  
Facilita a adição de novos tipos de exames e simplifica a lógica de criação.

---

### R3, R4: Geração de Laudos em Múltiplos Formatos (Texto, HTML, PDF)

**Padrão Aplicado:** Bridge  

**Aplicação:**  
Separa a abstração do Laudo de sua implementação de formatação.

---

### R5: Aplicação de Regras de Validação Extensíveis

**Padrão Aplicado:** Chain of Responsibility  

**Aplicação:**  
Criação de cadeia de validadores para aplicar regras específicas aos exames.

**Estrutura:**
- **Interface `ValidadorExame`:** Contrato com `setProximo()` e `validar(ExameComponent)`.
- **Classe Abstrata `ValidadorBase`:** Encadeamento de validadores.
- **Validadores Concretos:** `ValidadorHemogramaResultados`, `ValidadorImplantesMetalicos`, `ValidadorExameAgrupado`.

**Justificativa:**  
Desacopla a lógica de validação, permitindo extensão e reordenação de regras.

---

### R6: Notificação de Pacientes Quando o Laudo Estiver Pronto

**Padrão Aplicado:** Observer  

**Aplicação:**  
Notificar pacientes automaticamente por canais específicos ao concluir o laudo.

**Estrutura:**
- **`AssuntoNotificacao`:** Gerencia os observadores.
- **Interface `ObservadorNotificacao`:** Define `atualizar()`.
- **`NotificadorWhatsapp`:** Implementação concreta para WhatsApp.
- **`FachadaNotificacaoComunicacao`:** Facilita a integração de notificações.

**Justificativa:**  
Permite adicionar ou remover canais de notificação sem acoplar ao sistema principal.

---

### R7: Aplicação de Políticas de Desconto Dinâmicas

**Padrão Aplicado:** Decorator  

**Aplicação:**  
Permite a aplicação de múltiplos descontos acumuláveis aos preços dos exames.

**Estrutura:**
- **Interface `Desconto`:** Define `aplicarDesconto(originalPrice)`.
- **`PrecoBaseDesconto`:** Componente a ser decorado.
- **Decoradores Concretos:** `DescontoConvenio`, `DescontoIdoso`, `DescontoOutubroRosa`.

**Justificativa:**  
Oferece flexibilidade para combinar descontos de forma transparente.

---

### R8: Gerenciamento da Prioridade dos Exames e Fila de Processamento

**Padrão Aplicado:** Strategy  

**Aplicação:**  
Gerencia a ordenação de exames em fila com base em prioridades.

**Estrutura:**
- **`FilaDePrioridade`:** Mantém e gerencia exames.
- **Interface `EstrategiaPrioridade`:** Define `determinarPrioridade(ExameComponent)`.
- **Estratégias Concretas:** `EstrategiaPrioridadeUrgente`, `EstrategiaPrioridadePoucoUrgente`, `EstrategiaPrioridadeRotina`.

**Justificativa:**  
Permite selecionar a lógica de prioridade em tempo de execução.

---

### R1, R9: Orquestração do Sistema

**Padrão Aplicado:** Facade  

**Aplicação:**  
Fornece uma interface unificada para os subsistemas do sistema.

**Estrutura:**
- **`SistemaDiagnosticoFacade`:** Encapsula a complexidade dos múltiplos padrões aplicados.  

Expõe métodos como:
- `registrarNovoExame()`
- `validarExame()`
- `gerarLaudo()`
- `imprimirLaudo()`
- `notificarPaciente()`

**Justificativa:**  
Simplifica a interação do cliente com o sistema, escondendo detalhes de implementação e integrando os diversos padrões utilizados.

