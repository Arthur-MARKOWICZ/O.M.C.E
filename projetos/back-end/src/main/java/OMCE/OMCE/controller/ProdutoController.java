package OMCE.OMCE.controller;
import OMCE.OMCE.Produto.*;
import OMCE.OMCE.User.User;
import OMCE.OMCE.Validacao.ValidacaoProduto;
import OMCE.OMCE.User.UserRepository;
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
    public ProdutoController(ProdutoRepository produtoRepository,
                             UserRepository userRepository) {
        this.produtoRepository = produtoRepository;
        this.userRepository = userRepository;
    }
    @PostMapping("/cadastroProduto")
    public ResponseEntity cadastroProduto(@RequestBody DadosCadastroProduto dados) {
        validar.ValidarCadastroProduto(dados);
        User user = userRepository.getReferenceById(dados.id_usuario());
        Produto newproduto = new Produto(dados);
        newproduto.setUsuario(user);

        this.produtoRepository.save(newproduto);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/visualizarDetalhesProduto/{id}", produces = "application/json")
    public ResponseEntity mostraDetalhesProdutos(@PathVariable Long id){

        Optional<Produto> produto = produtoRepository.findById(id);
        Optional<User> user = userRepository.findById(produto.get().getUsuario().getId());

        if(produto.isPresent()) {
            User usuario = user.get();
            byte[] imagemBytes = produto.get().getImagem();
            String imagem = Base64.getEncoder().encodeToString(imagemBytes);
            Map<String, Object> json = new HashMap<>();
            json.put("id", produto.get().getId());
            json.put("dataaquisicao", produto.get().getDataaquisicao());
            json.put("nome", produto.get().getNome());
            json.put("preco", produto.get().getPreco());
            json.put("Imagem", imagem);
            json.put("Imagem_tipo", produto.get().getImageTipo());
            json.put("condicao", produto.get().getCondicao());
            json.put("detalhes", produto.get().getDetalhes());

                json.put("nome_do_usuario", usuario.getNome());
                json.put("id_vendedor", usuario.getId());


            return ResponseEntity.ok(json);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }@GetMapping("/filtro")
    public ResponseEntity<Page<ProdutoRespostaDTO>> filtrarProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Categoria catEnum = null;
        if (categoria != null && !categoria.isBlank()) {
            try {
                catEnum = Categoria.valueOf(categoria.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Page.empty());
            }
        }
        Page<Produto> produtos = produtoRepository.filtrarProdutos(nome, catEnum, precoMin, precoMax, pageable);
        Page<ProdutoRespostaDTO> produtosDTO = produtos.map(ProdutoRespostaDTO::new);
        return ResponseEntity.ok(produtosDTO);
    }
    @DeleteMapping("/deletar/{id}")
    @Transactional
    public ResponseEntity<?> deletarProduto(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto ou usuário não encontrado.");
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.ok("Produto deletado com sucesso.");
    }

    @PutMapping ("/alterarDadosProduto")
    @Transactional
    public ResponseEntity alterardados(@RequestBody DadosAlterarDadosProduto dados){
        validar.ValidarAlterarProduto(dados);
        var produto =  produtoRepository.getReferenceById(dados.id());
        produto.alterarDados(dados);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/todosProdutosUsuario")
    public ResponseEntity<Page<ProdutoRespostaDTO>> pegarProdutosUsuario(@PageableDefault(size=10)Pageable pageable,
                                                                         @RequestHeader("Id-Usuario") Long id_usuario) {
        Page<Produto> produtos = produtoRepository.pegarProdutosUsuario(id_usuario,pageable);

       var produtoDTO = produtos.map(ProdutoRespostaDTO::new);
       return ResponseEntity.ok(produtoDTO);

    }
}