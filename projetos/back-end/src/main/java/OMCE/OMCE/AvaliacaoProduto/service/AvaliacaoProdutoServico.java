package OMCE.OMCE.AvaliacaoProduto.service;

import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoDTO;
import OMCE.OMCE.AvaliacaoProduto.repository.AvaliacaoProdutoRepositorio;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.Avaliacao.service.AvaliacaoTemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoProdutoServico
        extends AvaliacaoTemplateService<AvaliacaoProdutoDTO, AvaliacaoProduto> {

    @Autowired
    private AvaliacaoProdutoRepositorio repositorio;

    @Autowired
    private ProdutoRepository produtoRepositorio;

    @Override
    protected AvaliacaoProduto criarAvaliacao(AvaliacaoProdutoDTO dto) {

        Produto produto = produtoRepositorio.findById(dto.getIdProduto())
                .orElseThrow(() ->
                        new RuntimeException("Produto não encontrado"));

        return new AvaliacaoProduto(dto, produto);
    }

    @Override
    protected void salvarAvaliacao(AvaliacaoProduto avaliacao) {
        repositorio.save(avaliacao);
    }

    @Override
    protected List<Integer> buscarNotas(Long idProduto) {
        return repositorio.buscarTodasNotas(idProduto);
    }

    public Page<AvaliacaoProduto> listarPorProduto(
            Long idProduto,
            Pageable pageable) {

        return repositorio.findByProdutoId(idProduto, pageable);
    }
}