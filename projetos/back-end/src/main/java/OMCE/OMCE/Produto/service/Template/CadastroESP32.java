package OMCE.OMCE.Produto.service.template;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.enums.Categoria;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.Validacao.ValidacaoProduto;
import org.springframework.stereotype.Service;

@Service
public class CadastroESP32 extends CadastroProdutoTemplate {

    public CadastroESP32(
            ProdutoRepository repository,
            UserService userService,
            ValidacaoProduto validar) {

        super(repository, userService, validar);
    }

    @Override
    protected void configurarCategoria(
            Produto produto,
            DadosCadastroProduto dados) {

        produto.setCategoria(Categoria.ESP32);
        produto.setModelo(dados.modelo());
    }
}