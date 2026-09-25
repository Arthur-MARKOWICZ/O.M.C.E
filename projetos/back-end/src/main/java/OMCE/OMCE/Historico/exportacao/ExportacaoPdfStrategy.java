package OMCE.OMCE.Historico.exportacao;

import OMCE.OMCE.Historico.exportacao.dto.LinhaHistoricoDTO;
import OMCE.OMCE.Historico.exportacao.dto.PeriodoExportacao;
import OMCE.OMCE.Pagamento.repository.PagamentoRepository;
import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

/** Estrategia PDF: alem das colunas comuns, cada linha traz a imagem do produto. */
@Component
public class ExportacaoPdfStrategy extends ExportacaoHistoricoBase {

    private static final String[] COLUNAS =
            {"Imagem", "Pedido", "Data", "Meio de pagamento", "Produto", "Vendedor", "Categoria", "Condição", "Qtd", "Preço unitário", "Total"};
    private static final float[] LARGURAS = {1.1f, 0.8f, 1.5f, 1.3f, 2.6f, 1.7f, 1.3f, 1f, 0.6f, 1.3f, 1.2f};
    private static final float TAMANHO_IMAGEM = 55f;

    private static final Font FONTE_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font FONTE_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
    private static final Font FONTE_CABECALHO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
    private static final Font FONTE_CELULA = FontFactory.getFont(FontFactory.HELVETICA, 8);
    private static final Font FONTE_VAZIA = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7, Color.GRAY);

    public ExportacaoPdfStrategy(ItemPedidoRepository itemPedidoRepository, PagamentoRepository pagamentoRepository) {
        super(itemPedidoRepository, pagamentoRepository);
    }

    @Override
    protected byte[] gerar(List<LinhaHistoricoDTO> linhas, PeriodoExportacao periodo) {
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4.rotate(), 28f, 28f, 28f, 28f);
        PdfWriter.getInstance(documento, saida);
        documento.open();
        try {

            Paragraph titulo = new Paragraph("Histórico de compras", FONTE_TITULO);
            titulo.setSpacingAfter(4f);
            documento.add(titulo);
            Paragraph subtitulo = new Paragraph(descricaoPeriodo(periodo), FONTE_SUBTITULO);
            subtitulo.setSpacingAfter(14f);
            documento.add(subtitulo);

            PdfPTable tabela = new PdfPTable(COLUNAS.length);
            tabela.setWidthPercentage(100f);
            tabela.setWidths(LARGURAS);
            tabela.setHeaderRows(1);
            for (String coluna : COLUNAS) {
                tabela.addCell(celulaCabecalho(coluna));
            }

            for (LinhaHistoricoDTO item : linhas) {
                tabela.addCell(celulaImagem(item));
                tabela.addCell(celulaTexto(String.valueOf(item.pedidoId())));
                tabela.addCell(celulaTexto(item.dataCompra() != null ? item.dataCompra().format(DATA_HORA_BR) : ""));
                tabela.addCell(celulaTexto(item.metodoPagamento()));
                tabela.addCell(celulaTexto(item.produto()));
                tabela.addCell(celulaTexto(item.vendedor()));
                tabela.addCell(celulaTexto(item.categoria()));
                tabela.addCell(celulaTexto(item.condicao()));
                tabela.addCell(celulaTexto(String.valueOf(item.quantidade())));
                tabela.addCell(celulaTexto(moeda(item.precoUnitario())));
                tabela.addCell(celulaTexto(moeda(item.total())));
            }

            PdfPCell rotuloTotal = new PdfPCell(new Phrase("Total geral", FONTE_CABECALHO));
            rotuloTotal.setColspan(COLUNAS.length - 1);
            rotuloTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rotuloTotal.setPadding(5f);
            tabela.addCell(rotuloTotal);
            PdfPCell valorTotal = new PdfPCell(new Phrase(moeda(totalGeral(linhas)), FONTE_CABECALHO));
            valorTotal.setPadding(5f);
            tabela.addCell(valorTotal);

            documento.add(tabela);
        } catch (DocumentException e) {
            throw new IllegalStateException("Nao foi possivel gerar o PDF do historico.", e);
        } finally {
            documento.close();
        }
        return saida.toByteArray();
    }

    /**
     * Os bytes vem de um LONGBLOB alimentado por Base64 enviado pelo cliente, entao
     * podem estar ausentes ou corrompidos: nesse caso a celula vira um aviso e o
     * relatorio continua sendo gerado.
     */
    private PdfPCell celulaImagem(LinhaHistoricoDTO item) {
        if (item.imagem() != null && item.imagem().length > 0) {
            try {
                Image imagem = Image.getInstance(item.imagem());
                imagem.scaleToFit(TAMANHO_IMAGEM, TAMANHO_IMAGEM);
                PdfPCell celula = new PdfPCell(imagem, false);
                celula.setPadding(4f);
                celula.setHorizontalAlignment(Element.ALIGN_CENTER);
                celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
                return celula;
            } catch (Exception imagemInvalida) {
                // formato nao suportado ou bytes corrompidos: cai no aviso abaixo
            }
        }
        PdfPCell celula = new PdfPCell(new Phrase("sem imagem", FONTE_VAZIA));
        celula.setPadding(5f);
        celula.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return celula;
    }

    private PdfPCell celulaCabecalho(String texto) {
        PdfPCell celula = new PdfPCell(new Phrase(texto, FONTE_CABECALHO));
        celula.setBackgroundColor(new Color(232, 240, 234));
        celula.setPadding(5f);
        return celula;
    }

    private PdfPCell celulaTexto(String texto) {
        PdfPCell celula = new PdfPCell(new Phrase(texto != null ? texto : "", FONTE_CELULA));
        celula.setPadding(5f);
        celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return celula;
    }

    private String moeda(double valor) {
        return String.format(Locale.of("pt", "BR"), "R$ %.2f", valor);
    }

    @Override
    public FormatoExportacao getFormato() {
        return FormatoExportacao.PDF;
    }

    @Override
    public String getContentType() {
        return "application/pdf";
    }

    @Override
    public String getExtensao() {
        return "pdf";
    }
}