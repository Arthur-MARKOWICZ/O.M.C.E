package OMCE.OMCE.Carrinho;

import OMCE.OMCE.Carrinho.Command.*;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoExecutor executor;
    private final CarrinhoService service;
    private final ProdutoRepository produtoRepo;

    public CarrinhoController(CarrinhoExecutor executor, CarrinhoService service, ProdutoRepository produtoRepo) {
        this.executor = executor;
        this.service = service;
        this.produtoRepo = produtoRepo;
    }

    @PostMapping("/{usuarioId}/adicionar/{produtoId}")
    public void adicionar(@PathVariable Long usuarioId, @PathVariable Long produtoId) {
        Produto produto = produtoRepo.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        executor.executarComando(new AdicionarProdutoCommand(service, usuarioId, produto));
    }

    @DeleteMapping("/{usuarioId}/remover/{produtoId}")
    public void remover(@PathVariable Long usuarioId, @PathVariable Long produtoId) {
        executor.executarComando(new RemoverProdutoCommand(service, usuarioId, produtoId));
    }

    @DeleteMapping("/{usuarioId}/limpar")
    public void limpar(@PathVariable Long usuarioId) {
        executor.executarComando(new LimparCarrinhoCommand(service, usuarioId));
    }

    @PostMapping("/{usuarioId}/finalizar")
    public void finalizar(@PathVariable Long usuarioId) {
        executor.executarComando(new FinalizarCompraCommand(service, usuarioId));
    }

    @GetMapping("/{usuarioId}")
    public List<Produto> listar(@PathVariable Long usuarioId) {
        ListarCarrinhoCommand cmd = new ListarCarrinhoCommand(service, usuarioId);
        executor.executarComando(cmd);
        return cmd.getResultado();
    }
  @GetMapping("/{usuarioId}/total")
public double calcularTotal(@PathVariable Long usuarioId) {
    return service.calcularTotal(usuarioId);
}

}

