package OMCE.OMCE.controller;

import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoDTO;
import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoRespostaDTO;
import OMCE.OMCE.AvaliacaoProduto.service.AvaliacaoProdutoServico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoProdutoController {

    @Autowired
    private AvaliacaoProdutoServico servico;

    @PostMapping("/criar")
    public ResponseEntity<Void> criar(
            @RequestBody AvaliacaoProdutoDTO dto) {

        servico.criar(dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/produto/{idProduto}")
    public ResponseEntity<Page<AvaliacaoProdutoRespostaDTO>>
    listarPorProduto(
            @PathVariable Long idProduto,
            Pageable pageable) {

        return ResponseEntity.ok(
                servico.listarPorProduto(
                        idProduto,
                        pageable)
        );
    }

    @GetMapping("/produto/{idProduto}/media")
    public ResponseEntity<Double> mediaNotas(
            @PathVariable Long idProduto) {

        return ResponseEntity.ok(
                servico.calcularMedia(idProduto)
        );
    }

    @GetMapping("/produto/{idProduto}/pagina")
    public ResponseEntity<Page<AvaliacaoProdutoRespostaDTO>>
    listarPorProdutoPaginado(
            @PathVariable Long idProduto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                servico.listarPorProduto(
                        idProduto,
                        pageable)
        );
    }
}