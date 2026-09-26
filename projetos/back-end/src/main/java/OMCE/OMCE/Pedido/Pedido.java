package OMCE.OMCE.Pedido;

import OMCE.OMCE.Enderco.Endereco;
import OMCE.OMCE.Entrega.EntregaCalculada;
import OMCE.OMCE.Entrega.TipoEntrega;
import OMCE.OMCE.Pedido.dto.PedidoCadastroDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_entrega")
    private TipoEntrega tipoEntrega;
    @Column(name = "valor_frete")
    private double valorFrete;
    @Column(name = "prazo_entrega_dias")
    private int prazoEntregaDias;

    public Pedido() {
    }
    public Pedido(PedidoCadastroDTO dados, EntregaCalculada entrega){
        this.compradorId = dados.id_comprador();
        this.enderecoEntrega = new Endereco(dados.endereco());
        this.dataPedido = LocalDateTime.now();
        this.tipoEntrega = entrega.tipo();
        this.valorFrete = entrega.valorFrete();
        this.prazoEntregaDias = entrega.prazoDias();
        this.valor = dados.valor() + entrega.valorFrete();
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

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public int getPrazoEntregaDias() {
        return prazoEntregaDias;
    }

    public void setPrazoEntregaDias(int prazoEntregaDias) {
        this.prazoEntregaDias = prazoEntregaDias;
    }
}