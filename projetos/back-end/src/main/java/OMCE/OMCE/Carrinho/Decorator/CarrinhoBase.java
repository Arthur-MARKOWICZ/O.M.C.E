package OMCE.OMCE.Carrinho.Decorator;

import OMCE.OMCE.Produto.Produto;
import java.util.List;

public class CarrinhoBase implements Carrinho {

    private final List<Produto> produtos;

    public CarrinhoBase(List<Produto> produtos) {
        this.produtos = produtos;
    }

    @Override
    public double calcularTotal() {
        return produtos.stream()
                .mapToDouble(Produto::getPreco)
                .sum();
    }
}
