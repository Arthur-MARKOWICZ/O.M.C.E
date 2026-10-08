package OMCE.OMCE.Produto.service.template;

import OMCE.OMCE.Execao.UserNaoEncontrado;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.Validacao.ValidacaoProduto;
import OMCE.reuso.template.CadastroTemplate;

import java.util.Base64;

public abstract class CadastroProdutoTemplate extends CadastroTemplate<DadosCadastroProduto, User, Produto> {

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

    @Override
    protected void validar(DadosCadastroProduto dados) {
        validarCadastro(dados);
    }

    protected void validarCadastro(DadosCadastroProduto dados) {
        validar.ValidarCadastroProduto(dados);
    }

    @Override
    protected User buscarUsuario(DadosCadastroProduto dados) {

        User user = userService.pegarUserPorId(dados.id_usuario());

        if (user == null) {
            throw new UserNaoEncontrado("Usuario nao encontrado");
        }

        return user;
    }

    @Override
    protected Produto criar(DadosCadastroProduto dados) {
        return criarProduto(dados);
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

    @Override
    protected void definirUsuario(Produto produto, User usuario) {
        produto.setUsuario(usuario);
    }

    @Override
    protected void configurar(Produto produto, DadosCadastroProduto dados) {
        configurarCategoria(produto, dados);
    }

    protected abstract void configurarCategoria(
            Produto produto,
            DadosCadastroProduto dados
    );

    @Override
    protected Produto salvar(Produto produto) {
        return repository.save(produto);
    }
}
