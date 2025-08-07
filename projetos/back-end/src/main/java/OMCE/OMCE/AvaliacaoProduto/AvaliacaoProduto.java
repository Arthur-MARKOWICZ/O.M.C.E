package OMCE.OMCE.AvaliacaoProduto;

import OMCE.OMCE.AvaliacaoProduto.dto.AvaliacaoProdutoDTO;
import OMCE.OMCE.Produto.Produto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AvaliacaoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int nota;

    private String comentario;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(name = "criado_em")
    private LocalDateTime dataCriacao = LocalDateTime.now();
    public AvaliacaoProduto(AvaliacaoProdutoDTO dto, Produto produto){
        this.nota = dto.getNota();
        this.comentario = dto.getComentario();
        this.produto = produto;
    }
}

