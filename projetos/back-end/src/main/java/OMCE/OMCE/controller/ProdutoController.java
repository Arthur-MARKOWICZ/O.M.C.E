package OMCE.OMCE.controller;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import OMCE.OMCE.User.DadosAlterarDadosUser;
import OMCE.OMCE.User.User;
import OMCE.OMCE.config.TokenService;
import OMCE.OMCE.User.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import OMCE.OMCE.Produto.DadosAlterarDadosProduto;

import java.util.*;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
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
    @Autowired
    private TokenService TokenService;
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarProduto(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String emailUsuario = TokenService.validateToken(token);
    
        Optional<Produto> produto = produtoRepository.findById(id);
        User usuario = userRepository.findByEmail(emailUsuario); 
    
        if (produto.isEmpty() || usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto ou usuário não encontrado.");
        }
    
        if (produto.get().getId_usuario() != usuario.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para deletar este produto.");
        }
    
        produtoRepository.deleteById(id);
        return ResponseEntity.ok("Produto deletado com sucesso.");
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarProduto(
        @PathVariable Long id,
        @RequestHeader("Authorization") String authHeader,
        @RequestBody Produto dadosEditados) {
        String token = authHeader.replace("Bearer ", "");
        String emailUsuario = TokenService.validateToken(token);
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        User usuario = userRepository.findByEmail(emailUsuario); // corrigido
        if (produtoOptional.isEmpty() || usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto ou usuário não encontrado.");
        }
        Produto produto = produtoOptional.get();

        if (produto.getId_usuario() != usuario.getId()) { // corrigido
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para editar este produto.");
        }
        produto.setNome(dadosEditados.getNome());
        produto.setPreco(dadosEditados.getPreco());
        produto.setDetalhes(dadosEditados.getDetalhes());
        produtoRepository.save(produto);
            return ResponseEntity.ok("Produto atualizado com sucesso.");
    }
    @PutMapping ("/alterarDadosProduto")
    @Transactional
    public ResponseEntity alterardados(DadosAlterarDadosProduto dados){
        var produto =  produtoRepository.getReferenceById(dados.id());
        produto.alterarDados(dados);
        return ResponseEntity.ok().build();
    }

}