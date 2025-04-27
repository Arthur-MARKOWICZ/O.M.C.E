package OMCE.OMCE.Produto;

import OMCE.OMCE.User.User;
import OMCE.OMCE.User.UserRepository;
import OMCE.OMCE.controller.ProdutoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private MockMvc mvc;

    private ProdutoController produtoController;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private UserRepository userRepository;

    private Page<Produto> produtosPage;
    @BeforeEach
    void setup(){
        produtoController = new ProdutoController(produtoRepository,userRepository);
        mockMvc = MockMvcBuilders
                .standaloneSetup(produtoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver() {
                })
                .build();

        // Dados de exemplo
        User u = new User();
        u.setId(1L);
        u.setNome("test");
        u.setEmail("test@example.com");

        Produto p1 = Mockito.mock(Produto.class);
        when(p1.getId()).thenReturn(1L);
        when(p1.getNome()).thenReturn("Arduino");
        when(p1.getPreco()).thenReturn(100.0);
        when(p1.getUsuario()).thenReturn(u);

        Page<Produto> page = new PageImpl<>(List.of(p1), PageRequest.of(0, 10), 1);
        when(produtoRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
    }
    @Test
    void listarTodosProduto_deveRetornarPagina()    throws Exception{
        mockMvc.perform(get("/produto/todos")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Arduino"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

}
