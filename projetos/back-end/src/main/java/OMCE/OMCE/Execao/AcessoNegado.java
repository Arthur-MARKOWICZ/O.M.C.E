package OMCE.OMCE.Execao;

public class AcessoNegado extends RuntimeException {
    public AcessoNegado(String message) {
        super(message);
    }
}