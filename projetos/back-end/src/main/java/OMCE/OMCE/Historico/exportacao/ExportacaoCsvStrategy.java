package OMCE.OMCE.Historico.exportacao;

import OMCE.OMCE.Historico.exportacao.dto.LinhaHistoricoDTO;
import OMCE.OMCE.Historico.exportacao.dto.PeriodoExportacao;
import OMCE.OMCE.Pagamento.repository.PagamentoRepository;
import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/** Estrategia CSV: separador ';' e BOM UTF-8 para o Excel em pt-BR abrir corretamente. */
@Component
public class ExportacaoCsvStrategy extends ExportacaoHistoricoBase {

    private static final String SEPARADOR = ";";
    private static final String BOM = "﻿";

    public ExportacaoCsvStrategy(ItemPedidoRepository itemPedidoRepository, PagamentoRepository pagamentoRepository) {
        super(itemPedidoRepository, pagamentoRepository);
    }

    @Override
    protected byte[] gerar(List<LinhaHistoricoDTO> linhas, PeriodoExportacao periodo) {
        StringBuilder csv = new StringBuilder(BOM);
        linha(csv, "Pedido", "Data", "Meio de pagamento", "Produto", "Vendedor", "Categoria", "Condição", "Qtd", "Preço unitário", "Total");
        for (LinhaHistoricoDTO item : linhas) {
            linha(csv,
                    String.valueOf(item.pedidoId()),
                    item.dataCompra() != null ? item.dataCompra().format(DATA_HORA_BR) : "",
                    item.metodoPagamento(),
                    item.produto(),
                    item.vendedor(),
                    item.categoria(),
                    item.condicao(),
                    String.valueOf(item.quantidade()),
                    numero(item.precoUnitario()),
                    numero(item.total()));
        }
        linha(csv, "", "", "", "", "", "", "", "", "Total geral", numero(totalGeral(linhas)));
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void linha(StringBuilder csv, String... campos) {
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) csv.append(SEPARADOR);
            csv.append(escapar(campos[i]));
        }
        csv.append("\r\n");
    }

    private String escapar(String valor) {
        if (valor == null) return "";
        if (valor.contains(SEPARADOR) || valor.contains("\"") || valor.contains("\n") || valor.contains("\r")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }

    private String numero(double valor) {
        return String.format(Locale.of("pt", "BR"), "%.2f", valor);
    }

    @Override
    public FormatoExportacao getFormato() {
        return FormatoExportacao.CSV;
    }

    @Override
    public String getContentType() {
        return "text/csv;charset=UTF-8";
    }

    @Override
    public String getExtensao() {
        return "csv";
    }
}