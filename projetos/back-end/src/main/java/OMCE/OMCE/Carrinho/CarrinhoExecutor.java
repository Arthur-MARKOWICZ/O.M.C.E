package OMCE.OMCE.Carrinho;

import OMCE.OMCE.Carrinho.Command.CarrinhoCommand;
import org.springframework.stereotype.Component;

@Component
public class CarrinhoExecutor {

    public void executarComando(CarrinhoCommand command) {
        command.executar();
    }
}
