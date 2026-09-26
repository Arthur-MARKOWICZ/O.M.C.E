package OMCE.OMCE.controller;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Enderco.Endereco;
import OMCE.OMCE.Entrega.EntregaCalculada;
import OMCE.OMCE.Entrega.EntregaContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/entrega")
public class EntregaController {

    @Autowired
    private EntregaContext entregaContext;

    @GetMapping("/opcoes")
    public List<EntregaCalculada> opcoes(@RequestParam double valor, @RequestParam(required = false) String estado) {
        Endereco endereco = new Endereco(new DadosEndereco(null, null, estado, null, null));
        return entregaContext.todas().stream()
                .map(estrategia -> estrategia.calcular(valor, endereco))
                .sorted((a, b) -> Integer.compare(a.prazoDias(), b.prazoDias()))
                .toList();
    }
}