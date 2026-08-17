package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Pedido.ItemPedido;
import OMCE.OMCE.Pedido.Pedido;
import OMCE.OMCE.Pedido.dto.PedidoCadastroDTO;
import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import OMCE.OMCE.Pedido.repository.PedidoRepository;
import OMCE.OMCE.Pedido.service.PedidoService;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Test
    public void DeveCadastrarPedidoComTodosOsDadosCorretos() {
        ArrayList<Long> idsProdutos = new ArrayList<>();
        idsProdutos.add(1L);
        idsProdutos.add(2L);

        DadosEndereco endereco = new DadosEndereco(
                "9991-140",
                "Brasil",
                "São Paulo",
                "SP",
                "12345678"
        );

        PedidoCadastroDTO dto = new PedidoCadastroDTO(
                idsProdutos,
                10L,
                250.75,
                endereco
        );

        Pedido pedidoSalvo = new Pedido(dto);
        pedidoSalvo.setId(99L);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);
        when(produtoRepository.getReferenceById(any(Long.class))).thenReturn(new Produto());
        doNothing().when(produtoRepository).produtoVendido(any(Long.class));

        pedidoService.CadastroCompra(dto);

        verify(pedidoRepository, times(1)).save(any(Pedido.class));

        verify(produtoRepository, times(2)).produtoVendido(any(Long.class));

        verify(itemPedidoRepository, times(2)).save(any(ItemPedido.class));
    }
}
