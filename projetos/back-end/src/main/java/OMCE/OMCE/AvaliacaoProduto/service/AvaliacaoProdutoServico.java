package OMCE.OMCE.AvaliacaoProduto.service;

import OMCE.OMCE.Avaliacao.service.AvaliacaoTemplateService;
import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoAtualizacaoDTO;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoDTO;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoRespostaDTO;
import OMCE.OMCE.AvaliacaoProduto.repository.AvaliacaoProdutoRepositorio;
import OMCE.OMCE.Execao.AcessoNegado;
import OMCE.OMCE.Execao.UserNaoEncontrado;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoProdutoServico
        extends AvaliacaoTemplateService<
                AvaliacaoProdutoDTO,
                AvaliacaoProduto> {

    @Autowired
    private AvaliacaoProdutoRepositorio repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected AvaliacaoProduto criarAvaliacao(
            AvaliacaoProdutoDTO dto) {

        AvaliacaoProduto avaliacao =
                new AvaliacaoProduto(dto);

        Produto produto = produtoRepository
                .findById(dto.getIdProduto())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Produto não encontrado com id: "
                                        + dto.getIdProduto()));

        avaliacao.setProduto(produto);
        avaliacao.setAvaliador(usuarioLogado());

        return avaliacao;
    }

    @Override
    protected void salvarAvaliacao(
            AvaliacaoProduto avaliacao) {

        repository.save(avaliacao);
    }

    @Override
    protected List<Integer> buscarNotas(
            Long idProduto) {

        return repository.buscarTodasNotas(idProduto);
    }

    public Page<AvaliacaoProdutoRespostaDTO> listarPorProduto(
            Long idProduto,
            Pageable pageable) {

        Page<AvaliacaoProduto> avaliacoes =
                repository.findByProdutoId(
                        idProduto,
                        pageable);

        return avaliacoes.map(
                AvaliacaoProdutoRespostaDTO::new
        );
    }

    public Page<AvaliacaoProdutoRespostaDTO> listarMinhas(
            Pageable pageable) {

        User avaliador = usuarioLogado();
        return repository
                .findByAvaliadorId(avaliador.getId(), pageable)
                .map(AvaliacaoProdutoRespostaDTO::new);
    }

    public void atualizar(
            Long id,
            AvaliacaoProdutoAtualizacaoDTO dto) {

        AvaliacaoProduto avaliacao = buscarPorId(id);
        garantirAutor(avaliacao);
        avaliacao.setNota(dto.nota());
        avaliacao.setComentario(dto.comentario());
        repository.save(avaliacao);
    }

    public void deletar(Long id) {
        AvaliacaoProduto avaliacao = buscarPorId(id);
        garantirAutor(avaliacao);
        repository.delete(avaliacao);
    }

    private AvaliacaoProduto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Avaliação de produto não encontrada com id: "
                                        + id));
    }

    private void garantirAutor(AvaliacaoProduto avaliacao) {
        User logado = usuarioLogado();
        if (avaliacao.getAvaliador() == null
                || avaliacao.getAvaliador().getId() != logado.getId()) {
            throw new AcessoNegado(
                    "Você só pode alterar ou excluir avaliações que escreveu.");
        }
    }

    private User usuarioLogado() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UserNaoEncontrado("Usuário não autenticado");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        User user = userRepository.findByEmail(authentication.getName());
        if (user == null) {
            throw new UserNaoEncontrado(
                    "Usuário não encontrado: " + authentication.getName());
        }
        return user;
    }
}
