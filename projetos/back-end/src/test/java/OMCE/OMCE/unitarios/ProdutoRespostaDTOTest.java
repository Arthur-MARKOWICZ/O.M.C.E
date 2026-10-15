package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.ProdutoRespostaDTO;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.utils.ProdutoTestFactory;
import org.junit.jupiter.api.Test;

import static OMCE.OMCE.Produto.enums.Categoria.ESP32;
import static OMCE.OMCE.Produto.enums.Condicao.USADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProdutoRespostaDTOTest {

    private User vendedor() {
        DadosEndereco endereco = new DadosEndereco("8123434", "brasil", "test", "test", "Rua test");
        User vendedor = new User(new DadosCadastroUser(
                "vendedor teste", "12345678912", "22-06-2005", "test",
                endereco, "test@gmail.com", "1231313139", "testUser", "test"));
        vendedor.setId(7L);
        return vendedor;
    }

    private Produto produtoDe(User vendedor) {
        Produto produto = ProdutoTestFactory.produto(ProdutoTestFactory.dados(
                "Sensor", 10, "detalhes", 7L, "10", "image/png", ESP32, USADO));
        produto.setId(1L);
        produto.setPreco(25.5);
        produto.setUsuario(vendedor);
        return produto;
    }

    @Test
    void deveUsarNomeEIdDoVendedorQuandoEleExiste() {
        ProdutoRespostaDTO dto = new ProdutoRespostaDTO(produtoDe(vendedor()));

        assertEquals("vendedor teste", dto.nomeUsuario());
        assertEquals(Long.valueOf(7L), dto.id_vendedor());
    }

    @Test
    void deveMostrarUsuarioRemovidoEmTodosOsConstrutoresQuandoNaoHaVendedor() {
        Produto produto = produtoDe(null);

        ProdutoRespostaDTO simples = new ProdutoRespostaDTO(produto);
        ProdutoRespostaDTO comValor = new ProdutoRespostaDTO(produto, 30.0);
        ProdutoRespostaDTO comFrete = new ProdutoRespostaDTO(produto, 30.0, 9.9);

        for (ProdutoRespostaDTO dto : new ProdutoRespostaDTO[]{simples, comValor, comFrete}) {
            assertEquals("Usuário removido", dto.nomeUsuario());
            assertNull(dto.id_vendedor());
        }
        assertEquals(Double.valueOf(9.9), comFrete.valorFrete());
    }
}