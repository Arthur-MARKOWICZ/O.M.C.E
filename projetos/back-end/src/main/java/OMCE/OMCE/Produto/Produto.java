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
    @Override
    public String toString() {
        return "Produto{id=" + id + ", nome=" + nome + ", preco=" + preco + "}";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }
}
