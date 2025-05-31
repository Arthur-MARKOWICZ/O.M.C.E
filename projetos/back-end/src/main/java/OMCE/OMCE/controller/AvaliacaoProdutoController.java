package OMCE.OMCE.controller;


import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProdutoDTO;
import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;
import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProdutoServico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoProdutoController {

    @Autowired
    private AvaliacaoProdutoServico servico;


    @PostMapping("/criar")

    public ResponseEntity<Void> criar(@RequestBody AvaliacaoProdutoDTO dto) {
        servico.criar(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/produto/{idProduto}")
    public ResponseEntity<List<AvaliacaoProduto>> listarPorProduto(@PathVariable Long idProduto) {
        return ResponseEntity.ok(servico.listarPorProduto(idProduto));
    }

    @GetMapping("/produto/{idProduto}/media")
    public ResponseEntity<Double> mediaNotas(@PathVariable Long idProduto) {
        return ResponseEntity.ok(servico.mediaNotas(idProduto));
    }
}


