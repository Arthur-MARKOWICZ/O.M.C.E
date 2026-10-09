package OMCE.OMCE.unitarios;

import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendedor;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorAtualizacaoDTO;
import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorDTO;
import OMCE.OMCE.AvaliacaoVendedor.repository.AvaliacaoVendedorRepository;
import OMCE.OMCE.AvaliacaoVendedor.service.AvaliacaoVendorService;
import OMCE.OMCE.Execao.AcessoNegado;
import OMCE.OMCE.User.Role;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvaliacaoVendedorServiceTest {

    @InjectMocks
    private AvaliacaoVendorService service;

    @Mock
    private AvaliacaoVendedorRepository repository;

    @Mock
    private UserRepository userRepository;

    private User avaliador;
    private User vendedor;

    @BeforeEach
    void setupSecurity() {
        avaliador = new User();
        avaliador.setId(7L);
        avaliador.setEmail("comprador@test.com");
        avaliador.setRole(Role.COMPRADOR);

        vendedor = new User();
        vendedor.setId(1L);
        vendedor.setRole(Role.VENDEDOR);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        avaliador, null, avaliador.getAuthorities()));
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveCadastrarComAvaliadorLogado() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(repository.save(any(AvaliacaoVendedor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.criar(new AvaliacaoVendedorDTO(5, "Ótimo!", 1L));

        ArgumentCaptor<AvaliacaoVendedor> captor =
                ArgumentCaptor.forClass(AvaliacaoVendedor.class);
        verify(repository).save(captor.capture());
        assertEquals(avaliador.getId(), captor.getValue().getAvaliador().getId());
        assertEquals(vendedor.getId(), captor.getValue().getVendedor().getId());
    }

    @Test
    void deveAtualizarAvaliacaoDoAutor() {
        AvaliacaoVendedor avaliacao = avaliacaoDoAutor(10L);
        when(repository.findById(10L)).thenReturn(Optional.of(avaliacao));
        when(repository.save(any(AvaliacaoVendedor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.atualizar(10L, new AvaliacaoVendedorAtualizacaoDTO(3, "Mediano"));

        assertEquals(3, avaliacao.getNota());
        assertEquals("Mediano", avaliacao.getComentario());
        verify(repository).save(avaliacao);
    }

    @Test
    void naoDeveAtualizarAvaliacaoDeOutroUsuario() {
        AvaliacaoVendedor avaliacao = avaliacaoDoAutor(10L);
        User outro = new User();
        outro.setId(99L);
        outro.setRole(Role.COMPRADOR);
        avaliacao.setAvaliador(outro);
        when(repository.findById(10L)).thenReturn(Optional.of(avaliacao));

        assertThrows(AcessoNegado.class, () ->
                service.atualizar(10L, new AvaliacaoVendedorAtualizacaoDTO(1, "Ruim")));

        verify(repository, never()).save(any(AvaliacaoVendedor.class));
    }

    @Test
    void deveDeletarAvaliacaoDoAutor() {
        AvaliacaoVendedor avaliacao = avaliacaoDoAutor(10L);
        when(repository.findById(10L)).thenReturn(Optional.of(avaliacao));

        service.deletar(10L);

        verify(repository).delete(avaliacao);
    }

    @Test
    void naoDeveDeletarAvaliacaoDeOutroUsuario() {
        AvaliacaoVendedor avaliacao = avaliacaoDoAutor(10L);
        User outro = new User();
        outro.setId(99L);
        outro.setRole(Role.COMPRADOR);
        avaliacao.setAvaliador(outro);
        when(repository.findById(10L)).thenReturn(Optional.of(avaliacao));

        assertThrows(AcessoNegado.class, () -> service.deletar(10L));

        verify(repository, never()).delete(any(AvaliacaoVendedor.class));
    }

    private AvaliacaoVendedor avaliacaoDoAutor(Long id) {
        AvaliacaoVendedor avaliacao = new AvaliacaoVendedor();
        avaliacao.setId(id);
        avaliacao.setNota(5);
        avaliacao.setComentario("Bom");
        avaliacao.setAvaliador(avaliador);
        avaliacao.setVendedor(vendedor);
        return avaliacao;
    }
}
