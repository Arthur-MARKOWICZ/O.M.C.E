package OMCE.OMCE.controller;

import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProdutoDTO;
import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;
import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProdutoServico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    public ResponseEntity<Page<AvaliacaoProduto>> listarPorProduto(@PathVariable Long idProduto,Pageable pageable) {
        var avaliacoes = servico.listarPorProduto(idProduto,pageable);
        System.out.println(avaliacoes);
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/produto/{idProduto}/media")
    public ResponseEntity<Double> mediaNotas(@PathVariable Long idProduto) {
        return ResponseEntity.ok(servico.mediaNotas(idProduto));
    }

    
    @GetMapping("/produto/{idProduto}/pagina")
    public ResponseEntity<Page<AvaliacaoProdutoDTO>> listarPorProdutoPaginado(
        @PathVariable Long idProduto,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AvaliacaoProduto> avaliacoesPage = servico.listarPorProduto(idProduto, pageable);

        Page<AvaliacaoProdutoDTO> dtoPage = avaliacoesPage.map(av -> new AvaliacaoProdutoDTO(
            av.getNota(),
            av.getComentario(),
            av.getProduto().getId()
        ));

        return ResponseEntity.ok(dtoPage);
    }
}



