package OMCE.OMCE.Produto;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.Base64;


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
    private boolean vendido;
    @Lob
    private byte[] imagem;
    @Column(name = "imagem_tipo")
    private String imageTipo;

    public Produto(DadosCadastroProduto dados){
        this.vendido = false;
        this.nome = dados.nome();
        this.preco = dados.preco();
        this.detalhes = dados.detalhes();
        this.id_usuario = dados.id_usuario();
        this.imagem = Base64.getDecoder().decode(dados.imagem());
        this.imageTipo = dados.imagem_tipo();
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

    public boolean isVendido() {
        return vendido;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getImageTipo() {
        return imageTipo;
    }

    public void setImageTipo(String imageTipo) {
        this.imageTipo = imageTipo;
    }
}
