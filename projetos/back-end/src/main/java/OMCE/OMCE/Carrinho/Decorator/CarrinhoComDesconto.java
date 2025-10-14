package OMCE.OMCE.Carrinho.Decorator;

public class CarrinhoComDesconto extends CarrinhoDecorator {

    public CarrinhoComDesconto(Carrinho carrinhoDecorado) {
        super(carrinhoDecorado);
    }

    @Override
    public double calcularTotal() {
        double total = super.calcularTotal();
        if (total >= 100.0) {
            total *= 0.9; 
        }
        return total;
    }
}
