package OMCE.OMCE.AvaliacaoProduto;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductReviewService {

    @Autowired
    private ProductReviewRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public void create(ProdutoAvaliacaoDTO dto) {
        Produto produto = produtoRepository.findById(dto.id_produto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ProductReview review = new ProductReview();
        review.setScore(dto.score());
        review.setComment(dto.comment());
        review.setProduct(produto);
        review.setCreatedAt(LocalDateTime.now());

        repository.save(review);
    }

    public List<ProductReview> listByProduct(Long productId) {
        return repository.findByProductId(productId);
    }

    public double averageScore(Long productId) {
        List<ProductReview> reviews = repository.findByProductId(productId);
        return reviews.stream()
                .mapToInt(ProductReview::getScore)
                .average()
                .orElse(0.0);
    }
}

