package OMCE.OMCE.AvaliacaoVendedor.service;

import OMCE.OMCE.Avaliacao.service.AvaliacaoTemplateService;
import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendedor;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorDTO;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorRespostaDTO;
import OMCE.OMCE.AvaliacaoVendedor.repository.AvaliacaoVendedorRepository;
import OMCE.OMCE.Execao.UserNaoEncontrado;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}