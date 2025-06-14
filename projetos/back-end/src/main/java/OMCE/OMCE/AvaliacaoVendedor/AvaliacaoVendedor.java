package OMCE.OMCE.AvaliacaoVendedor;


import OMCE.OMCE.User.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao_vendedor")
public class AvaliacaoVendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int nota;
    private String comentario;
    private boolean recomendado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private User vendedor;
    private LocalDateTime data = LocalDateTime.now();
    public AvaliacaoVendedor(AvaliacaoVendedorDTO dto){
        this.recomendado = dto.recomendado();
        this.nota = dto.nota();;
        this.comentario = dto.comentario();
    }

    public AvaliacaoVendedor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRecomendado() {
        return recomendado;
    }

    public void setRecomendado(boolean recomendado) {
        this.recomendado = recomendado;
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

    public User getVendedor() {
        return vendedor;
    }

    public void setVendedor(User vendedor) {
        this.vendedor = vendedor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}

