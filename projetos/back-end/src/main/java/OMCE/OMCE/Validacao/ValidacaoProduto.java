package OMCE.OMCE.Validacao;

import OMCE.OMCE.Produto.DadosAlterarDadosProduto;
import OMCE.OMCE.Produto.DadosCadastroProduto;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoProduto {
    public void ValidarCadastroProduto(DadosCadastroProduto dados){
        if(dados.nome() == null){
            throw new RuntimeException("O produto nao tem nome,adicionar o nome");
        }
        if(dados.detalhes() == null){
            throw  new RuntimeException("O detalhes do produto esta em branco");
        }
        if(dados.imagem() == null){
            throw   new RuntimeException("O produto esta sem imagem");
        }
        if(dados.imagem_tipo() == null){
            throw  new RuntimeException("O produto esta sem imagem");
        }
        if(dados.preco() <= 0){
            throw new RuntimeException("O produto esta com um preco invalido");
        }
    }
    public void ValidarAlterarProduto(DadosAlterarDadosProduto dados){
        if(dados.nome() == null){
            throw new RuntimeException("O produto nao tem nome,adicionar o nome");
        }
        if(dados.detalhes() == null){
            throw  new RuntimeException("O detalhes do produto esta em branco");
        }
        if(dados.imagem() == null){
            throw  new RuntimeException("O produto esta sem imagem");
        }
        if(dados.imagem_tipo() == null){
            throw  new RuntimeException("O produto esta sem imagem");
        }
        if(dados.preco() <= 0){
            throw new RuntimeException("O produto esta com um preco invalido");
        }
    }

}
