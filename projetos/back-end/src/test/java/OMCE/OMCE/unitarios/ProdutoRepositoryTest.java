package OMCE.OMCE.unitarios;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static OMCE.OMCE.Produto.enums.Categoria.ESP32;
import static OMCE.OMCE.Produto.enums.Condicao.USADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProdutoRepositoryTest {

    @Mock
    private ProdutoRepository repository;

    @Test
    @DisplayName("deve pegar os produtos pelo id")
    void PegarProdutoPeloId() {
        // Arrange
        DadosCadastroProduto dados = new DadosCadastroProduto(
                "test", 10, "test", 1L,
                "10", "10", ESP32, USADO
        );
        Produto produto = new Produto(dados);
        produto.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        // Act
        Optional<Produto> result = repository.findById(1L);
        // Assert
        assertTrue(result.isPresent());
        assertEquals("test", result.get().getNome());
    }

    @Test
    @DisplayName("deve obter os detalhes do produto pelo id")
    void DeveObterDetalhesProdutoPeloId() {
        String imagemBase64 = "dGVzdA==";
        String imagemTipo = "image/png";
        DadosCadastroProduto dados = new DadosCadastroProduto(
                "test", 10.0, "test", 1L,
                imagemBase64, imagemTipo, ESP32, USADO
        );
        Produto produto = new Produto(dados);
        produto.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        Optional<Produto> result = repository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("test", result.get().getNome());
        assertEquals("test", result.get().getDetalhes());
    }

    @Test
    @DisplayName("deve pegar os produtos pelo id do usuario criador")
    void PegarProdutosPorUsuario() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        DadosCadastroProduto dados = new DadosCadastroProduto(
                "test", 10, "test", 1L,
                "10", "10", ESP32, USADO
        );
        Produto produto = new Produto(dados);
        produto.setId(1L);
        Page<Produto> produtoPage = new PageImpl<>(List.of(produto));
        when(repository.pegarProdutosUsuario(1L, pageable)).thenReturn(produtoPage);
        // Act
        Page<Produto> result = repository.pegarProdutosUsuario(1L, pageable);
        // Assert
        assertTrue(result.isFirst());
        assertEquals("test", result.getContent().get(0).getNome());
        assertEquals(1L, result.getContent().get(0).getId());
    }
}
