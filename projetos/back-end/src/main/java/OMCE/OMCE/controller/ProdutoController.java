package OMCE.OMCE.controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import OMCE.OMCE.Produto.*;
import OMCE.OMCE.Produto.dto.DadosAlterarDadosProduto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.dto.ProdutoRespostaDTO;
import OMCE.OMCE.Produto.enums.Categoria;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.Produto.service.ProdutoService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.Validacao.ValidacaoProduto;
import OMCE.OMCE.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    ValidacaoProduto validar;
    private final ProdutoService service;
    public ProdutoController(ProdutoService service) {
        this.service = service;
    }
    @PostMapping("/cadastroProduto")
    public ResponseEntity<Void> cadastroProduto(@RequestBody DadosCadastroProduto dados, @AuthenticationPrincipal User usuarioLogado) {
        service.cadastro(dados, usuarioLogado.getId());
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/visualizarDetalhesProduto/{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> mostraDetalhesProdutos(@PathVariable Long id){
        Map<String, Object> detalhes = service.pegarDetalhesDoProduto(id);
       return ResponseEntity.ok(detalhes);
    }
    @GetMapping("/filtro")
    public ResponseEntity<Page<ProdutoRespostaDTO>> filtrarProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<ProdutoRespostaDTO> produtosDTO = service.filtrarProduto(nome,categoria,precoMin,precoMax,pageable);
        return ResponseEntity.ok(produtosDTO);
    }
    @DeleteMapping("/deletar/{id}")
    @Transactional
    public ResponseEntity<String> deletarProduto(@PathVariable Long id, @AuthenticationPrincipal User usuarioLogado) {
        service.deletar(id, usuarioLogado.getId());
        return ResponseEntity.ok("Produto deletado com sucesso.");
    }

    @PutMapping ("/alterarDadosProduto")
    @Transactional
    public ResponseEntity<Void> alterardados(@RequestBody DadosAlterarDadosProduto dados, @AuthenticationPrincipal User usuarioLogado){
        service.alterarDadosProduto(dados, usuarioLogado.getId());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/todosProdutosUsuario")
    public ResponseEntity<Page<ProdutoRespostaDTO>> pegarProdutosUsuario(@PageableDefault(size=10)Pageable pageable,
                                                                         @AuthenticationPrincipal User usuarioLogado,
                                                                         @RequestParam(required = false) String nome,
                                                                         @RequestParam(required = false) String categoria,
                                                                         @RequestParam(required = false) Double precoMin,
                                                                         @RequestParam(required = false) Double precoMax) {
        Page<ProdutoRespostaDTO> produtoDTO = service.pegarProdutosPorUser(usuarioLogado.getId(), nome, categoria, precoMin, precoMax, pageable);
       return ResponseEntity.ok(produtoDTO);

    }
}