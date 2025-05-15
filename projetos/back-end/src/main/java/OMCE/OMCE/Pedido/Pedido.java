package OMCE.OMCE.Pedido;

import OMCE.OMCE.Enderco.Endereco;
import OMCE.OMCE.Produto.Produto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id")
    private Long id;
    private Long compradorId;
    private double valor;
    @Embedded
    private Endereco enderecoEntrega;

    public Pedido() {
    }
    public Pedido(PedidoCadastroDTO dados){
        this.compradorId = dados.id_comprador();
        this.valor = dados.valor();
        this.enderecoEntrega = new Endereco(dados.endereco());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getCompradorId() {
        return compradorId;
    }

    public void setCompradorId(Long compradorId) {
        this.compradorId = compradorId;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
}
