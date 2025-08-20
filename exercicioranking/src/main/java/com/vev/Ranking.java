package com.vev;

public class Ranking {
    private final int MAXSCORES = 20;
    private Record[] scores;
    private int pos;

    public Ranking() {
        scores = new Record[MAXSCORES];
        pos = 0;
    }

    // Insere um registro na lista mantendo a ordenação
    private void orderedInsert(Record record) {
        for (int i = 0; i < pos; i++) {
            if (record.getScore() > scores[i].getScore()) {
                for (int j = pos; j > i; j--) {
                    scores[j] = scores[j - 1];
                }
                scores[i] = record;
                pos++;
                return;
            }
        }
        scores[pos] = record;
        pos++;
    }

    // Insere um novo registro na lista, se possível, mantendo a ordenação
    // Retorna true se a inserção foi possível
    public boolean add(Record record) {
        if (pos < MAXSCORES) {
            orderedInsert(record);
            return true;
        } else {
            if (record.getScore() >= this.worstScore().getScore()) {
                // remove o pior score
                pos--;
                // insere ordenado
                orderedInsert(record);
                return true;
            }
        }
        return false;
    }

    public int numRecords() {
        return pos;
    }

    public Record getScore(int i) {
        if (i < 0 || i >= pos) {
            return null;
        } else {
            return scores[i];
        }
    }

    public Record worstScore() {
        return scores[pos - 1];
    }

    public Record bestScore() {
        return scores[0];
    }

}
