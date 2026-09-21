package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Execao.PeriodoInvalido;
import OMCE.OMCE.Historico.exportacao.ExportacaoContext;
import OMCE.OMCE.Historico.exportacao.ExportacaoCsvStrategy;
import OMCE.OMCE.Historico.exportacao.ExportacaoPdfStrategy;
import OMCE.OMCE.Historico.exportacao.ExportacaoXlsxStrategy;
import OMCE.OMCE.Historico.exportacao.FormatoExportacao;
import OMCE.OMCE.Historico.exportacao.dto.ArquivoExportado;
import OMCE.OMCE.Pagamento.Pagamento;
import OMCE.OMCE.Pagamento.enums.MetodoPagamento;
import OMCE.OMCE.Pagamento.repository.PagamentoRepository;
import OMCE.OMCE.Pedido.ItemPedido;
import OMCE.OMCE.Pedido.Pedido;
import OMCE.OMCE.Pedido.dto.PedidoCadastroDTO;
import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.utils.ProdutoTestFactory;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static OMCE.OMCE.Produto.enums.Categoria.ESP32;
import static OMCE.OMCE.Produto.enums.Condicao.USADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExportacaoHistoricoTest {

    private static final LocalDate INICIO = LocalDate.of(2025, 1, 1);
    private static final LocalDate FIM = LocalDate.of(2025, 1, 31);

    @Mock
    private ItemPedidoRepository itemPedidoRepository;
    @Mock
    private PagamentoRepository pagamentoRepository;

    private ExportacaoCsvStrategy csv;
    private ExportacaoXlsxStrategy xlsx;
    private ExportacaoPdfStrategy pdf;
    private ItemPedido item;

    @BeforeEach
    void setup() throws Exception {
        csv = new ExportacaoCsvStrategy(itemPedidoRepository, pagamentoRepository);
        xlsx = new ExportacaoXlsxStrategy(itemPedidoRepository, pagamentoRepository);
        pdf = new ExportacaoPdfStrategy(itemPedidoRepository, pagamentoRepository);

        DadosEndereco dadosEndereco = new DadosEndereco("8123434", "brasil", "test", "test", "Rua test");
        User vendedor = new User(new DadosCadastroUser(
                "vendedor teste", "12345678912", "22-06-2005", "test",
                dadosEndereco, "test@gmail.com", "1231313139", "testUser", "test"));
        vendedor.setId(7L);

        Produto produto = ProdutoTestFactory.produto(ProdutoTestFactory.dados(
                "Sensor de teste", 10, "test", 7L, "10", "image/png", ESP32, USADO));
        produto.setId(1L);
        produto.setPreco(25.5);
        produto.setUsuario(vendedor);
        produto.setImagem(pngDeTeste());

        Pedido pedido = new Pedido(new PedidoCadastroDTO(new ArrayList<>(List.of(1L)), 3L, 25.5, dadosEndereco, MetodoPagamento.PIX));
        pedido.setId(99L);
        pedido.setDataPedido(LocalDateTime.of(2025, 1, 15, 10, 30));

        item = new ItemPedido(pedido, produto);

        Pagamento pagamento = new Pagamento(pedido, MetodoPagamento.PIX, 25.5);

        lenient().when(itemPedidoRepository.pegarComprasNoPeriodo(eq(3L), any(), any()))
                .thenReturn(List.of(item));
        lenient().when(pagamentoRepository.findByPedidoIdIn(any())).thenReturn(List.of(pagamento));
    }

    private byte[] pngDeTeste() throws Exception {
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB), "png", saida);
        return saida.toByteArray();
    }

    @Test
    void deveRecusarPeriodoComDataNula() {
        assertThrows(PeriodoInvalido.class, () -> csv.exportar(3L, null, FIM));
        assertThrows(PeriodoInvalido.class, () -> csv.exportar(3L, INICIO, null));
    }

    @Test
    void deveRecusarDataFimAnteriorADataInicio() {
        assertThrows(PeriodoInvalido.class, () -> csv.exportar(3L, FIM, INICIO));
    }

    @Test
    void deveConsultarComOIntervaloNormalizadoParaODiaInteiro() {
        csv.exportar(3L, INICIO, FIM);

        ArgumentCaptor<LocalDateTime> inicio = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> fim = ArgumentCaptor.forClass(LocalDateTime.class);
        org.mockito.Mockito.verify(itemPedidoRepository)
                .pegarComprasNoPeriodo(eq(3L), inicio.capture(), fim.capture());

        assertEquals(INICIO.atStartOfDay(), inicio.getValue());
        assertEquals(FIM.atTime(LocalTime.MAX), fim.getValue());
    }

    @Test
    void deveGerarCsvComCabecalhoLinhaETotal() {
        ArquivoExportado arquivo = csv.exportar(3L, INICIO, FIM);
        String conteudo = new String(arquivo.conteudo(), StandardCharsets.UTF_8);

        assertTrue(conteudo.contains("Pedido;Data;Meio de pagamento;Produto"));
        assertTrue(conteudo.contains("Sensor de teste"));
        assertTrue(conteudo.contains("vendedor teste"));
        assertTrue(conteudo.contains("15/01/2025 10:30"));
        assertTrue(conteudo.contains("Pix"));
        assertTrue(conteudo.contains("Total geral;25,50"));
        assertEquals("historico-compras_2025-01-01_a_2025-01-31.csv", arquivo.nomeArquivo());
    }

    @Test
    void deveEscaparCampoComPontoEVirgula() {
        item.getProduto().setNome("Kit; com separador");

        String conteudo = new String(csv.exportar(3L, INICIO, FIM).conteudo(), StandardCharsets.UTF_8);

        assertTrue(conteudo.contains("\"Kit; com separador\""));
    }

    @Test
    void deveGerarXlsxLegivelPeloPoi() throws Exception {
        ArquivoExportado arquivo = xlsx.exportar(3L, INICIO, FIM);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(arquivo.conteudo()))) {
            Sheet planilha = workbook.getSheetAt(0);
            assertEquals("Pedido", planilha.getRow(0).getCell(0).getStringCellValue());
            Row primeira = planilha.getRow(1);
            assertEquals(99d, primeira.getCell(0).getNumericCellValue());
            assertEquals("Pix", primeira.getCell(2).getStringCellValue());
            assertEquals("Sensor de teste", primeira.getCell(3).getStringCellValue());
            assertEquals(25.5, primeira.getCell(8).getNumericCellValue());
            assertEquals("Total geral", planilha.getRow(2).getCell(8).getStringCellValue());
        }
        assertEquals("historico-compras_2025-01-01_a_2025-01-31.xlsx", arquivo.nomeArquivo());
    }

    @Test
    void deveGerarPdfComAImagemDoProduto() {
        ArquivoExportado arquivo = pdf.exportar(3L, INICIO, FIM);

        assertNotNull(arquivo.conteudo());
        assertTrue(arquivo.conteudo().length > 0);
        assertEquals("%PDF", new String(arquivo.conteudo(), 0, 4, StandardCharsets.ISO_8859_1));
        assertEquals("application/pdf", arquivo.contentType());
        assertEquals("historico-compras_2025-01-01_a_2025-01-31.pdf", arquivo.nomeArquivo());
    }

    @Test
    void deveGerarPdfMesmoSemImagem() {
        item.getProduto().setImagem(null);

        assertEquals("%PDF", new String(pdf.exportar(3L, INICIO, FIM).conteudo(), 0, 4, StandardCharsets.ISO_8859_1));
    }

    @Test
    void deveGerarPdfMesmoComImagemCorrompida() {
        item.getProduto().setImagem(new byte[]{1, 2, 3, 4, 5});

        assertEquals("%PDF", new String(pdf.exportar(3L, INICIO, FIM).conteudo(), 0, 4, StandardCharsets.ISO_8859_1));
    }

    @Test
    void deveGerarArquivoVazioQuandoNaoHaComprasNoPeriodo() {
        when(itemPedidoRepository.pegarComprasNoPeriodo(eq(4L), any(), any())).thenReturn(List.of());

        String conteudo = new String(csv.exportar(4L, INICIO, FIM).conteudo(), StandardCharsets.UTF_8);

        assertTrue(conteudo.contains("Pedido;Data;Meio de pagamento;Produto"));
        assertTrue(conteudo.contains("Total geral;0,00"));
    }

    @Test
    void contextDeveEscolherAEstrategiaDoFormato() {
        ExportacaoContext context = new ExportacaoContext(List.of(csv, xlsx, pdf));

        assertSame(pdf, context.escolher(FormatoExportacao.PDF));
        assertSame(csv, context.escolher(FormatoExportacao.CSV));
        assertSame(xlsx, context.escolher(FormatoExportacao.XLSX));
    }

    @Test
    void contextDeveRecusarFormatoSemEstrategia() {
        ExportacaoContext context = new ExportacaoContext(List.of(csv));

        assertThrows(IllegalArgumentException.class, () -> context.escolher(FormatoExportacao.PDF));
    }
}