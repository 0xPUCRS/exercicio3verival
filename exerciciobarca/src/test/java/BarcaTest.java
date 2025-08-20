package com.vev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BarcaTest {

    private Barca barca;

    @BeforeEach
    void setUp() {
        barca = new Barca();
    }

    // PARTIÇÃO 1: Identificador de assento inválido (retorno 0)
    @Test
    void testAssentoInvalidoFormatoIncorreto() {
        assertEquals(0, barca.ocupaLugar("ABC123"));
        assertEquals(0, barca.ocupaLugar("F1A1"));
        assertEquals(0, barca.ocupaLugar("F123A123"));
        assertEquals(0, barca.ocupaLugar(""));
        assertEquals(0, barca.ocupaLugar("G01A01")); // letra errada
    }

    @Test
    void testAssentoInvalidoFilaForaLimite() {
        assertEquals(0, barca.ocupaLugar("F61A01")); // fila > 60 (inválido para array 0-59)
        assertEquals(0, barca.ocupaLugar("F99A01")); // fila muito alta
    }

    @Test
    void testAssentoInvalidoAssentoForaLimite() {
        assertEquals(0, barca.ocupaLugar("F01A20")); // assento >= 20 (inválido para array 0-19)
        assertEquals(0, barca.ocupaLugar("F01A99")); // assento muito alto
    }

    @Test
    void testAssentoInvalidoRegexLimitacao() {
        assertEquals(0, barca.ocupaLugar("F91A01")); // dígito 9 não aceito pela regex
        assertEquals(0, barca.ocupaLugar("F01A91")); // dígito 9 não aceito pela regex
    }

    // PARTIÇÃO 2: Assento ocupado ou restrito (retorno 1)
    @Test
    void testAssentoJaOcupado() {
        assertEquals(3, barca.ocupaLugar("F01A01")); // primeira ocupação
        assertEquals(1, barca.ocupaLugar("F01A01")); // tentativa de reocupar
    }

    @Test
    void testFilaRestritaComAte100Passageiros() {
        // Com barca vazia (0 passageiros <= 100), filas > 20 são restritas
        assertEquals(1, barca.ocupaLugar("F21A01")); // fila 21 > 20, restrita
        assertEquals(1, barca.ocupaLugar("F59A19")); // fila 59 > 20, restrita
        assertEquals(1, barca.ocupaLugar("F40A10")); // fila 40 > 20, restrita
    }

    @Test
    void testFilaRestritaComExatamente100Passageiros() {
        // Ocupa exatamente 100 assentos (5 filas × 20 assentos)
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j < 20; j++) {
                barca.ocupaLugar(i, j);
            }
        }

        assertEquals(3, barca.ocupaLugar("F05A01")); // fila 5 <= 20, permitida com 100 passageiros
        assertEquals(1, barca.ocupaLugar("F21A01")); // fila 21 > 20, restrita com 101 passageiros
    }

    // PARTIÇÃO 3: Assento bloqueado por distribuição de peso (retorno 2)
    @Test
    void testAssentoBloqueadoEntre101e200Passageiros() {
        // Ocupa 150 assentos: 7 filas completas (140) + 10 assentos da 8ª fila
        for (int i = 40; i <= 46; i++) {
            for (int j = 0; j < 20; j++) {
                barca.ocupaLugar(i, j);
            }
        }
        for (int j = 0; j < 10; j++) {
            barca.ocupaLugar(47, j);
        }

        // Com 150 passageiros (101-200), filas < 40 são bloqueadas
        assertEquals(2, barca.ocupaLugar("F01A01")); // fila 1 < 40, bloqueada
        assertEquals(2, barca.ocupaLugar("F39A19")); // fila 39 < 40, bloqueada
        assertEquals(2, barca.ocupaLugar("F00A00")); // fila 0 < 40, bloqueada
    }

    @Test
    void testAssentoBloqueadoExatamente101Passageiros() {
        // Ocupa exatamente 101 assentos
        for (int i = 40; i <= 44; i++) {
            for (int j = 0; j < 20; j++) {
                barca.ocupaLugar(i, j);
            }
        }
        barca.ocupaLugar(45, 0); // 101º assento

        // Com 101 passageiros, filas < 40 são bloqueadas
        assertEquals(2, barca.ocupaLugar("F20A10")); // fila 20 < 40, bloqueada
    }

    // PARTIÇÃO 4: Assento atribuído com sucesso (retorno 3)
    @Test
    void testAssentoValidoComAte100Passageiros() {
        // Com barca vazia, filas 0-20 são permitidas
        assertEquals(3, barca.ocupaLugar("F00A00")); // fila 0 <= 20, permitida
        assertEquals(3, barca.ocupaLugar("F20A19")); // fila 20 <= 20, permitida
        assertEquals(3, barca.ocupaLugar("F10A10")); // fila 10 <= 20, permitida
    }

    @Test
    void testAssentoValidoEntre101e200Passageiros() {
        // Ocupa 150 assentos nas filas 40-47
        for (int i = 40; i <= 46; i++) {
            for (int j = 0; j < 20; j++) {
                barca.ocupaLugar(i, j);
            }
        }
        for (int j = 0; j < 10; j++) {
            barca.ocupaLugar(47, j);
        }

        // Com 150 passageiros, filas >= 40 são permitidas
        assertEquals(3, barca.ocupaLugar("F40A01")); // fila 40 >= 40, permitida
        assertEquals(3, barca.ocupaLugar("F59A01")); // fila 59 >= 40, permitida
    }

    @Test
    void testAssentoValidoComMaisDe200Passageiros() {
        // Ocupa 250 assentos: 12 filas completas + 10 assentos extras
        for (int i = 40; i <= 51; i++) {
            for (int j = 0; j < 20; j++) {
                barca.ocupaLugar(i, j);
            }
        }
        for (int j = 0; j < 10; j++) {
            barca.ocupaLugar(52, j);
        }

        // Com > 200 passageiros, qualquer fila disponível é permitida
        assertEquals(3, barca.ocupaLugar("F01A01")); // qualquer fila disponível
        assertEquals(3, barca.ocupaLugar("F39A01")); // qualquer fila disponível
        assertEquals(3, barca.ocupaLugar("F53A01")); // qualquer fila disponível
    }

    // TESTES DE VALORES LIMITE
    @Test
    void testValoresLimiteValidosMinimos() {
        assertEquals(3, barca.ocupaLugar("F00A00")); // valores mínimos válidos (0,0)
    }

    @Test
    void testValoresLimiteValidosMaximos() {
        // Valores máximos suportados pela regex [0-8]{2}
        assertEquals(3, barca.ocupaLugar("F88A88")); // máximo aceito pela regex mas inválido por limites
        // Na verdade, este deve retornar 0 porque fila 88 > 60 e assento 88 >= 20
        assertEquals(0, barca.ocupaLugar("F88A88"));

        // Valores máximos realmente válidos dentro dos arrays
        assertEquals(3, barca.ocupaLugar("F59A19")); // máximos reais válidos (mas fila > 20 com 0 passageiros = restrita)
        // Este deve retornar 1 porque fila 59 > 20 com <= 100 passageiros
        assertEquals(1, barca.ocupaLugar("F59A19"));
    }

    @Test
    void testTransicaoPassageiros() {
        // Testa transições críticas entre as regras

        // Exatamente 100 passageiros
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 20; j++) {
                barca.ocupaLugar(i, j);
            }
        }
        assertEquals(3, barca.ocupaLugar("F20A01")); // 100 passageiros, fila 20 ainda permitida

        // Agora 101 passageiros - deve bloquear filas < 40
        assertEquals(2, barca.ocupaLugar("F39A01")); // 101 passageiros, fila 39 < 40 bloqueada
    }
}