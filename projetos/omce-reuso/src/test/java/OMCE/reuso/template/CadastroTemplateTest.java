package OMCE.reuso.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CadastroTemplateTest {

    /** Entidade e dados de brincadeira, so para exercitar o template. */
    static class Item {
        String nome;
        String dono;
        String tipo;
    }

    record Dados(String nome, String tipo) { }

    /** Implementacao que registra a ordem em que cada passo foi chamado. */
    static class CadastroItem extends CadastroTemplate<Dados, String, Item> {
        final List<String> passos = new ArrayList<>();

        @Override
        protected void validar(Dados dados) {
            passos.add("validar");
            if (dados.nome() == null) {
                throw new IllegalArgumentException("nome obrigatorio");
            }
        }

        @Override
        protected String buscarUsuario(Dados dados) {
            passos.add("buscarUsuario");
            return "maria";
        }

        @Override
        protected Item criar(Dados dados) {
            passos.add("criar");
            Item item = new Item();
            item.nome = dados.nome();
            return item;
        }

        @Override
        protected void definirUsuario(Item item, String usuario) {
            passos.add("definirUsuario");
            item.dono = usuario;
        }

        @Override
        protected void configurar(Item item, Dados dados) {
            passos.add("configurar");
            item.tipo = dados.tipo();
        }

        @Override
        protected Item salvar(Item item) {
            passos.add("salvar");
            return item;
        }
    }

    @Test
    void executaOsPassosNaOrdemDoTemplate() {
        CadastroItem cadastro = new CadastroItem();

        cadastro.cadastrar(new Dados("sensor", "temperatura"));

        assertEquals(
                List.of("validar", "buscarUsuario", "criar", "definirUsuario", "configurar", "salvar"),
                cadastro.passos);
    }

    @Test
    void devolveAEntidadeMontadaPelosPassos() {
        Item item = new CadastroItem().cadastrar(new Dados("sensor", "temperatura"));

        assertEquals("sensor", item.nome);
        assertEquals("maria", item.dono);
        assertEquals("temperatura", item.tipo);
    }

    @Test
    void validacaoInvalidaInterrompeOFluxoAntesDeSalvar() {
        CadastroItem cadastro = new CadastroItem();

        assertThrows(IllegalArgumentException.class,
                () -> cadastro.cadastrar(new Dados(null, "x")));

        assertEquals(List.of("validar"), cadastro.passos);
        assertTrue(cadastro.passos.stream().noneMatch("salvar"::equals));
    }
}
