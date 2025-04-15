package OMCE.OMCE.controller;
import OMCE.OMCE.Produto.DadosCadastroProduto;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import OMCE.OMCE.User.DadosAlterarDadosUser;
import OMCE.OMCE.User.User;
import OMCE.OMCE.Validacao.ValidacaoProduto;
import OMCE.OMCE.config.TokenService;
import OMCE.OMCE.User.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import OMCE.OMCE.Produto.DadosAlterarDadosProduto;

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
    @PostMapping("/cadastroProduto")
    public ResponseEntity cadastroProduto(@RequestBody DadosCadastroProduto dados) {
        validar.ValidarCadastroProduto(dados);
        Produto newproduto = new Produto(dados);
        this.produtoRepository.save(newproduto);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/visualizarDetalhesProduto/{id}", produces = "application/json")
    public ResponseEntity mostraDetalhesProdutos(@PathVariable Long id){

        Optional<Produto> produto = produtoRepository.findById(id);
        Optional<User> user = userRepository.findById(produto.get().getId_usuario());

        if(produto.isPresent()) {
            User usuario = user.get();
            byte[] imagemBytes = produto.get().getImagem();
            String imagem = Base64.getEncoder().encodeToString(imagemBytes);
            Map<String, Object> json = new HashMap<>();
            json.put("id", produto.get().getId());
            json.put("nome", produto.get().getNome());
            json.put("preco", produto.get().getPreco());
            json.put("Imagem", imagem);
            json.put("Imagem_tipo", produto.get().getImageTipo());
            json.put("detalhes", produto.get().getDetalhes());
            json.put("nome do usuario", usuario.getNome());

            return ResponseEntity.ok(json);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }@GetMapping("/todos")
    public ResponseEntity<List<Map<String, Object>>> listarTodosProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        List<Map<String, Object>> listaProdutos = new ArrayList<>();

        for (Produto p : produtos) {
            Optional<User> usuario = userRepository.findById(p.getId_usuario());

            if (usuario.isPresent()) {
                Map<String, Object> json = new HashMap<>();
                json.put("id", p.getId());
                json.put("nome", p.getNome());
                json.put("preco", p.getPreco());
                json.put("detalhes", p.getDetalhes());
                if (p.getImagem() != null) {
                    json.put("imagem", Base64.getEncoder().encodeToString(p.getImagem()));
                    json.put("imagem_tipo", p.getImageTipo());
                } else {
                    json.put("imagem", null);
                    json.put("imagem_tipo", null);
                }
                json.put("imagem_tipo", p.getImageTipo());
                json.put("nome_usuario", usuario.get().getNome());
                listaProdutos.add(json);
            }
        }

        return ResponseEntity.ok(listaProdutos);
    }
    @DeleteMapping("/deletar/{id}")
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
    public ResponseEntity alterardados(DadosAlterarDadosProduto dados){
        validar.ValidarAlterarProduto(dados);
        var produto =  produtoRepository.getReferenceById(dados.id());
        produto.alterarDados(dados);
        return ResponseEntity.ok().build();
    }

}