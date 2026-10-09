package OMCE.OMCE.unitarios;

import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoAtualizacaoDTO;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoDTO;
import OMCE.OMCE.AvaliacaoProduto.repository.AvaliacaoProdutoRepositorio;
import OMCE.OMCE.AvaliacaoProduto.service.AvaliacaoProdutoServico;
import OMCE.OMCE.Execao.AcessoNegado;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.enums.Categoria;
import OMCE.OMCE.Produto.enums.Condicao;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.User.Role;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.repository.UserRepository;
import OMCE.OMCE.utils.ProdutoTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private UserRepository userRepository;

    private User avaliador;

    @BeforeEach
    void setupSecurity() {
        avaliador = new User();
        avaliador.setId(7L);
        avaliador.setEmail("comprador@test.com");
        avaliador.setRole(Role.COMPRADOR);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        avaliador, null, avaliador.getAuthorities()));
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void DeveCadastrarAvaliacaoProdutoComDadosCorretos() {
        DadosCadastroProduto dadosProduto = ProdutoTestFactory.dados(
                "Teclado Mecânico",
                80.90,
                "Teclado RGB com switches azuis",
                1L,
                "imagem/png",
                "base64",
                Categoria.OUTRO,
                Condicao.NOVO
        );

        Produto produto = ProdutoTestFactory.produto(dadosProduto);
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

        ArgumentCaptor<AvaliacaoProduto> captor =
                ArgumentCaptor.forClass(AvaliacaoProduto.class);
        verify(avaliacaoRepository, times(1)).save(captor.capture());
        assertEquals(avaliador.getId(), captor.getValue().getAvaliador().getId());
    }

    @Test
    public void NaoDeveCadastrarAvaliacaoSemIdProduto() {
        AvaliacaoProdutoDTO dto = new AvaliacaoProdutoDTO(
                4,
                "Produto bom, mas veio sem caixa.",
                null
        );

        when(produtoRepository.findById(null)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            avaliacaoService.criar(dto);
        });

        verify(avaliacaoRepository, never()).save(any(AvaliacaoProduto.class));
    }

    @Test
    public void DeveAtualizarAvaliacaoDoAutor() {
        AvaliacaoProduto avaliacao = avaliacaoDoAutor(5L, 4, "Ok");
        when(avaliacaoRepository.findById(5L)).thenReturn(Optional.of(avaliacao));
        when(avaliacaoRepository.save(any(AvaliacaoProduto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        avaliacaoService.atualizar(5L, new AvaliacaoProdutoAtualizacaoDTO(5, "Excelente"));

        assertEquals(5, avaliacao.getNota());
        assertEquals("Excelente", avaliacao.getComentario());
        verify(avaliacaoRepository).save(avaliacao);
    }

    @Test
    public void NaoDeveAtualizarAvaliacaoDeOutroUsuario() {
        AvaliacaoProduto avaliacao = avaliacaoDoAutor(5L, 4, "Ok");
        User outro = new User();
        outro.setId(99L);
        outro.setRole(Role.COMPRADOR);
        avaliacao.setAvaliador(outro);
        when(avaliacaoRepository.findById(5L)).thenReturn(Optional.of(avaliacao));

        assertThrows(AcessoNegado.class, () ->
                avaliacaoService.atualizar(5L, new AvaliacaoProdutoAtualizacaoDTO(1, "Ruim")));

        verify(avaliacaoRepository, never()).save(any(AvaliacaoProduto.class));
    }

    @Test
    public void DeveDeletarAvaliacaoDoAutor() {
        AvaliacaoProduto avaliacao = avaliacaoDoAutor(5L, 4, "Ok");
        when(avaliacaoRepository.findById(5L)).thenReturn(Optional.of(avaliacao));

        avaliacaoService.deletar(5L);

        verify(avaliacaoRepository).delete(avaliacao);
    }

    @Test
    public void NaoDeveDeletarAvaliacaoDeOutroUsuario() {
        AvaliacaoProduto avaliacao = avaliacaoDoAutor(5L, 4, "Ok");
        User outro = new User();
        outro.setId(99L);
        outro.setRole(Role.COMPRADOR);
        avaliacao.setAvaliador(outro);
        when(avaliacaoRepository.findById(5L)).thenReturn(Optional.of(avaliacao));

        assertThrows(AcessoNegado.class, () -> avaliacaoService.deletar(5L));

        verify(avaliacaoRepository, never()).delete(any(AvaliacaoProduto.class));
    }

    private AvaliacaoProduto avaliacaoDoAutor(Long id, int nota, String comentario) {
        AvaliacaoProduto avaliacao = new AvaliacaoProduto();
        avaliacao.setId(id);
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);
        avaliacao.setAvaliador(avaliador);
        return avaliacao;
    }
}
