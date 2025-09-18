package com.vev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RankingTest {

    private Ranking ranking;

    @BeforeEach
    void setUp() {
        ranking = new Ranking();
    }

    // Testes para o método add()
    @Test
    void testAddToEmptyRanking() {
        Record record = new Record("Player1", 100);
        assertTrue(ranking.add(record));
        assertEquals(1, ranking.numRecords());
        assertEquals(record, ranking.getScore(0));
    }

    @Test
    void testAddMultipleRecordsInOrder() {
        assertTrue(ranking.add(new Record("Player1", 100)));
        assertTrue(ranking.add(new Record("Player2", 200)));
        assertTrue(ranking.add(new Record("Player3", 150)));

        assertEquals(3, ranking.numRecords());
        assertEquals(200, ranking.getScore(0).getScore()); // Maior score primeiro
        assertEquals(150, ranking.getScore(1).getScore());
        assertEquals(100, ranking.getScore(2).getScore());
    }

    @Test
    void testAddWhenRankingNotFull() {
        // Adiciona 19 jogadores (menos que MAXSCORES)
        for (int i = 1; i <= 19; i++) {
            assertTrue(ranking.add(new Record("Player" + i, i * 10)));
        }
        assertEquals(19, ranking.numRecords());

        // Adiciona mais um
        assertTrue(ranking.add(new Record("Player20", 250)));
        assertEquals(20, ranking.numRecords());
    }

    @Test
    void testAddWhenRankingFullWithBetterScore() {
        // Preenche o ranking com 20 jogadores
        for (int i = 1; i <= 20; i++) {
            ranking.add(new Record("Player" + i, i * 10));
        }

        // Tenta adicionar jogador com score melhor que o pior
        assertTrue(ranking.add(new Record("NewPlayer", 250)));
        assertEquals(20, ranking.numRecords());
        assertEquals(250, ranking.bestScore().getScore());
    }

    @Test
    void testAddWhenRankingFullWithWorseScore() {
        // Preenche o ranking com 20 jogadores
        for (int i = 1; i <= 20; i++) {
            ranking.add(new Record("Player" + i, i * 10));
        }

        // Tenta adicionar jogador com score pior que o último
        assertFalse(ranking.add(new Record("BadPlayer", 5)));
        assertEquals(20, ranking.numRecords());
    }

    @Test
    void testAddWhenRankingFullWithEqualScore() {
        // Preenche o ranking
        for (int i = 1; i <= 20; i++) {
            ranking.add(new Record("Player" + i, i * 10));
        }

        // Adiciona jogador com score igual ao pior
        assertTrue(ranking.add(new Record("EqualPlayer", 10)));
        assertEquals(20, ranking.numRecords());
    }

    @Test
    void testAddNullRecord() {
        assertThrows(NullPointerException.class, () -> {
            ranking.add(null);
        });
    }

    @Test
    void testAddWithZeroScore() {
        assertTrue(ranking.add(new Record("Player1", 0)));
        assertEquals(0, ranking.getScore(0).getScore());
    }

    @Test
    void testAddWithNegativeScore() {
        assertTrue(ranking.add(new Record("Player1", -50)));
        assertEquals(-50, ranking.getScore(0).getScore());
    }

    @Test
    void testAddWithVeryLargeScore() {
        assertTrue(ranking.add(new Record("Player1", Integer.MAX_VALUE)));
        assertEquals(Integer.MAX_VALUE, ranking.getScore(0).getScore());
    }

    // Testes para o método numRecords()
    @Test
    void testNumRecordsEmpty() {
        assertEquals(0, ranking.numRecords());
    }

    @Test
    void testNumRecordsPartiallyFilled() {
        ranking.add(new Record("Player1", 100));
        ranking.add(new Record("Player2", 200));
        assertEquals(2, ranking.numRecords());
    }

    @Test
    void testNumRecordsFull() {
        for (int i = 1; i <= 20; i++) {
            ranking.add(new Record("Player" + i, i * 10));
        }
        assertEquals(20, ranking.numRecords());
    }

    // Testes para o método getScore()
    @Test
    void testGetScoreValidIndex() {
        ranking.add(new Record("Player1", 100));
        ranking.add(new Record("Player2", 200));

        assertEquals("Player2", ranking.getScore(0).getName());
        assertEquals("Player1", ranking.getScore(1).getName());
    }

    @Test
    void testGetScoreInvalidNegativeIndex() {
        ranking.add(new Record("Player1", 100));
        assertNull(ranking.getScore(-1));
    }

    @Test
    void testGetScoreInvalidLargeIndex() {
        ranking.add(new Record("Player1", 100));
        assertNull(ranking.getScore(1));
        assertNull(ranking.getScore(100));
    }

    @Test
    void testGetScoreEmptyRanking() {
        assertNull(ranking.getScore(0));
    }

    @Test
    void testGetScoreBoundaryIndex() {
        ranking.add(new Record("Player1", 100));
        assertNotNull(ranking.getScore(0));
        assertNull(ranking.getScore(1));
    }

    @Test
    void testGetScoreAtMaxValidIndex() {
        for (int i = 1; i <= 20; i++) {
            ranking.add(new Record("Player" + i, i * 10));
        }

        assertNotNull(ranking.getScore(19)); // Último índice válido
        assertNull(ranking.getScore(20));    // Primeiro índice inválido
    }

    // Testes para o método worstScore()
    @Test
    void testWorstScoreSingleRecord() {
        Record record = new Record("Player1", 100);
        ranking.add(record);
        assertEquals(record, ranking.worstScore());
    }

    @Test
    void testWorstScoreMultipleRecords() {
        ranking.add(new Record("Player1", 100));
        ranking.add(new Record("Player2", 200));
        ranking.add(new Record("Player3", 50));

        assertEquals(50, ranking.worstScore().getScore());
    }

    @Test
    void testWorstScoreEmptyRanking() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            ranking.worstScore();
        });
    }

    // Testes para o método bestScore()
    @Test
    void testBestScoreSingleRecord() {
        Record record = new Record("Player1", 100);
        ranking.add(record);
        assertEquals(record, ranking.bestScore());
    }

    @Test
    void testBestScoreMultipleRecords() {
        ranking.add(new Record("Player1", 100));
        ranking.add(new Record("Player2", 200));
        ranking.add(new Record("Player3", 50));

        assertEquals(200, ranking.bestScore().getScore());
    }

    @Test
    void testBestScoreEmptyRanking() {
        // bestScore() acessa scores[0] que existe mas é null quando ranking vazio
        assertNull(ranking.bestScore());
    }

    // Testes de cenários complexos
    @Test
    void testCorrectReplacementWhenFull() {
        // Preenche com scores 10, 20, 30... 200
        for (int i = 1; i <= 20; i++) {
            ranking.add(new Record("Player" + i, i * 10));
        }

        // Adiciona score 150 (deve substituir o pior que é 10)
        assertTrue(ranking.add(new Record("NewPlayer", 150)));

        // Verifica que o pior agora é 20 (não 10)
        assertEquals(20, ranking.worstScore().getScore());

        // Verifica que 150 está na posição correta
        boolean found150 = false;
        for (int i = 0; i < ranking.numRecords(); i++) {
            if (ranking.getScore(i).getScore() == 150) {
                found150 = true;
                break;
            }
        }
        assertTrue(found150);
    }

    @Test
    void testOrderingAfterMultipleInsertions() {
        int[] scores = {50, 100, 75, 200, 25, 150, 80, 300, 10, 90};

        for (int i = 0; i < scores.length; i++) {
            ranking.add(new Record("Player" + i, scores[i]));
        }

        // Verifica se está ordenado decrescente
        for (int i = 0; i < ranking.numRecords() - 1; i++) {
            assertTrue(ranking.getScore(i).getScore() >= ranking.getScore(i + 1).getScore(),
                    "Ranking não está ordenado na posição " + i);
        }
    }

    @Test
    void testMultipleSameScores() {
        ranking.add(new Record("Player1", 100));
        ranking.add(new Record("Player2", 100));
        ranking.add(new Record("Player3", 100));

        assertEquals(3, ranking.numRecords());
        assertEquals(100, ranking.bestScore().getScore());
        assertEquals(100, ranking.worstScore().getScore());
    }

    @Test
    void testEqualScoreReplacementBehavior() {
        // Preenche ranking
        for (int i = 1; i <= 20; i++) {
            ranking.add(new Record("Player" + i, i * 10));
        }

        // O pior score é 10. Adiciona outro com score 10
        assertTrue(ranking.add(new Record("EqualPlayer", 10)));

        // Verifica se realmente substituiu (comportamento questionável)
        assertEquals(20, ranking.numRecords());
        assertEquals(10, ranking.worstScore().getScore());
    }

    @Test
    void testOrderedInsertMaintainsOrder() {
        // Adiciona em ordem específica para testar inserção no meio
        ranking.add(new Record("A", 300));
        ranking.add(new Record("B", 100));
        ranking.add(new Record("C", 200)); // Deve ser inserido entre A e B

        assertEquals("A", ranking.getScore(0).getName());
        assertEquals("C", ranking.getScore(1).getName());
        assertEquals("B", ranking.getScore(2).getName());
    }

    @Test
    void testExactlyMaxScores() {
        for (int i = 1; i <= 20; i++) {
            assertTrue(ranking.add(new Record("Player" + i, i)));
        }

        assertEquals(20, ranking.numRecords());

        // Próxima inserção com score menor deve falhar
        assertFalse(ranking.add(new Record("Worse", 0)));

        // Próxima inserção com score igual ao pior deve passar (devido ao >=)
        assertTrue(ranking.add(new Record("Equal", 1)));
    }

    // Teste de integração completa
    @Test
    void testCompleteScenario() {
        // Cenário completo: adiciona vários jogadores e verifica ordenação
        ranking.add(new Record("Alice", 100));
        ranking.add(new Record("Bob", 300));
        ranking.add(new Record("Charlie", 200));
        ranking.add(new Record("David", 150));

        assertEquals(4, ranking.numRecords());
        assertEquals("Bob", ranking.bestScore().getName());
        assertEquals("Alice", ranking.worstScore().getName());

        // Verifica ordenação completa
        assertEquals(300, ranking.getScore(0).getScore());
        assertEquals(200, ranking.getScore(1).getScore());
        assertEquals(150, ranking.getScore(2).getScore());
        assertEquals(100, ranking.getScore(3).getScore());
    }

    // Teste para Record com nome nulo
    @Test
    void testAddRecordWithNullName() {
        assertTrue(ranking.add(new Record(null, 100)));
        assertEquals(1, ranking.numRecords());
        assertNull(ranking.getScore(0).getName());
    }

    // Teste para Record com nome vazio
    @Test
    void testAddRecordWithEmptyName() {
        assertTrue(ranking.add(new Record("", 100)));
        assertEquals(1, ranking.numRecords());
        assertEquals("", ranking.getScore(0).getName());
    }

    // Teste que revela o bug no orderedInsert
    @Test
    void testOrderedInsertBug() {
        // Adiciona elementos para forçar inserção no meio
        ranking.add(new Record("First", 300));
        ranking.add(new Record("Last", 100));

        // Estado antes: pos = 2
        assertEquals(2, ranking.numRecords());

        // Inserção no meio deve manter pos correto
        ranking.add(new Record("Middle", 200));

        // Bug: pos pode estar incorreto após orderedInsert
        assertEquals(3, ranking.numRecords());

        // Verifica ordenação
        assertEquals(300, ranking.getScore(0).getScore());
        assertEquals(200, ranking.getScore(1).getScore());
        assertEquals(100, ranking.getScore(2).getScore());
    }

    // Teste para detectar se pos pode exceder MAXSCORES
    @Test
    void testPosNeverExceedsMaxScores() {
        // Preenche completamente
        for (int i = 1; i <= 20; i++) {
            ranking.add(new Record("Player" + i, i));
        }

        // Múltiplas substituições
        for (int i = 21; i <= 30; i++) {
            ranking.add(new Record("NewPlayer" + i, i));
        }

        // pos nunca deve exceder MAXSCORES
        assertTrue(ranking.numRecords() <= 20);
    }

    // Teste para inserção com scores duplicados em posições específicas
    @Test
    void testInsertDuplicateScoreInMiddle() {
        ranking.add(new Record("A", 300));
        ranking.add(new Record("B", 200));
        ranking.add(new Record("C", 100));

        // Inserir outro com score 200 (duplicata no meio)
        ranking.add(new Record("D", 200));

        assertEquals(4, ranking.numRecords());

        // Verificar que ambos 200 estão presentes
        int count200 = 0;
        for (int i = 0; i < ranking.numRecords(); i++) {
            if (ranking.getScore(i).getScore() == 200) {
                count200++;
            }
        }
        assertEquals(2, count200);
    }

    // Teste boundary: verificar que não aceita mais que MAXSCORES
    @Test
    void testStrictMaxScoresLimit() {
        // Preenche com scores muito altos
        for (int i = 1; i <= 20; i++) {
            ranking.add(new Record("Player" + i, 1000 + i));
        }

        assertEquals(20, ranking.numRecords());

        // Tentar adicionar com score altíssimo
        assertFalse(ranking.add(new Record("TooHigh", 10000)));
        assertEquals(20, ranking.numRecords());
    }

    // Teste para validar que bestScore nunca falha inesperadamente
    @Test
    void testBestScoreConsistency() {
        // Adiciona e remove múltiplas vezes
        ranking.add(new Record("A", 100));
        assertNotNull(ranking.bestScore());

        // Preenche e substitui
        for (int i = 1; i <= 20; i++) {
            ranking.add(new Record("Player" + i, i));
        }

        // Múltiplas substituições
        for (int i = 21; i <= 25; i++) {
            ranking.add(new Record("New" + i, i));
        }

        // bestScore deve sempre retornar o maior
        Record best = ranking.bestScore();
        assertNotNull(best);
        assertEquals(25, best.getScore());
    }
}