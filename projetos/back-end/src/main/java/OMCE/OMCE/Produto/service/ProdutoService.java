package OMCE.OMCE.Produto.service;

import OMCE.OMCE.Execao.CategoriaInvalida;
import OMCE.OMCE.Execao.ProdutoNaoEncontrado;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosAlterarDadosProduto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.dto.ProdutoRespostaDTO;
import OMCE.OMCE.Produto.enums.Categoria;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.Produto.service.template.CadastroArduino;
import OMCE.OMCE.Produto.service.template.CadastroBateria;
import OMCE.OMCE.Produto.service.template.CadastroCabo;
import OMCE.OMCE.Produto.service.template.CadastroConector;
import OMCE.OMCE.Produto.service.template.CadastroESP32;
import OMCE.OMCE.Produto.service.template.CadastroMotor;
import OMCE.OMCE.Produto.service.template.CadastroOutro;
import OMCE.OMCE.Produto.service.template.CadastroResistor;
import OMCE.OMCE.Produto.service.template.CadastroSensor;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.Validacao.ValidacaoProduto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProdutoService {

    private final ProdutoRepository repository;
    private final UserService userService;

    private final CadastroESP32 cadastroESP32;
    private final CadastroArduino cadastroArduino;
    private final CadastroResistor cadastroResistor;
    private final CadastroSensor cadastroSensor;
    private final CadastroBateria cadastroBateria;
    private final CadastroCabo cadastroCabo;
    private final CadastroMotor cadastroMotor;
    private final CadastroConector cadastroConector;
    private final CadastroOutro cadastroOutro;

    public ProdutoService(
            ProdutoRepository repository,
            UserService userService,
            CadastroESP32 cadastroESP32,
            CadastroArduino cadastroArduino,
            CadastroResistor cadastroResistor,
            CadastroSensor cadastroSensor,
            CadastroBateria cadastroBateria,
            CadastroCabo cadastroCabo,
            CadastroMotor cadastroMotor,
            CadastroConector cadastroConector,
            CadastroOutro cadastroOutro) {

        this.repository = repository;
        this.userService = userService;

        this.cadastroESP32 = cadastroESP32;
        this.cadastroArduino = cadastroArduino;
        this.cadastroResistor = cadastroResistor;
        this.cadastroSensor = cadastroSensor;
        this.cadastroBateria = cadastroBateria;
        this.cadastroCabo = cadastroCabo;
        this.cadastroMotor = cadastroMotor;
        this.cadastroConector = cadastroConector;
        this.cadastroOutro = cadastroOutro;
    }

    public Produto cadastro(DadosCadastroProduto dados) {

        return switch (dados.categoria()) {

            case ESP32 -> cadastroESP32.cadastrar(dados);

            case ARDUINO -> cadastroArduino.cadastrar(dados);

            case REGISTORES -> cadastroResistor.cadastrar(dados);

            case SENSORES -> cadastroSensor.cadastrar(dados);

            case BATERIA -> cadastroBateria.cadastrar(dados);

            case CABOS -> cadastroCabo.cadastrar(dados);

            case MOTORES -> cadastroMotor.cadastrar(dados);

            case CONECTORES -> cadastroConector.cadastrar(dados);

            case OUTRO -> cadastroOutro.cadastrar(dados);
        };
    }

    public Map<String, Object> pegarDetalhesDoProduto(Long id) {

        Produto produto = repository.findById(id)
                .orElseThrow(() ->
                        new ProdutoNaoEncontrado(
                                "Produto não encontrado com id: " + id
                        )
                );

        User usuario = userService.pegarUserPorId(
                produto.getUsuario().getId()
        );

        byte[] imagemBytes = produto.getImagem();

        String imagem = imagemBytes != null
                ? Base64.getEncoder().encodeToString(imagemBytes)
                : null;

        Map<String, Object> json = new HashMap<>();

        json.put("id", produto.getId());
        json.put("nome", produto.getNome());
        json.put("preco", produto.getPreco());
        json.put("Imagem", imagem);
        json.put("Imagem_tipo", produto.getImageTipo());
        json.put("condicao", produto.getCondicao());
        json.put("categoria", produto.getCategoria());
        json.put("detalhes", produto.getDetalhes());
        json.put("nome_do_usuario", usuario.getNome());
        json.put("id_vendedor", usuario.getId());
        json.put("modelo", produto.getModelo());
        json.put("voltagem", produto.getVoltagem());
        json.put("carga", produto.getCarga());
        json.put("comprimento", produto.getComprimento());
        json.put("tipo", produto.getTipo());

        return json;
    }

    public Page<ProdutoRespostaDTO> filtrarProduto(
            String nome,
            String categoria,
            Double precoMin,
            Double precoMax,
            Pageable pageable) {

        Categoria catEnum = null;

        if (categoria != null && !categoria.isBlank()) {

            try {

                catEnum = Categoria.valueOf(
                        categoria.toUpperCase()
                );

            } catch (IllegalArgumentException e) {

                throw new CategoriaInvalida(
                        "Categoria invalida: " + categoria
                );
            }
        }

        Page<Produto> produtos = repository.filtrarProdutos(
                nome,
                catEnum,
                precoMin,
                precoMax,
                pageable
        );

        return produtos.map(ProdutoRespostaDTO::new);
    }

    public void deletar(Long id) {

        Optional<Produto> produto = repository.findById(id);

        if (produto.isEmpty()) {
            throw new ProdutoNaoEncontrado(
                    "produto nao encontrado"
            );
        }

        repository.deleteById(id);
    }

    public void alterarDadosProduto(
            DadosAlterarDadosProduto dados) {

        Produto produto = repository.findById(dados.id())
                .orElseThrow(() ->
                        new ProdutoNaoEncontrado(
                                "Produto não encontrado com id: "
                                        + dados.id()
                        )
                );

        produto.alterarDados(dados);

        repository.save(produto);
    }

    public Page<ProdutoRespostaDTO> pegarProdutosPorUser(
            Long id_usuario,
            Pageable pageable) {

        Page<Produto> produtos =
                repository.pegarProdutosUsuario(
                        id_usuario,
                        pageable
                );

        return produtos.map(ProdutoRespostaDTO::new);
    }
}