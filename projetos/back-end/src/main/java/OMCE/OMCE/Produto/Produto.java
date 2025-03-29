package OMCE.OMCE.Produto;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "produto")
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private Double preco;
    private String detalhes;
    private long id_usuario;
    public Produto(DadosCadastroProduto dados){
        this.nome = dados.nome();
        this.preco = dados.preco();
        this.detalhes = dados.detalhes();
        this.id_usuario = dados.id_usuario();
    }
}
