package OMCE.OMCE.Pagamento.enums;

public enum MetodoPagamento {
    CARTAO_CREDITO,
    CARTAO_DEBITO,
    PIX;

    public String getDescricao() {
        return switch (this) {
            case CARTAO_CREDITO -> "Cartão de crédito";
            case CARTAO_DEBITO -> "Cartão de débito";
            case PIX -> "Pix";
        };
    }
}