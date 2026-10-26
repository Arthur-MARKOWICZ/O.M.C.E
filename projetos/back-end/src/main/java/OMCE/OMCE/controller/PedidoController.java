package OMCE.OMCE.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import OMCE.OMCE.Pedido.dto.PedidoCadastroDTO;
import OMCE.OMCE.Pedido.service.PedidoService;
import OMCE.OMCE.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastroPedido(@RequestBody PedidoCadastroDTO dados, @AuthenticationPrincipal User usuarioLogado){
        pedidoService.CadastroCompra(dados, usuarioLogado.getId());
        return ResponseEntity.ok().build();
    }
}