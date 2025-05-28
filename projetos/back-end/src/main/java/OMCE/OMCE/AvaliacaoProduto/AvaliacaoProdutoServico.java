package OMCE.OMCE.AvaliacaoProduto;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoProdutoServico {

    @Autowired
    private AvaliacaoProdutoRepositorio repositorio;

    @Autowired
    private ProdutoRepository produtoRepositorio;

    public void criar(AvaliacaoProdutoDTO dto) {
        Produto produto = produtoRepositorio.findById(dto.getIdProduto())
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        AvaliacaoProduto avaliacao = new AvaliacaoProduto();
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setProduto(produto);

        repositorio.save(avaliacao);
    }

    public List<AvaliacaoProduto> listarPorProduto(Long idProduto) {
        return repositorio.findByProdutoId(idProduto);
    }

    public Double mediaNotas(Long idProduto) {
        Double media = repositorio.mediaPorProduto(idProduto);
        return media != null ? media : 0.0;
    }
} 
