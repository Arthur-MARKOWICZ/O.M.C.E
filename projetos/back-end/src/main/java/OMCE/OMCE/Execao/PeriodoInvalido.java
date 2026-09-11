package OMCE.OMCE.Execao;

public class PeriodoInvalido extends RuntimeException {
    public PeriodoInvalido(String mensagem) {
        super(mensagem);
    }
}
