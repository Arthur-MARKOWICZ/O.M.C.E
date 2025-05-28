package OMCE.OMCE.AvaliacaoProduto;

import OMCE.OMCE.Produto.Produto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Produto product;

    private LocalDateTime createdAt = LocalDateTime.now();
}
