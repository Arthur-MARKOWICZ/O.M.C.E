package OMCE.OMCE.AvaliacaoProduto.service;

import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoDTO;
import OMCE.OMCE.AvaliacaoProduto.repository.AvaliacaoProdutoRepositorio;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AvaliacaoProdutoServico {

    @Autowired
    private AvaliacaoProdutoRepositorio repositorio;

    @Autowired
    private ProdutoRepository produtoRepositorio;

    public void criar(AvaliacaoProdutoDTO dto) {
        Produto produto = produtoRepositorio.findById(dto.getIdProduto())
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        AvaliacaoProduto avaliacao = new AvaliacaoProduto(dto,produto);

        repositorio.save(avaliacao);
    }


    public Double mediaNotas(Long idProduto) {
        Double media = repositorio.mediaPorProduto(idProduto);
        return media != null ? media : 0.0;
    }

    
    public Page<AvaliacaoProduto> listarPorProduto(Long idProduto, Pageable pageable) {
        return repositorio.findByProdutoId(idProduto, pageable);
    }
}


