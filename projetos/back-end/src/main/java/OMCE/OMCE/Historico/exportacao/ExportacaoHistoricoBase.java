package OMCE.OMCE.Historico.exportacao;

import OMCE.OMCE.Execao.PeriodoInvalido;
import OMCE.OMCE.Historico.exportacao.dto.ArquivoExportado;
import OMCE.OMCE.Historico.exportacao.dto.LinhaHistoricoDTO;
import OMCE.OMCE.Historico.exportacao.dto.PeriodoExportacao;
import OMCE.OMCE.Pagamento.Pagamento;
import OMCE.OMCE.Pagamento.repository.PagamentoRepository;
import OMCE.OMCE.Pedido.ItemPedido;
import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Classe base das estrategias de exportacao. Concentra o filtro de data da compra
 * (validacao do intervalo e a consulta ja filtrada) e deixa para as subclasses
 * apenas a geracao do arquivo no formato especifico.
 */
public abstract class ExportacaoHistoricoBase implements ExportacaoStrategy {

    protected static final DateTimeFormatter DATA_HORA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    protected static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATA_ARQUIVO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ItemPedidoRepository itemPedidoRepository;
    private final PagamentoRepository pagamentoRepository;

    protected ExportacaoHistoricoBase(ItemPedidoRepository itemPedidoRepository, PagamentoRepository pagamentoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public final ArquivoExportado exportar(Long compradorId, LocalDate dataInicio, LocalDate dataFim) {
        PeriodoExportacao periodo = montarPeriodo(dataInicio, dataFim);
        List<LinhaHistoricoDTO> linhas = buscarCompras(compradorId, periodo);
        return new ArquivoExportado(nomeArquivo(periodo), getContentType(), gerar(linhas, periodo));
    }

    /** Filtro de data: valida o intervalo informado e normaliza para dia inteiro. */
    protected PeriodoExportacao montarPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new PeriodoInvalido("Informe a data de inicio e a data de fim.");
        }
        if (fim.isBefore(inicio)) {
            throw new PeriodoInvalido("A data de fim nao pode ser anterior a data de inicio.");
        }
        return new PeriodoExportacao(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));
    }

    protected List<LinhaHistoricoDTO> buscarCompras(Long compradorId, PeriodoExportacao periodo) {
        List<ItemPedido> itens = itemPedidoRepository.pegarComprasNoPeriodo(compradorId, periodo.inicio(), periodo.fim());

        List<Long> pedidoIds = itens.stream().map(item -> item.getPedido().getId()).distinct().toList();
        Map<Long, String> metodoPorPedido = pagamentoRepository.findByPedidoIdIn(pedidoIds).stream()
                .collect(Collectors.toMap(
                        pagamento -> pagamento.getPedido().getId(),
                        pagamento -> pagamento.getMetodoPagamento().getDescricao(),
                        (primeiro, segundo) -> primeiro));

        return itens.stream()
                .map(item -> new LinhaHistoricoDTO(item, metodoPorPedido.getOrDefault(item.getPedido().getId(), "")))
                .toList();
    }

    protected String nomeArquivo(PeriodoExportacao periodo) {
        return "historico-compras_%s_a_%s.%s".formatted(
                periodo.inicio().format(DATA_ARQUIVO),
                periodo.fim().format(DATA_ARQUIVO),
                getExtensao());
    }

    protected String descricaoPeriodo(PeriodoExportacao periodo) {
        return "Período: %s a %s".formatted(periodo.inicio().format(DATA_BR), periodo.fim().format(DATA_BR));
    }

    protected double totalGeral(List<LinhaHistoricoDTO> linhas) {
        return linhas.stream().mapToDouble(LinhaHistoricoDTO::total).sum();
    }

    /** Unico ponto de variacao entre as estrategias: montar os bytes do arquivo. */
    protected abstract byte[] gerar(List<LinhaHistoricoDTO> linhas, PeriodoExportacao periodo);
}