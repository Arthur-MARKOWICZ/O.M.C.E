package OMCE.OMCE.Pagamento.service;

import OMCE.OMCE.Execao.PagamentoNaoEncontrado;
import OMCE.OMCE.Pagamento.Pagamento;
import OMCE.OMCE.Pagamento.dto.PagamentoRespostaDTO;
import OMCE.OMCE.Pagamento.enums.MetodoPagamento;
import OMCE.OMCE.Pagamento.repository.PagamentoRepository;
import OMCE.OMCE.Pagamento.strategy.PagamentoContext;
import OMCE.OMCE.Pedido.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoContext pagamentoContext;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    public Pagamento registrarPagamento(Pedido pedido, MetodoPagamento metodo, double valor) {
        Pagamento pagamento = pagamentoContext.escolher(metodo).processar(pedido, valor);
        return pagamentoRepository.save(pagamento);
    }

    public PagamentoRespostaDTO buscarPorPedido(Long pedidoId) {
        Pagamento pagamento = pagamentoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado para o pedido: " + pedidoId));
        return toDTO(pagamento);
    }

    private PagamentoRespostaDTO toDTO(Pagamento pagamento) {
        return new PagamentoRespostaDTO(
                pagamento.getId(),
                pagamento.getPedido().getId(),
                pagamento.getMetodoPagamento(),
                pagamento.getValor(),
                pagamento.getProtocolo(),
                pagamento.getStatus(),
                pagamento.getDataPagamento()
        );
    }
}