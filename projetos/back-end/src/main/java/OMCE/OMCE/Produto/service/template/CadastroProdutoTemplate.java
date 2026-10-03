package OMCE.OMCE.Produto.service.template;

import OMCE.OMCE.Execao.UserNaoEncontrado;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.Validacao.ValidacaoProduto;

import java.util.Base64;

public abstract class CadastroProdutoTemplate {

    protected final ProdutoRepository repository;
    protected final UserService userService;
    protected final ValidacaoProduto validar;

    public CadastroProdutoTemplate(
            ProdutoRepository repository,
            UserService userService,
            ValidacaoProduto validar) {

        this.repository = repository;
        this.userService = userService;
        this.validar = validar;
    }

    // TEMPLATE METHOD
    public Produto cadastrar(DadosCadastroProduto dados) {

        validarCadastro(dados);

        User usuario = buscarUsuario(dados);

        Produto produto = criarProduto(dados);

        definirUsuario(produto, usuario);

        configurarCategoria(produto, dados);

        return salvar(produto);
    }

    protected void validarCadastro(DadosCadastroProduto dados) {
        validar.ValidarCadastroProduto(dados);
    }

    protected User buscarUsuario(DadosCadastroProduto dados) {

        User user = userService.pegarUserPorId(dados.id_usuario());

        if (user == null) {
            throw new UserNaoEncontrado("Usuario nao encontrado");
        }

        return user;
    }

    protected Produto criarProduto(DadosCadastroProduto dados) {

        Produto produto = new Produto();

        produto.setNome(dados.nome());
        produto.setPreco(dados.preco());
        produto.setDetalhes(dados.detalhes());
        produto.setVendido(false);
        produto.setCondicao(dados.condicao());

        if (dados.imagem() != null) {
            produto.setImagem(
                    Base64.getDecoder().decode(dados.imagem())
            );

            produto.setImageTipo(dados.imagem_tipo());
        }

        return produto;
    }

    protected void definirUsuario(Produto produto, User usuario) {
        produto.setUsuario(usuario);
    }

    protected abstract void configurarCategoria(
            Produto produto,
            DadosCadastroProduto dados
    );

    protected Produto salvar(Produto produto) {
        return repository.save(produto);
    }
}