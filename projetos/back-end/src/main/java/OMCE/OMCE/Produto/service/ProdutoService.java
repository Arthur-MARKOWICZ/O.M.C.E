package OMCE.OMCE.Produto.service;

import OMCE.OMCE.Execao.CategoriaInvalida;
import OMCE.OMCE.Execao.ProdutoNaoEncontrado;
import OMCE.OMCE.Execao.UserNaoEncontrado;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosAlterarDadosProduto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.dto.ProdutoRespostaDTO;
import OMCE.OMCE.Produto.enums.Categoria;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.Validacao.ValidacaoProduto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProdutoService {
    private final ProdutoRepository repository;
    private final UserService userService;
    private final ValidacaoProduto validar;
    public ProdutoService(ProdutoRepository repository, UserService userService, ValidacaoProduto validar) {
        this.repository = repository;
        this.userService = userService;
        this.validar = validar;
    }
    public Produto cadastro(DadosCadastroProduto dados){
        validar.ValidarCadastroProduto(dados);
        User user = userService.pegarUserPorId(dados.id_usuario());
        if (user == null){
            throw new UserNaoEncontrado("Usuario nao encontrado");
        }
        Produto newproduto = new Produto(dados);
        newproduto.setUsuario(user);
        this.repository.save(newproduto);
        return newproduto;
    }
    public  Map<String, Object> pegarDetalhesDoProduto(Long id){

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontrado("Produto não encontrado com id: " + id));
        User usuario = userService.pegarUserPorId(produto.getUsuario().getId());

        byte[] imagemBytes = produto.getImagem();
        String imagem = imagemBytes != null ? Base64.getEncoder().encodeToString(imagemBytes) : null;
        Map<String, Object> json = new HashMap<>();
        json.put("id", produto.getId());
        json.put("nome", produto.getNome());
        json.put("preco", produto.getPreco());
        json.put("Imagem", imagem);
        json.put("Imagem_tipo", produto.getImageTipo());
        json.put("condicao", produto.getCondicao());
        json.put("detalhes", produto.getDetalhes());
        json.put("nome_do_usuario", usuario.getNome());
        json.put("id_vendedor", usuario.getId());

        return json;
    }
    public Page<ProdutoRespostaDTO> filtrarProduto(String nome, String categoria,Double precoMin, Double precoMax,
                                                  Pageable pageable){

        Categoria catEnum = null;
        if (categoria != null && !categoria.isBlank()) {
            try {
                catEnum = Categoria.valueOf(categoria.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CategoriaInvalida("Categoria invalida: " + categoria);
            }
        }
        Page<Produto> produtos = repository.filtrarProdutos(nome, catEnum, precoMin, precoMax, pageable);
        Page<ProdutoRespostaDTO> produtosDTO = produtos.map(ProdutoRespostaDTO::new);
        return  produtosDTO;

    }
    public void deletar(Long id){
        Optional<Produto> produto = repository.findById(id);
        if (produto.isEmpty() ) {
           throw new ProdutoNaoEncontrado("produto nao encontrado");
        }
        repository.deleteById(id);
    }
    public void alterarDadosProduto(DadosAlterarDadosProduto dados){
        validar.ValidarAlterarProduto(dados);
        Produto produto = repository.findById(dados.id())
                .orElseThrow(() -> new ProdutoNaoEncontrado("Produto não encontrado com id: " + dados.id()));
        produto.alterarDados(dados);
        repository.save(produto);
    }
    public Page<ProdutoRespostaDTO> pegarProdutosPorUser(Long id_usuario,Pageable pageable){
        Page<Produto> produtos = repository.pegarProdutosUsuario(id_usuario,pageable);

        Page<ProdutoRespostaDTO> produtoDTO = produtos.map(ProdutoRespostaDTO::new);
        return  produtoDTO;
    }

}
