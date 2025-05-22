package OMCE.OMCE.controller;

import OMCE.OMCE.AvaliacaoProduto.ProductReviewService;
import OMCE.OMCE.AvaliacaoProduto.ProdutoAvaliacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
