package OMCE.OMCE.Historico.exportacao;

import OMCE.OMCE.Historico.exportacao.dto.LinhaHistoricoDTO;
import OMCE.OMCE.Historico.exportacao.dto.PeriodoExportacao;
import OMCE.OMCE.Pagamento.repository.PagamentoRepository;
import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

/** Estrategia XLSX, montada com Apache POI. */
@Component
public class ExportacaoXlsxStrategy extends ExportacaoHistoricoBase {

    private static final String[] COLUNAS =
            {"Pedido", "Data", "Meio de pagamento", "Produto", "Vendedor", "Categoria", "Condição", "Qtd", "Preço unitário", "Total"};

    public ExportacaoXlsxStrategy(ItemPedidoRepository itemPedidoRepository, PagamentoRepository pagamentoRepository) {
        super(itemPedidoRepository, pagamentoRepository);
    }

    @Override
    protected byte[] gerar(List<LinhaHistoricoDTO> linhas, PeriodoExportacao periodo) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream saida = new ByteArrayOutputStream()) {
            Sheet planilha = workbook.createSheet("Histórico de compras");
            CellStyle estiloCabecalho = estiloCabecalho(workbook);
            CellStyle estiloMoeda = estiloMoeda(workbook);

            Row cabecalho = planilha.createRow(0);
            for (int coluna = 0; coluna < COLUNAS.length; coluna++) {
                var celula = cabecalho.createCell(coluna);
                celula.setCellValue(COLUNAS[coluna]);
                celula.setCellStyle(estiloCabecalho);
            }

            int numeroLinha = 1;
            for (LinhaHistoricoDTO item : linhas) {
                Row linha = planilha.createRow(numeroLinha++);
                linha.createCell(0).setCellValue(item.pedidoId() != null ? item.pedidoId() : 0);
                linha.createCell(1).setCellValue(item.dataCompra() != null ? item.dataCompra().format(DATA_HORA_BR) : "");
                linha.createCell(2).setCellValue(item.metodoPagamento());
                linha.createCell(3).setCellValue(item.produto());
                linha.createCell(4).setCellValue(item.vendedor());
                linha.createCell(5).setCellValue(item.categoria());
                linha.createCell(6).setCellValue(item.condicao());
                linha.createCell(7).setCellValue(item.quantidade());
                var precoUnitario = linha.createCell(8);
                precoUnitario.setCellValue(item.precoUnitario());
                precoUnitario.setCellStyle(estiloMoeda);
                var total = linha.createCell(9);
                total.setCellValue(item.total());
                total.setCellStyle(estiloMoeda);
            }

            Row rodape = planilha.createRow(numeroLinha);
            var rotulo = rodape.createCell(8);
            rotulo.setCellValue("Total geral");
            rotulo.setCellStyle(estiloCabecalho);
            var celulaTotalGeral = rodape.createCell(9);
            celulaTotalGeral.setCellValue(totalGeral(linhas));
            celulaTotalGeral.setCellStyle(estiloMoeda);

            for (int coluna = 0; coluna < COLUNAS.length; coluna++) {
                planilha.autoSizeColumn(coluna);
            }

            workbook.write(saida);
            return saida.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Nao foi possivel gerar a planilha do historico.", e);
        }
    }

    private CellStyle estiloCabecalho(Workbook workbook) {
        CellStyle estilo = workbook.createCellStyle();
        Font fonte = workbook.createFont();
        fonte.setBold(true);
        estilo.setFont(fonte);
        return estilo;
    }

    private CellStyle estiloMoeda(Workbook workbook) {
        CellStyle estilo = workbook.createCellStyle();
        estilo.setDataFormat(workbook.createDataFormat().getFormat("R$ #,##0.00"));
        return estilo;
    }

    @Override
    public FormatoExportacao getFormato() {
        return FormatoExportacao.XLSX;
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    @Override
    public String getExtensao() {
        return "xlsx";
    }
}