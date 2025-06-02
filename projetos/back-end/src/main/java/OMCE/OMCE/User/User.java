package OMCE.OMCE.User;

import OMCE.OMCE.Enderco.Endereco;
import OMCE.OMCE.Produto.Produto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String subistituir;
    private String nome;
    @Embedded
    private Endereco endereco;
    private String cpf;
    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference
    private List<Produto> produtos;
    private String dataNasc;
    private String sexo;
    private String email;
    private String telefone;
    @Column(name = "nome_user")
    private String nomeUser;
    private String senha;
    private boolean ativo;
    @Column(name = "token_redefinicao")
    private String tokenRedefinicao;
    @Column(name = "token_expiracao")
    private LocalDateTime tokenExpiracao;
    public User(){}
    public User(DadosCadastroUser dados){
        this.ativo = true;
        this.subistituir = dados.subistituir();
        this.nome = dados.nome();
        this.endereco = new Endereco(dados.endereco());
        this.cpf = dados.cpf();
        this.dataNasc = dados.dataNasc();
        this.sexo = dados.sexo();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.nomeUser = dados.nomeUser();
    }


    public void alterarDados(DadosAlterarDadosUser dados, String novoHash){

        this.nome = dados.nome();
        this.endereco = new Endereco(dados.endereco());
        this.cpf = dados.cpf();
        this.dataNasc = dados.dataNasc();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.nomeUser = dados.nomeUser();
        if(dados.novaSenha() != null){
            this.senha = novoHash;
        }
    }

    public String getSubistituir() {
        return subistituir;
    }

    public void setSubistituir(String subistituir) {
        this.subistituir = subistituir;
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

    public Endereco getEndereco() {
        return endereco;
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

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getTokenRedefinicao() { 
        return tokenRedefinicao; 
    
    }
    public void setTokenRedefinicao(String tokenRedefinicao) { 
        this.tokenRedefinicao = tokenRedefinicao; 
    }

    public LocalDateTime getTokenExpiracao() { 
        return tokenExpiracao; 
    
    }
    public void setTokenExpiracao(LocalDateTime tokenExpiracao) { 
        this.tokenExpiracao = tokenExpiracao; 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void excluir() {
        this.ativo = false;
    }
}
