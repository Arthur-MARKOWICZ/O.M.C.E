package OMCE.OMCE.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import OMCE.OMCE.Pagamento.dto.PagamentoRespostaDTO;
import OMCE.OMCE.Pagamento.service.PagamentoService;
import OMCE.OMCE.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<PagamentoRespostaDTO> buscarPorPedido(@PathVariable Long pedidoId, @AuthenticationPrincipal User usuarioLogado) {
        return ResponseEntity.ok(pagamentoService.buscarPorPedido(pedidoId, usuarioLogado.getId()));
    }
}