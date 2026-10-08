package OMCE.reuso.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class StrategyRegistryTest {

    enum Tipo { A, B, C }

    interface Acao {
        Tipo tipo();
    }

    private static Acao acao(Tipo tipo) {
        return () -> tipo;
    }

    @Test
    void escolheEstrategiaPelaChave() {
        Acao a = acao(Tipo.A);
        Acao b = acao(Tipo.B);
        StrategyRegistry<Tipo, Acao> registro =
                new StrategyRegistry<>(List.of(a, b), Acao::tipo, "Tipo de acao");

        assertSame(a, registro.escolher(Tipo.A));
        assertSame(b, registro.escolher(Tipo.B));
    }

    @Test
    void chaveNaoRegistradaLancaIllegalArgumentComMensagemDescritiva() {
        StrategyRegistry<Tipo, Acao> registro =
                new StrategyRegistry<>(List.of(acao(Tipo.A)), Acao::tipo, "Tipo de acao");

        IllegalArgumentException erro =
                assertThrows(IllegalArgumentException.class, () -> registro.escolher(Tipo.C));
        assertEquals("Tipo de acao nao suportado: C", erro.getMessage());
    }

    @Test
    void chaveNulaTambemELancaIllegalArgument() {
        StrategyRegistry<Tipo, Acao> registro =
                new StrategyRegistry<>(List.of(acao(Tipo.A)), Acao::tipo, "Tipo de acao");

        assertThrows(IllegalArgumentException.class, () -> registro.escolher(null));
    }

    @Test
    void chaveDuplicadaImpedeACriacao() {
        assertThrows(IllegalStateException.class, () ->
                new StrategyRegistry<>(List.of(acao(Tipo.A), acao(Tipo.A)), Acao::tipo, "Tipo de acao"));
    }

    @Test
    void suportaETodasRefletemOQueFoiRegistrado() {
        StrategyRegistry<Tipo, Acao> registro =
                new StrategyRegistry<>(List.of(acao(Tipo.A), acao(Tipo.B)), Acao::tipo, "Tipo de acao");

        assertTrue(registro.suporta(Tipo.A));
        assertFalse(registro.suporta(Tipo.C));
        assertEquals(2, registro.todas().size());
    }

    @Test
    void listaVaziaCriaRegistroSemEstrategias() {
        StrategyRegistry<Tipo, Acao> registro =
                new StrategyRegistry<>(List.of(), Acao::tipo, "Tipo de acao");

        assertTrue(registro.todas().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> registro.escolher(Tipo.A));
    }
}
