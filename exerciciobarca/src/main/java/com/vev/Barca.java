package com.vev;

import java.util.regex.Pattern;

public class Barca {
    public static final int ASSENTOS_POR_FILA = 20;
    public static final int FILAS = 60;
    private boolean[][] assentos;
    private int qtdadeAssentosOcupados;

    public Barca() {
        assentos = new boolean[FILAS][];
        for (int i = 0; i < FILAS; i++) {
            assentos[i] = new boolean[ASSENTOS_POR_FILA];
            for (int j = 0; j < ASSENTOS_POR_FILA; j++) {
                assentos[i][j] = false;
            }
        }
        qtdadeAssentosOcupados = 0;
    }

    // Método auxiliar projetado para facilitar testes
    protected void ocupaLugar(int fila, int assento) {
        assentos[fila][assento] = true;
        qtdadeAssentosOcupados++;
    }

    /*
     * 0 – Identificador de assento inválido
     * 1 – Assento ocupado
     * 2 – Assento bloqueado devido a distribuição de peso
     * 3 – Ok, assento atribuído ao passageiro.
     */
    public int ocupaLugar(String assentoInformado) {
        // Verifica se é um assento valido
        if (Pattern.matches("[F][0-8]{2}[A][0-8]{2}", assentoInformado) == false) {
            return 0;
        }
        int fila = Integer.parseInt(assentoInformado.substring(1, 3));
        int assento = Integer.parseInt(assentoInformado.substring(4, 6));
        if (fila < 0 || fila > 60) {
            return 0;
        }
        if (assento < 0 || assento >= 20) {
            return 0;
        }
        // Verifica se o assento já não está ocupado
        if (assentos[fila][assento] == true) {
            return 1;
        }
        // Se tem até 100 passageiros, verifica se fila <= 20
        if (qtdadeAssentosOcupados <= 100 && fila > 20) {
            return 1;
        }
        // Se tem mais de 100 e até de 200 passageiros, verifica se fila >= 40
        if (qtdadeAssentosOcupados > 100 && qtdadeAssentosOcupados <= 200 && fila < 40) {
            return 2;
        }
        // Ocupa o assento
        assentos[fila][assento] = true;
        qtdadeAssentosOcupados++;
        return 3;
    }
}
