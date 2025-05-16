package OMCE.OMCE.AvaliacaoProduto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewService {

    @Autowired
    private ProductReviewRepository repository;

    public ProductReview create(ProductReview review) {
        return repository.save(review);
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

