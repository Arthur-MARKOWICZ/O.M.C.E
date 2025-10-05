package OMCE.OMCE.unitarios;

import OMCE.OMCE.AvaliacaoVendedor.dto.AvaliacaoVendedorDTO;
import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendedor;
import OMCE.OMCE.AvaliacaoVendedor.repository.AvaliacaoVendedorRepository;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AvaliacaoVendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvaliacaoVendedorRepository avaliacaoVendedorRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User vendedor;

    @BeforeEach
    void setup() {
        vendedor = new User();
        vendedor.setId(1L);
        vendedor.setNome("Carlos Vendedor");

        when(userRepository.getReferenceById(1L)).thenReturn(vendedor);

        when(avaliacaoVendedorRepository.save(any(AvaliacaoVendedor.class)))
                .thenAnswer(invocation -> {
                    AvaliacaoVendedor arg = invocation.getArgument(0);
                    arg.setId(123L);
                    return arg;
                });
    }

    @Test
    @DisplayName("3 - Deve fazer o cadastro de uma avaliação de um vendedor")
    @WithMockUser(roles = {"USER"})
    void deveCadastrarAvaliacaoVendedor() throws Exception {
        AvaliacaoVendedorDTO dto = new AvaliacaoVendedorDTO(5, "Ótimo vendedor!", 1L);

        mockMvc.perform(post("/avaliacaoVendedor/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        org.mockito.Mockito.verify(avaliacaoVendedorRepository, org.mockito.Mockito.times(1))
                .save(any(AvaliacaoVendedor.class));
    }
}