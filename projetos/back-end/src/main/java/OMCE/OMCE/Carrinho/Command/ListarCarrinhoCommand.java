package OMCE.OMCE.Carrinho.Command;

import OMCE.OMCE.Carrinho.Command.CarrinhoCommand;
import OMCE.OMCE.Carrinho.CarrinhoService;
import OMCE.OMCE.Produto.Produto;
import java.util.List;

public class ListarCarrinhoCommand implements CarrinhoCommand {

    private final CarrinhoService carrinhoService;
    private final Long usuarioId;
    private List<Produto> resultado;

    public ListarCarrinhoCommand(CarrinhoService service, Long usuarioId) {
        this.carrinhoService = service;
        this.usuarioId = usuarioId;
    }

    @Override
    public void executar() {
        resultado = carrinhoService.getCarrinho(usuarioId);
    }

    public List<Produto> getResultado() {
        return resultado;
    }
}
