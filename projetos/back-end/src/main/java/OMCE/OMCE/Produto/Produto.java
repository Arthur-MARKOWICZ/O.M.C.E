package OMCE.OMCE.Produto;

import OMCE.OMCE.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.Base64;
import OMCE.OMCE.Produto.DadosAlterarDadosProduto;


@NoArgsConstructor
@Entity
@Table(name = "produto")
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private Double preco;
    private String detalhes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @JsonBackReference
    private User usuario;
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
        this.usuario = new User();
        this.imagem = Base64.getDecoder().decode(dados.imagem());
        this.imageTipo = dados.imagem_tipo();
    }
    @Override
    public String toString() {
        return "Produto{id=" + id + ", nome=" + nome + ", preco=" + preco + "}";
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
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

    public void alterarDados(DadosAlterarDadosProduto dados) {
        this.nome = dados.nome();
        this.preco = dados.preco();
        this.detalhes = dados.detalhes();
        if(dados.imagem() != null){
            this.imagem = Base64.getDecoder().decode(dados.imagem());
            this.imageTipo = dados.imagem_tipo();
        }

    }
}
