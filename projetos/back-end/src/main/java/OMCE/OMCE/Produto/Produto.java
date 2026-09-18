package OMCE.OMCE.Produto;

import OMCE.OMCE.Produto.dto.DadosAlterarDadosProduto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.enums.Categoria;
import OMCE.OMCE.Produto.enums.Condicao;
import OMCE.OMCE.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.Base64;

import org.springframework.data.jpa.repository.JpaRepository;


@NoArgsConstructor
@Entity
@Table(name = "produto")
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private Double preco;
    private String detalhes;
    private String modelo;
    private String voltagem;
    private String carga;
    private String comprimento;
    private String tipo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @JsonBackReference
    private User usuario;
    private boolean vendido;
    @Lob
    private byte[] imagem;
    @Column(name = "imagem_tipo")
    private String imageTipo;
    @Enumerated(EnumType.STRING)
    private Condicao condicao;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

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

    public Condicao getCondicao() {
        return condicao;
    }

    public void setCondicao(Condicao condicao) {
        this.condicao = condicao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getVoltagem() {
        return voltagem;
    }

    public void setVoltagem(String voltagem) {
        this.voltagem = voltagem;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public String getComprimento() {
        return comprimento;
    }

    public void setComprimento(String comprimento) {
        this.comprimento = comprimento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public interface ProdutoRepository extends JpaRepository<Produto, Long> {}

}
