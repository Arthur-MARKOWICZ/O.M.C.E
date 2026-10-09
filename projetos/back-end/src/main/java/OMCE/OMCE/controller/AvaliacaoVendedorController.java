package OMCE.OMCE.controller;

import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorAtualizacaoDTO;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorDTO;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorRespostaDTO;
import OMCE.OMCE.AvaliacaoVendedor.service.AvaliacaoVendorService;

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
    public ResponseEntity<Void> cadastro(
            @RequestBody AvaliacaoVendedorDTO dto) {

        service.criar(dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/media/{id}")
    public ResponseEntity<Double> pegarMedia(
            @PathVariable Long id) {

        double media = service.calcularMedia(id);

        return ResponseEntity.ok(media);
    }

    @GetMapping("/minhas")
    public ResponseEntity<Page<AvaliacaoVendedorRespostaDTO>> listarMinhas(
            Pageable pageable) {

        return ResponseEntity.ok(service.listarMinhas(pageable));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable Long id,
            @RequestBody AvaliacaoVendedorAtualizacaoDTO dto) {

        service.atualizar(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<AvaliacaoVendedorRespostaDTO>>
    pegarAvaliacoes(
            @PathVariable Long id,
            Pageable pageable) {

        Page<AvaliacaoVendedorRespostaDTO> avaliacoes =
                service.pegarAvaliaca(pageable, id);

        return ResponseEntity.ok(avaliacoes);
    }
}
