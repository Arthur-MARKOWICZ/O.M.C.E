package OMCE.OMCE.AvaliacaoProduto.dto;

public class AvaliacaoProdutoDTO {

    private int nota;
    private String comentario;
    private Long idProduto;

    public AvaliacaoProdutoDTO() {
    }

    public AvaliacaoProdutoDTO(int nota, String comentario, Long idProduto) {
        this.nota = nota;
        this.comentario = comentario;
        this.idProduto = idProduto;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }
}