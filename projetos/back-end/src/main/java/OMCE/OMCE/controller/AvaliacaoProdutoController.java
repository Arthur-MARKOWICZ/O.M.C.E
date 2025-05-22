package OMCE.OMCE.controller;

import OMCE.OMCE.AvaliacaoProduto.ProdutoAvaliacaoDTO;
import OMCE.OMCE.AvaliacaoProduto.ProductReview;
import OMCE.OMCE.AvaliacaoProduto.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class AvaliacaoProdutoController {

    @Autowired
    private ProductReviewService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ProdutoAvaliacaoDTO dto) {
        service.create(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReview>> listByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(service.listByProduct(productId));
    }

    @GetMapping("/product/{productId}/average")
    public ResponseEntity<Double> averageScore(@PathVariable Long productId) {
        return ResponseEntity.ok(service.averageScore(productId));
    }
}

