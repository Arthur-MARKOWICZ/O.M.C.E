package OMCE.OMCE.Carrinho.Decorator;

public abstract class CarrinhoDecorator implements Carrinho {

    protected final Carrinho carrinhoDecorado;

    public CarrinhoDecorator(Carrinho carrinhoDecorado) {
        this.carrinhoDecorado = carrinhoDecorado;
    }

    @Override
    public double calcularTotal() {
        return carrinhoDecorado.calcularTotal();
    }
}
