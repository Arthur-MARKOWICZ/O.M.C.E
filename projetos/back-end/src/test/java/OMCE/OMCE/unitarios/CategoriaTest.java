package OMCE.OMCE.unitarios;

import OMCE.OMCE.Execao.CategoriaInvalida;
import OMCE.OMCE.Produto.enums.Categoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoriaTest {

    @Test
    void deveConverterOTextoIgnorandoCaixaEEspacos() {
        assertEquals(Categoria.ESP32, Categoria.deTexto(" esp32 "));
        assertEquals(Categoria.SENSORES, Categoria.deTexto("Sensores"));
    }

    @Test
    void deveTratarTextoVazioComoSemFiltro() {
        assertNull(Categoria.deTexto(null));
        assertNull(Categoria.deTexto(""));
        assertNull(Categoria.deTexto("   "));
    }

    @Test
    void deveRecusarCategoriaDesconhecida() {
        CategoriaInvalida erro = assertThrows(CategoriaInvalida.class, () -> Categoria.deTexto("XYZ"));

        assertEquals("Categoria invalida: XYZ", erro.getMessage());
    }
}