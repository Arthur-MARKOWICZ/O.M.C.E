package OMCE.OMCE.controller;

import OMCE.OMCE.Historico.HistoricoService;
import OMCE.OMCE.Produto.ProdutoRespostaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/historico")
public class HistoricoController {
    @Autowired
    private HistoricoService historicoService;
    @GetMapping("/Vendas")
    public ResponseEntity<Page<ProdutoRespostaDTO>> historicoVendas(@PageableDefault(size=10) Pageable pageable, @RequestHeader("Id-Usuario") Long id_usuario){
        Page<ProdutoRespostaDTO> produtos = historicoService.pegarHistoricoDeVenda(id_usuario,pageable);
        return ResponseEntity.ok(produtos);
    }
    @GetMapping("/compra")
    public ResponseEntity<Page<ProdutoRespostaDTO>> historicoCompra(@PageableDefault(size=10) Pageable pageable, @RequestHeader("Id-Usuario") Long id_usuario){
        Page<ProdutoRespostaDTO> produtos = historicoService.pegarHistoricoDeCompra(id_usuario,pageable);
        return ResponseEntity.ok(produtos);
    }
}
