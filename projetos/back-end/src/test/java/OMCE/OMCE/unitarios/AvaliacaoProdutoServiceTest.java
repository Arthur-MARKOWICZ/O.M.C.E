package OMCE.OMCE.unitarios;

import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoDTO;
import OMCE.OMCE.AvaliacaoProduto.repository.AvaliacaoProdutoRepositorio;
import OMCE.OMCE.AvaliacaoProduto.service.AvaliacaoProdutoServico;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.enums.Categoria;
import OMCE.OMCE.Produto.enums.Condicao;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvaliacaoProdutoServiceTest {

    @InjectMocks
    private AvaliacaoProdutoServico avaliacaoService;

    @Mock
    private AvaliacaoProdutoRepositorio avaliacaoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    public void DeveCadastrarAvaliacaoProdutoComDadosCorretos() {
        DadosCadastroProduto dadosProduto = new DadosCadastroProduto(
                "Teclado Mecânico",
                80.90,
                "Teclado RGB com switches azuis",
                1L,
                "imagem/png",
                "base64",
                Categoria.OUTRO,
                Condicao.NOVO
        );

        Produto produto = new Produto(dadosProduto);
        produto.setId(10L);

        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));

        AvaliacaoProdutoDTO dto = new AvaliacaoProdutoDTO(
                5,
                "Excelente qualidade!",
                produto.getId()
        );

        when(avaliacaoRepository.save(any(AvaliacaoProduto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        avaliacaoService.criar(dto);

        verify(avaliacaoRepository, times(1)).save(any(AvaliacaoProduto.class));
    }

    @Test
    public void NaoDeveCadastrarAvaliacaoSemIdProduto() {
        AvaliacaoProdutoDTO dto = new AvaliacaoProdutoDTO(
                4,
                "Produto bom, mas veio sem caixa.",
                null
        );

        when(produtoRepository.findById(null)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            avaliacaoService.criar(dto);
        });

        verify(avaliacaoRepository, never()).save(any(AvaliacaoProduto.class));
    }

}
