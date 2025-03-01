package OMCE.OMCE.User;

import OMCE.OMCE.Enderco.Enderco;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    @Embedded
    private Enderco enderco;
    private String cpf;

    private String dataNasc;
    private String sexo;
    private String email;
    private String telefone;
    @Column(name = "nome_user")
    private String nomeUser;
    private String senha;


    public User(DadosCadastroUser dados){
        this.nome = dados.nome();
        this.enderco = new Enderco(dados.endereco());
        this.cpf = dados.cpf();
        this.dataNasc = dados.dataNasc();
        this.sexo = dados.sexo();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.nomeUser = dados.nomeUser();
        this.senha = dados.senha();
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
