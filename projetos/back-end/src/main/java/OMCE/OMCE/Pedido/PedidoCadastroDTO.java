package OMCE.OMCE.Pedido;

import OMCE.OMCE.Enderco.DadosEndereco;

import java.util.ArrayList;

public record PedidoCadastroDTO(ArrayList<Long> id_produtos , Long id_vendedor, Long id_comprador, double valor, DadosEndereco endereco) {
}
