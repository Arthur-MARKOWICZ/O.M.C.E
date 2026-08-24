package OMCE.OMCE.AvaliacaoVendedor.service;


import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendedor;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorDTO;
import OMCE.OMCE.AvaliacaoVendedor.repository.AvaliacaoVendedorRepository;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorRespostaDTO;
import OMCE.OMCE.Execao.UserNaoEncontrado;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoVendorService {
    @Autowired
    private AvaliacaoVendedorRepository repository;
    @Autowired
    private UserRepository userRepository;

    public void criar(AvaliacaoVendedorDTO dto){
        AvaliacaoVendedor avaliacao = new AvaliacaoVendedor(dto);
        User vendedor = userRepository.findById(dto.vendedor_id())
                .orElseThrow(() -> new UserNaoEncontrado("Vendedor não encontrado com id: " + dto.vendedor_id()));
        avaliacao.setVendedor(vendedor);
        repository.save(avaliacao);
    }
    public double mediaAvaliacao(Long id) {
        List<Integer> avaliacao = repository.pegarTodasNotasAvaliacose(id);
        double soma = avaliacao.stream()
                .mapToDouble(Integer::doubleValue)
                .sum();

        double media = soma / avaliacao.size();
        return media;
    }
    public Page<AvaliacaoVendedorRespostaDTO> pegarAvaliaca(Pageable pageable, Long id){
        Page<AvaliacaoVendedor> avaliacoes = repository.findByVendedorId(id,pageable);
        return avaliacoes.map(AvaliacaoVendedorRespostaDTO::new);
    }
}
