package OMCE.OMCE.controller;

import OMCE.OMCE.Pedido.PedidoCadastroDTO;
import OMCE.OMCE.Pedido.PedidoRepository;
import OMCE.OMCE.Pedido.PedidoService;
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
    public ResponseEntity cadastroPedido(@RequestBody PedidoCadastroDTO dados){
        pedidoService.CadastroCompra(dados);
        return ResponseEntity.ok().build();
    }
}
