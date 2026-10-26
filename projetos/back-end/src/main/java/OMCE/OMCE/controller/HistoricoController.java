package OMCE.OMCE.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import OMCE.OMCE.Historico.HistoricoService;
import OMCE.OMCE.Historico.exportacao.FormatoExportacao;
import OMCE.OMCE.Historico.exportacao.dto.ArquivoExportado;
import OMCE.OMCE.Produto.dto.ProdutoRespostaDTO;
import OMCE.OMCE.User.User;
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
    public ResponseEntity<Page<ProdutoRespostaDTO>> historicoVendas(@PageableDefault(size=10) Pageable pageable,
                                                                    @AuthenticationPrincipal User usuarioLogado,
                                                                    @RequestParam(required = false) String nome,
                                                                    @RequestParam(required = false) String categoria,
                                                                    @RequestParam(required = false) Double precoMin,
                                                                    @RequestParam(required = false) Double precoMax){
        Page<ProdutoRespostaDTO> produtos = historicoService.pegarHistoricoDeVenda(usuarioLogado.getId(), nome, categoria, precoMin, precoMax, pageable);
        return ResponseEntity.ok(produtos);
    }
    @GetMapping("/compra")
    public ResponseEntity<Page<ProdutoRespostaDTO>> historicoCompra(@PageableDefault(size=10) Pageable pageable,
                                                                    @AuthenticationPrincipal User usuarioLogado,
                                                                    @RequestParam(required = false) String nome,
                                                                    @RequestParam(required = false) String categoria,
                                                                    @RequestParam(required = false) Double precoMin,
                                                                    @RequestParam(required = false) Double precoMax){
        Page<ProdutoRespostaDTO> produtos = historicoService.pegarHistoricoDeCompra(usuarioLogado.getId(), nome, categoria, precoMin, precoMax, pageable);
        return ResponseEntity.ok(produtos);
    }
    @GetMapping("/compra/exportar")
    public ResponseEntity<byte[]> exportarHistoricoCompra(
            @AuthenticationPrincipal User usuarioLogado,
            @RequestParam FormatoExportacao formato,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim){
        ArquivoExportado arquivo = historicoService.exportarHistoricoDeCompra(usuarioLogado.getId(), formato, dataInicio, dataFim);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(arquivo.nomeArquivo()).build().toString())
                .body(arquivo.conteudo());
    }
}