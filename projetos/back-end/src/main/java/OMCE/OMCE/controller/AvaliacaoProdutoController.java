package OMCE.OMCE.controller;

import OMCE.OMCE.AvaliacaoProduto.ProductReview;
import OMCE.OMCE.AvaliacaoProduto.ProductReviewService;
import OMCE.OMCE.AvaliacaoProduto.ProdutoAvaliacaoDTO;
import org.hibernate.result.UpdateCountOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacao")
public class AvaliacaoProdutoController {
    @Autowired
    private ProductReviewService service;
    @PostMapping("/cadastro")
    public ResponseEntity cadastroAvaliacao(@RequestBody ProdutoAvaliacaoDTO dto){
        service.create(dto);
        return  ResponseEntity.ok().build();
    }
    @GetMapping("/pegarAvaliacoes/{id}")
    public ResponseEntity<List<ProductReview>> pegarAvaliacoes(@PathVariable Long id){
        List<ProductReview> avaliacoes = service.listByProduct(id);

        return ResponseEntity.ok(avaliacoes);
    }
}
