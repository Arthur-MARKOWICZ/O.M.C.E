package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Historico.HistoricoService;
import OMCE.OMCE.Pedido.dto.PedidoCadastroDTO;
import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import OMCE.OMCE.Pedido.repository.PedidoRepository;
import OMCE.OMCE.Pedido.service.PedidoService;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.dto.ProdutoRespostaDTO;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.Produto.service.ProdutoService;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.repository.UserRepository;
import org.aspectj.weaver.ast.Var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static OMCE.OMCE.Produto.enums.Categoria.ESP32;
import static OMCE.OMCE.Produto.enums.Condicao.USADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricoServiceTest {

    @InjectMocks
    private HistoricoService service;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    private Produto produtoCadastro;
    private User usuarioCadastro;

    @BeforeEach
    void setup() {

        DadosEndereco dadosEndereco = new DadosEndereco(
                "8123434", "brasil", "test", "test", "Rua test"
        );

        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser(
                "test", "12345678912", "22-06-2005", "test",
                dadosEndereco, "test@gmail.com", "1231313139",
                "testUser", "test"
        );

        usuarioCadastro = new User(dadosCadastroUser);
        usuarioCadastro.setId(1L);


        DadosCadastroProduto dadosProduto = new DadosCadastroProduto(
                "test", 10, "test", 1L, "10", "10", ESP32, USADO
        );

        produtoCadastro = new Produto(dadosProduto);
        produtoCadastro.setId(1L);
        Page<Produto> pageVendas = new PageImpl<>(List.of(produtoCadastro));
        lenient().when(produtoRepository.pegarVendas(1L, Pageable.unpaged())).thenReturn(pageVendas);
        Page<Produto> pageCompras = new PageImpl<>(List.of(produtoCadastro));
        lenient().when(itemPedidoRepository.pegarProdutosDoUsuario(1L, Pageable.unpaged()))
                .thenReturn(pageCompras);
    }

    @Test
    void DeveObterOHistoricoDeVenda() {
        Page<ProdutoRespostaDTO> historicoVenda = service.pegarHistoricoDeVenda(1L, Pageable.unpaged());

        assertNotNull(historicoVenda);
        assertEquals(1, historicoVenda.getContent().size());
        assertEquals(produtoCadastro.getId(), historicoVenda.getContent().get(0).id());
    }

    @Test
    void DeveObterOHistoricoDeCompra() {
        Page<ProdutoRespostaDTO> historicoCompra = service.pegarHistoricoDeCompra(1L, Pageable.unpaged());

        assertNotNull(historicoCompra);
        assertEquals(1, historicoCompra.getContent().size());
        assertEquals(produtoCadastro.getId(), historicoCompra.getContent().get(0).id());
    }
}