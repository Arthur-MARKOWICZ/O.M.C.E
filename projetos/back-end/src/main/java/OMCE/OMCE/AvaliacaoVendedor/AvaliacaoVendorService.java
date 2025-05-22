package OMCE.OMCE.AvaliacaoVendedor;


import OMCE.OMCE.User.User;
import OMCE.OMCE.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
