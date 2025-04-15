package OMCE.OMCE.Enderco;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable

@NoArgsConstructor
public class Endereco {
    private String cep;
    private String pais;
    private String estado;
    private String cidade;
    private String logradouro;
    public Endereco(DadosEndereco dados){
        this.cep = dados.cep();
        this.pais = dados.pais();
        this.estado = dados.estado();
        this.cidade = dados.cidade();
        this.logradouro = dados.logradouro();
    }

    public String getCep() {
        return cep;
    }

    public String getPais() {
        return pais;
    }

    public String getEstado() {
        return estado;
    }

    public String getCidade() {
        return cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }
}
