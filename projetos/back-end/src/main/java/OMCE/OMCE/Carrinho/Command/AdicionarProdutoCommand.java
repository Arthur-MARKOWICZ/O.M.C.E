package OMCE.OMCE.Carrinho.Command;

import OMCE.OMCE.Carrinho.Command.CarrinhoCommand;
import OMCE.OMCE.Carrinho.CarrinhoService;
import OMCE.OMCE.Produto.Produto;

public class AdicionarProdutoCommand implements CarrinhoCommand {

    private final CarrinhoService carrinhoService;
    private final Long usuarioId;
    private final Produto produto;

    public AdicionarProdutoCommand(CarrinhoService service, Long usuarioId, Produto produto) {
        this.carrinhoService = service;
        this.usuarioId = usuarioId;
        this.produto = produto;
    }

    @Override
    public void executar() {
        carrinhoService.adicionar(usuarioId, produto);
    }
}
