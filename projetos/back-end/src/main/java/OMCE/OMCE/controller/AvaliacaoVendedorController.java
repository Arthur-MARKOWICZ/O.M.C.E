package OMCE.OMCE.controller;

import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendedorDTO;
import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendedorRespostaDTO;
import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/media/{id}")
    public  ResponseEntity<Double> pegarMedia(@PathVariable Long id){
        double media=  service.mediaAvaliacao(id);
        return ResponseEntity.ok(media);
    }
    @GetMapping("/{id}")
    public  ResponseEntity<Page<AvaliacaoVendedorRespostaDTO>> pegarAvaliacoes(@PathVariable Long id, Pageable pageable){
        Page<AvaliacaoVendedorRespostaDTO> avaliacoes = service.pegarAvaliaca(pageable,id);
        return ResponseEntity.ok(avaliacoes);
    }
}
