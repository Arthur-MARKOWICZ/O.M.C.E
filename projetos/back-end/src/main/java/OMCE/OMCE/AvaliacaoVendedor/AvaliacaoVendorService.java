package OMCE.OMCE.AvaliacaoVendedor;


import OMCE.OMCE.User.User;
import OMCE.OMCE.User.UserRepository;
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
        User vendedor = userRepository.getReferenceById(dto.vendedor_id());
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
        return avaliacoes.map(avaliacao -> new AvaliacaoVendedorRespostaDTO(avaliacao));
    }
}
