package OMCE.OMCE.Pedido;

import OMCE.OMCE.Enderco.Endereco;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idVendedor;
    private Long idComprador;
    @ElementCollection
    private ArrayList<Long> id_produtos = new ArrayList<>();
    private double valor;
    @Embedded
    private Endereco enderecoEntrega;

    public Pedido() {
    }
    public Pedido(PedidoCadastroDTO dados){
        this.idComprador = dados.id_comprador();
        this.id_produtos = dados.id_produtos();
        this.idVendedor = dados.id_vendedor();
        this.valor = dados.valor();
        this.enderecoEntrega = new Endereco(dados.endereco());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Long idVendedor) {
        this.idVendedor = idVendedor;
    }

    public ArrayList<Long> getId_produtos() {
        return id_produtos;
    }

    public void setId_produtos(ArrayList<Long> id_produtos) {
        this.id_produtos = id_produtos;
    }

    public Long getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(Long idComprador) {
        this.idComprador = idComprador;
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
