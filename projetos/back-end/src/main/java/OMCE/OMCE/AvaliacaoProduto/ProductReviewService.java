package OMCE.OMCE.AvaliacaoProduto;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewService {

    @Autowired
    private ProductReviewRepository repository;
    @Autowired
    private ProdutoRepository produtoRepository;

    public ProductReview create(ProdutoAvaliacaoDTO dto) {
        ProductReview productReview = new ProductReview(dto);
        Produto produto = produtoRepository.getReferenceById(dto.id_produto());
        productReview.setProduct(produto);
        return repository.save(productReview);
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

