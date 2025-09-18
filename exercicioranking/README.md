# Ranking System - Análise de Cobertura de Testes

## 📋 Visão Geral

Este projeto implementa um sistema de ranking que mantém os melhores scores em ordem decrescente, com capacidade máxima de 20 registros. O sistema foi desenvolvido com foco em testes estruturais e análise de cobertura usando o critério MC/DC (Modified Condition/Decision Coverage).

## 🏗️ Estrutura do Projeto

```
exercicioranking/
├── src/
│   ├── main/java/com/vev/
│   │   ├── Ranking.java      # Classe principal do sistema de ranking
│   │   └── Record.java       # Classe que representa um registro (nome + score)
│   └── test/java/com/vev/
│       └── RankingTest.java  # Suite completa de testes
├── pom.xml                   # Configuração Maven com JaCoCo
└── README.md                 # Este arquivo
```

## 🎯 Funcionalidades Implementadas

### Classe `Ranking`
- **`add(Record record)`**: Insere um novo registro mantendo ordenação decrescente
- **`numRecords()`**: Retorna o número atual de registros
- **`getScore(int i)`**: Obtém o registro na posição i
- **`bestScore()`**: Retorna o melhor score (primeiro da lista)
- **`worstScore()`**: Retorna o pior score (último da lista)

### Classe `Record`
- Armazena nome do jogador e score
- Métodos getter para nome e score
- Método `toString()` para representação textual

## 📊 Análise de Cobertura

### Métricas de Cobertura (JaCoCo)
- ✅ **100% Cobertura de Instruções** (150/150)
- ✅ **100% Cobertura de Branches** (14/14)
- ✅ **100% Cobertura de Linhas** (36/36)
- ✅ **100% Cobertura de Métodos** (11/11)

### Relatório JaCoCo
Os relatórios detalhados estão disponíveis em:
```
target/site/jacoco/index.html
```

## 🔍 Análise MC/DC (Modified Condition/Decision Coverage)

### Decisões Complexas Identificadas

#### 1. `getScore(int i)` - Linha 52
```java
if (i < 0 || i >= pos)
```
**Condições:**
- A: `i < 0`
- B: `i >= pos`
- **Decisão:** `A || B`

**Cobertura MC/DC:**
- ✅ (A=true, B=false) → `testGetScoreInvalidNegativeIndex`
- ✅ (A=false, B=true) → `testGetScoreInvalidLargeIndex`
- ✅ (A=false, B=false) → `testGetScoreValidIndex`

#### 2. `add(Record record)` - Linha 32
```java
if (pos < MAXSCORES)
```
**Cobertura:**
- ✅ Condição verdadeira: múltiplos testes
- ✅ Condição falsa: testes com ranking cheio

#### 3. `add(Record record)` - Linha 36
```java
if (record.getScore() >= this.worstScore().getScore())
```
**Cobertura:**
- ✅ Condição verdadeira: `testAddWhenRankingFullWithBetterScore`
- ✅ Condição falsa: `testAddWhenRankingFullWithWorseScore`
- ✅ Caso limite (igualdade): `testAddWhenRankingFullWithEqualScore`

## 🧪 Classificação dos Testes

### 🟢 Testes Essenciais para MC/DC (15 testes)
1. `testAddToEmptyRanking`
2. `testAddWhenRankingNotFull`
3. `testAddWhenRankingFullWithBetterScore`
4. `testAddWhenRankingFullWithWorseScore`
5. `testAddWhenRankingFullWithEqualScore`
6. `testGetScoreValidIndex`
7. `testGetScoreInvalidNegativeIndex`
8. `testGetScoreInvalidLargeIndex`
9. `testGetScoreEmptyRanking`
10. `testNumRecordsEmpty`
11. `testWorstScoreEmptyRanking`
12. `testBestScoreEmptyRanking`
13. `testWorstScoreSingleRecord`
14. `testBestScoreSingleRecord`
15. `testAddMultipleRecordsInOrder`

### 🟡 Testes Funcionais Importantes (8 testes)
- `testCorrectReplacementWhenFull`
- `testOrderingAfterMultipleInsertions`
- `testOrderedInsertMaintainsOrder`
- `testExactlyMaxScores`
- `testMultipleSameScores`
- `testEqualScoreReplacementBehavior`
- `testOrderedInsertBug`
- `testInsertDuplicateScoreInMiddle`

### 🔴 Testes Redundantes para MC/DC (16 testes)
- `testAddWithZeroScore`
- `testAddWithNegativeScore`
- `testAddWithVeryLargeScore`
- `testNumRecordsPartiallyFilled`
- `testNumRecordsFull`
- `testGetScoreBoundaryIndex`
- `testGetScoreAtMaxValidIndex`
- `testWorstScoreMultipleRecords`
- `testBestScoreMultipleRecords`
- `testCompleteScenario`
- `testAddRecordWithNullName`
- `testAddRecordWithEmptyName`
- `testPosNeverExceedsMaxScores`
- `testStrictMaxScoresLimit`
- `testBestScoreConsistency`
- `testAddNullRecord` (falha - implementação não lança exceção)

## 📈 Resultados da Análise

### Status Atual
- **39 testes totais**
- **3 testes falhando** (comportamento da implementação difere do esperado)
- **100% cobertura estrutural** atingida

### Recomendações

#### Para MC/DC Puro:
- **Manter**: 15 testes essenciais
- **Remover**: 16 testes redundantes
- **Resultado**: Redução de 41% no número de testes mantendo 100% de cobertura MC/DC

#### Para Testes Funcionais Completos:
- **Manter**: 23 testes (essenciais + funcionais importantes)
- **Remover**: 16 testes redundantes
- **Resultado**: Redução de 18% mantendo cobertura funcional robusta

## 🚀 Executando os Testes

### Pré-requisitos
- Java 21
- Maven 3.6+

### Comandos

```bash
# Compilar o projeto
mvn compile

# Executar todos os testes
mvn test

# Gerar relatório de cobertura JaCoCo
mvn clean test jacoco:report

# Visualizar relatório de cobertura
open target/site/jacoco/index.html
```

### Configuração Maven
O projeto está configurado com:
- **JUnit 5** para testes
- **JaCoCo 0.8.13** para cobertura de código
- **Surefire Plugin** com `testFailureIgnore=true` para gerar relatórios mesmo com falhas

## 🐛 Issues Conhecidos

### Testes que Falham
1. **`testAddNullRecord`**: Espera `NullPointerException`, mas a implementação não lança
2. **`testBestScoreConsistency`**: Falha na verificação do melhor score após múltiplas substituições
3. **`testStrictMaxScoresLimit`**: Implementação aceita scores muito altos quando deveria rejeitar

### Comportamentos Questionáveis da Implementação
- Aceita registros com score igual ao pior quando ranking está cheio
- Não valida entrada nula no método `add()`
- `bestScore()` retorna `null` para ranking vazio (pode causar NPE)

## 📚 Conclusões

### Pontos Fortes
- ✅ Cobertura estrutural completa (100% MC/DC)
- ✅ Suite de testes abrangente
- ✅ Boa documentação dos casos de teste
- ✅ Testes bem organizados por método

### Oportunidades de Melhoria
- 🔧 Otimizar suite removendo testes redundantes
- 🔧 Corrigir comportamentos inconsistentes da implementação
- 🔧 Adicionar validação de entrada
- 🔧 Melhorar tratamento de casos extremos

### Métricas Finais
- **Eficiência de Testes**: 38.5% dos testes são essenciais para MC/DC
- **Cobertura de Branches**: 100% (14/14 branches)
- **Qualidade da Suite**: Alta (detecta bugs reais na implementação)

---

*Análise realizada em: 18 de Setembro de 2025*  
*Ferramenta de Cobertura: JaCoCo 0.8.13*  
*Critério de Análise: MC/DC (Modified Condition/Decision Coverage)*