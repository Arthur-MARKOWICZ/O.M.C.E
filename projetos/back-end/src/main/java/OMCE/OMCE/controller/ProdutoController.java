package OMCE.OMCE.controller;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    }

}

