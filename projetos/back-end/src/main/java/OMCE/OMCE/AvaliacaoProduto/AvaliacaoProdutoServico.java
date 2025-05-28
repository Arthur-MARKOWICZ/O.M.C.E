package OMCE.OMCE.AvaliacaoProduto;

import OMCE.OMCE.Produto.Produto;

import OMCE.OMCE.Produto.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvaliacaoProdutoServico {

    @Autowired
    private AvaliacaoProdutoRepositorio repositorio;

    @Autowired
    private ProdutoRepository produtoRepositorio;

    public void criar(AvaliacaoProdutoDTO dto) {
        Produto produto = produtoRepositorio.findById(dto.id_Produto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        AvaliacaoProduto avaliacao = new AvaliacaoProduto();
        avaliacao.setNota(dto.nota());
        avaliacao.setComentario(dto.comentario());
        avaliacao.setProduto(produto);
        avaliacao.setDataCriacao(LocalDateTime.now());

        repositorio.save(avaliacao);
    }

    public List<AvaliacaoProduto> listarPorProduto(Long idProduto) {
        return repositorio.findByProdutoId(idProduto);
    }

    public double mediaNotas(Long idProduto) {
        List<AvaliacaoProduto> avaliacoes = repositorio.findByProdutoId(idProduto);
        return avaliacoes.stream()
                .mapToInt(AvaliacaoProduto::getNota)
                .average()
                .orElse(0.0);
    }
}


