package OMCE.OMCE.Pedido.dto;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Pagamento.enums.MetodoPagamento;

import java.util.ArrayList;

public record PedidoCadastroDTO(ArrayList<Long> id_produtos , Long id_comprador, double valor, DadosEndereco endereco, MetodoPagamento metodoPagamento) {
}