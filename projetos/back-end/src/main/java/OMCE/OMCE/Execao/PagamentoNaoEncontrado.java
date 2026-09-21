package OMCE.OMCE.Execao;

public class PagamentoNaoEncontrado extends RuntimeException {
    public PagamentoNaoEncontrado(String message) {
        super(message);
    }
}