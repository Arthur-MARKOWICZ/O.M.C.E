package OMCE.OMCE.AvaliacaoVendedor.service;

import OMCE.OMCE.Avaliacao.service.AvaliacaoTemplateService;
import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendedor;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorAtualizacaoDTO;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorDTO;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorRespostaDTO;
import OMCE.OMCE.AvaliacaoVendedor.repository.AvaliacaoVendedorRepository;
import OMCE.OMCE.Execao.AcessoNegado;
import OMCE.OMCE.Execao.UserNaoEncontrado;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoVendorService
        extends AvaliacaoTemplateService<AvaliacaoVendedorDTO, AvaliacaoVendedor> {

    @Autowired
    private AvaliacaoVendedorRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected AvaliacaoVendedor criarAvaliacao(
            AvaliacaoVendedorDTO dto) {

        AvaliacaoVendedor avaliacao =
                new AvaliacaoVendedor(dto);

        User vendedor = userRepository
                .findById(dto.vendedor_id())
                .orElseThrow(() ->
                        new UserNaoEncontrado(
                                "Vendedor não encontrado com id: "
                                        + dto.vendedor_id()));

        avaliacao.setVendedor(vendedor);
        avaliacao.setAvaliador(usuarioLogado());

        return avaliacao;
    }

    @Override
    protected void salvarAvaliacao(
            AvaliacaoVendedor avaliacao) {

        repository.save(avaliacao);
    }

    @Override
    protected List<Integer> buscarNotas(Long idVendedor) {

        return repository.pegarTodasNotasAvaliacose(idVendedor);
    }

    public Page<AvaliacaoVendedorRespostaDTO> pegarAvaliaca(
            Pageable pageable,
            Long id) {

        Page<AvaliacaoVendedor> avaliacoes =
                repository.findByVendedorId(id, pageable);

        return avaliacoes.map(
                AvaliacaoVendedorRespostaDTO::new
        );
    }

    public Page<AvaliacaoVendedorRespostaDTO> listarMinhas(
            Pageable pageable) {

        User avaliador = usuarioLogado();
        return repository
                .findByAvaliadorId(avaliador.getId(), pageable)
                .map(AvaliacaoVendedorRespostaDTO::new);
    }

    public void atualizar(
            Long id,
            AvaliacaoVendedorAtualizacaoDTO dto) {

        AvaliacaoVendedor avaliacao = buscarPorId(id);
        garantirAutor(avaliacao);
        avaliacao.setNota(dto.nota());
        avaliacao.setComentario(dto.comentario());
        repository.save(avaliacao);
    }

    public void deletar(Long id) {
        AvaliacaoVendedor avaliacao = buscarPorId(id);
        garantirAutor(avaliacao);
        repository.delete(avaliacao);
    }

    private AvaliacaoVendedor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Avaliação de vendedor não encontrada com id: "
                                        + id));
    }

    private void garantirAutor(AvaliacaoVendedor avaliacao) {
        User logado = usuarioLogado();
        if (avaliacao.getAvaliador() == null
                || avaliacao.getAvaliador().getId() != logado.getId()) {
            throw new AcessoNegado(
                    "Você só pode alterar ou excluir avaliações que escreveu.");
        }
    }

    private User usuarioLogado() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UserNaoEncontrado("Usuário não autenticado");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        User user = userRepository.findByEmail(authentication.getName());
        if (user == null) {
            throw new UserNaoEncontrado(
                    "Usuário não encontrado: " + authentication.getName());
        }
        return user;
    }
}
