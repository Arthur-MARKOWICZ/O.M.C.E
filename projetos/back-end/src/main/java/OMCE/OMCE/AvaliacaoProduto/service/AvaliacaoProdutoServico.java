package OMCE.OMCE.AvaliacaoProduto.service;

import OMCE.OMCE.Avaliacao.service.AvaliacaoTemplateService;
import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoDTO;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoRespostaDTO;
import OMCE.OMCE.AvaliacaoProduto.repository.AvaliacaoProdutoRepositorio;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}