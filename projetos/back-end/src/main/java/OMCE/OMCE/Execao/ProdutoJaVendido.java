package OMCE.OMCE.Execao;

public class ProdutoJaVendido extends RuntimeException {
    public ProdutoJaVendido(String message) {
        super(message);
    }

    public static ProdutoJaVendido aoExcluir() {
        return new ProdutoJaVendido("Este produto já foi vendido e não pode ser excluído, pois faz parte do histórico de compras de quem o comprou.");
    }

    public static ProdutoJaVendido aoEditar() {
        return new ProdutoJaVendido("Este produto já foi vendido e não pode mais ser editado, para não alterar o histórico de quem o comprou.");
    }
}