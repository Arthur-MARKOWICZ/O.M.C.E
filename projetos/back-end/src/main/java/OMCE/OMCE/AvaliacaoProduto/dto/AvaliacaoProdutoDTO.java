package OMCE.OMCE.AvaliacaoProduto.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

public class AvaliacaoProdutoDTO {
    private int nota;
    private String comentario;
    private Long idProduto; 
}

