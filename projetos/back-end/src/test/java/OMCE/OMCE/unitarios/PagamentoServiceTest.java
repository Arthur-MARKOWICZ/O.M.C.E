package OMCE.OMCE.unitarios;

import OMCE.OMCE.Execao.AcessoNegado;
import OMCE.OMCE.Execao.PagamentoNaoEncontrado;
import OMCE.OMCE.Pagamento.Pagamento;
import OMCE.OMCE.Pagamento.dto.PagamentoRespostaDTO;
import OMCE.OMCE.Pagamento.enums.MetodoPagamento;
import OMCE.OMCE.Pagamento.enums.StatusPagamento;
import OMCE.OMCE.Pagamento.repository.PagamentoRepository;
import OMCE.OMCE.Pagamento.strategy.CartaoCreditoPagamento;
import OMCE.OMCE.Pagamento.strategy.CartaoDebitoPagamento;
import OMCE.OMCE.Pagamento.strategy.PagamentoContext;
import OMCE.OMCE.Pagamento.strategy.PixPagamento;
import OMCE.OMCE.Pagamento.service.PagamentoService;
import OMCE.OMCE.Pedido.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    private PagamentoService pagamentoService;

    @BeforeEach
    void setUp() {
        PagamentoContext context = new PagamentoContext(
                List.of(new CartaoCreditoPagamento(), new CartaoDebitoPagamento(), new PixPagamento())
        );
        pagamentoService = new PagamentoService();
        setField(pagamentoService, "pagamentoContext", context);
        setField(pagamentoService, "pagamentoRepository", pagamentoRepository);
    }

    @Test
    public void DeveEscolherEstrategiaDeCartaoDeCreditoEGerarProtocolo() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));

        Pagamento pagamento = pagamentoService.registrarPagamento(pedido, MetodoPagamento.CARTAO_CREDITO, 150.0);

        assertEquals(MetodoPagamento.CARTAO_CREDITO, pagamento.getMetodoPagamento());
        assertEquals(StatusPagamento.APROVADO, pagamento.getStatus());
        assertNotNull(pagamento.getProtocolo());
        assertTrue(pagamento.getProtocolo().startsWith("CC-"));
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    public void DeveEscolherEstrategiaDePixEGerarProtocolo() {
        Pedido pedido = new Pedido();
        pedido.setId(2L);
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));

        Pagamento pagamento = pagamentoService.registrarPagamento(pedido, MetodoPagamento.PIX, 89.9);

        assertEquals(MetodoPagamento.PIX, pagamento.getMetodoPagamento());
        assertEquals(StatusPagamento.APROVADO, pagamento.getStatus());
        assertNotNull(pagamento.getProtocolo());
        assertTrue(pagamento.getProtocolo().startsWith("PIX-"));
    }

    @Test
    public void DeveEscolherEstrategiaDeCartaoDeDebitoEGerarProtocolo() {
        Pedido pedido = new Pedido();
        pedido.setId(6L);
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));

        Pagamento pagamento = pagamentoService.registrarPagamento(pedido, MetodoPagamento.CARTAO_DEBITO, 42.0);

        assertEquals(MetodoPagamento.CARTAO_DEBITO, pagamento.getMetodoPagamento());
        assertEquals(StatusPagamento.APROVADO, pagamento.getStatus());
        assertNotNull(pagamento.getProtocolo());
        assertTrue(pagamento.getProtocolo().startsWith("CD-"));
    }

    @Test
    public void DeveRetornarPagamentoQuandoUsuarioForODonoDoPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(3L);
        pedido.setCompradorId(10L);
        Pagamento pagamento = new Pagamento(pedido, MetodoPagamento.PIX, 50.0);
        when(pagamentoRepository.findByPedidoId(3L)).thenReturn(Optional.of(pagamento));

        PagamentoRespostaDTO dto = pagamentoService.buscarPorPedido(3L, 10L);

        assertEquals(3L, dto.pedidoId());
        assertEquals(MetodoPagamento.PIX, dto.metodoPagamento());
    }

    @Test
    public void DeveNegarAcessoQuandoUsuarioNaoForODonoDoPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(4L);
        pedido.setCompradorId(10L);
        Pagamento pagamento = new Pagamento(pedido, MetodoPagamento.PIX, 50.0);
        when(pagamentoRepository.findByPedidoId(4L)).thenReturn(Optional.of(pagamento));

        assertThrows(AcessoNegado.class, () -> pagamentoService.buscarPorPedido(4L, 99L));
    }

    @Test
    public void DeveLancarNaoEncontradoQuandoPedidoNaoTemPagamento() {
        when(pagamentoRepository.findByPedidoId(5L)).thenReturn(Optional.empty());

        assertThrows(PagamentoNaoEncontrado.class, () -> pagamentoService.buscarPorPedido(5L, 10L));
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}