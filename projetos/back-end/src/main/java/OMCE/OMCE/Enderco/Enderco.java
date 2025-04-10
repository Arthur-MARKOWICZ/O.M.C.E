package OMCE.OMCE.Enderco;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable

@NoArgsConstructor
public class Enderco {
    private String cep;
    private String pais;
    private String estado;
    private String cidade;
    private String logradouro;
    public Enderco(DadosEnderco dados){
        this.cep = dados.cep();
        this.pais = dados.pais();
        this.estado = dados.estado();
        this.cidade = dados.cidade();
        this.logradouro = dados.logradouro();
    }
}
