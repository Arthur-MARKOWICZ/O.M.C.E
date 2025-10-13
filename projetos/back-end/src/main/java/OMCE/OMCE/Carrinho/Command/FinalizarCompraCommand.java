package OMCE.OMCE.Carrinho.Command;

import OMCE.OMCE.Carrinho.Command.CarrinhoCommand;
import OMCE.OMCE.Carrinho.CarrinhoService;

public class FinalizarCompraCommand implements CarrinhoCommand {

    private final CarrinhoService carrinhoService;
    private final Long usuarioId;

    public FinalizarCompraCommand(CarrinhoService service, Long usuarioId) {
        this.carrinhoService = service;
        this.usuarioId = usuarioId;
    }

    @Override
    public void executar() {
        carrinhoService.finalizar(usuarioId);
    }
}
