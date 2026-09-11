package OMCE.OMCE.controller;

import OMCE.OMCE.Historico.HistoricoService;
import OMCE.OMCE.Historico.exportacao.FormatoExportacao;
import OMCE.OMCE.Historico.exportacao.dto.ArquivoExportado;
import OMCE.OMCE.Produto.dto.ProdutoRespostaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/historico")
public class HistoricoController {
    @Autowired
    private HistoricoService historicoService;
    @GetMapping("/vendas")
    public ResponseEntity<Page<ProdutoRespostaDTO>> historicoVendas(@PageableDefault(size=10) Pageable pageable, @RequestHeader("Id-Usuario") Long id_usuario){
        Page<ProdutoRespostaDTO> produtos = historicoService.pegarHistoricoDeVenda(id_usuario,pageable);
        return ResponseEntity.ok(produtos);
    }
    @GetMapping("/compra")
    public ResponseEntity<Page<ProdutoRespostaDTO>> historicoCompra(@PageableDefault(size=10) Pageable pageable, @RequestHeader("Id-Usuario") Long id_usuario){
        Page<ProdutoRespostaDTO> produtos = historicoService.pegarHistoricoDeCompra(id_usuario,pageable);
        return ResponseEntity.ok(produtos);
    }
    @GetMapping("/compra/exportar")
    public ResponseEntity<byte[]> exportarHistoricoCompra(
            @RequestHeader("Id-Usuario") Long id_usuario,
            @RequestParam FormatoExportacao formato,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim){
        ArquivoExportado arquivo = historicoService.exportarHistoricoDeCompra(id_usuario, formato, dataInicio, dataFim);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(arquivo.nomeArquivo()).build().toString())
                .body(arquivo.conteudo());
    }
}
