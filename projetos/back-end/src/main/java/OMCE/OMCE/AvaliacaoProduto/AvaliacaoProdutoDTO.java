package OMCE.OMCE.AvaliacaoProduto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

public class AvaliacaoProdutoDTO {
    private String subistituir;
    private int nota;
    private String comentario;
    private Long idProduto; 
}

