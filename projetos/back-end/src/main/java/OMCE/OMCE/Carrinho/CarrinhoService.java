package OMCE.OMCE.Carrinho;

import OMCE.OMCE.Produto.Produto;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CarrinhoService {

    private final Map<Long, List<Produto>> carrinhos = new HashMap<>();

    public List<Produto> getCarrinho(Long usuarioId) {
        return carrinhos.getOrDefault(usuarioId, new ArrayList<>());
    }

    public void adicionar(Long usuarioId, Produto produto) {
        List<Produto> carrinho = carrinhos.computeIfAbsent(usuarioId, id -> new ArrayList<>());
        if (carrinho.stream().noneMatch(p -> p.getId() == produto.getId())) {
            carrinho.add(produto);
        }
    }

    public void remover(Long usuarioId, Long produtoId) {
        List<Produto> carrinho = carrinhos.get(usuarioId);
        if (carrinho != null) {
            carrinho.removeIf(p -> p.getId() == produtoId);
        }
    }

    public void limpar(Long usuarioId) {
        carrinhos.remove(usuarioId);
    }

    public void finalizar(Long usuarioId) {
        List<Produto> itens = carrinhos.get(usuarioId);
        System.out.println("Compra finalizada: " + itens);
        carrinhos.remove(usuarioId);
    }
}
