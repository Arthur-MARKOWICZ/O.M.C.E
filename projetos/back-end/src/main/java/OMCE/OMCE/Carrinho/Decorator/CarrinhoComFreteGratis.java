package OMCE.OMCE.Carrinho.Decorator;

public class CarrinhoComFreteGratis extends CarrinhoDecorator {

    public CarrinhoComFreteGratis(Carrinho carrinhoDecorado) {
        super(carrinhoDecorado);
    }

    @Override
    public double calcularTotal() {
        double total = super.calcularTotal();
        // remove o frete padrão (20.0) se o total for alto
        if (total >= 200.0) {
            total -= 20.0;
        }
        return total;
    }
}

