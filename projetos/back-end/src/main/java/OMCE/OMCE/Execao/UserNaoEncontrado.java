package OMCE.OMCE.Execao;

public class UserNaoEncontrado extends RuntimeException {
    public UserNaoEncontrado(String message) {
        super(message);
    }
}
