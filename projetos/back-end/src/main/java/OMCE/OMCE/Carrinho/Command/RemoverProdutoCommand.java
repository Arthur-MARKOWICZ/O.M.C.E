package OMCE.OMCE.Carrinho.Command;

import OMCE.OMCE.Carrinho.Command.CarrinhoCommand;
import OMCE.OMCE.Carrinho.CarrinhoService;

public class RemoverProdutoCommand implements CarrinhoCommand {

    private final CarrinhoService carrinhoService;
    private final Long usuarioId;
    private final Long produtoId;

    public RemoverProdutoCommand(CarrinhoService service, Long usuarioId, Long produtoId) {
        this.carrinhoService = service;
        this.usuarioId = usuarioId;
        this.produtoId = produtoId;
    }

    @Override
    public void executar() {
        carrinhoService.remover(usuarioId, produtoId);
    }
}
