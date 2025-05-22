package OMCE.OMCE.controller;

import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendedorDTO;
import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avaliacaoVendedor")
public class AvaliacaoVendedorController {

    @Autowired
    private AvaliacaoVendorService service;

    @PostMapping("/cadastro")
    public ResponseEntity cadastro(@RequestBody AvaliacaoVendedorDTO dto){
        service.criar(dto);
        return ResponseEntity.ok().build();
    }
}
