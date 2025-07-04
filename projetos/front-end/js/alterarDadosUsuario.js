const txtName = document.getElementById("txtName");

const txtCPF = document.getElementById("txtCPF");
const dataNasc = document.getElementById("txtNasc");
const txtEmail = document.getElementById("txtEmail");
const end_estado = document.getElementById("end_estado");
const end_cidade = document.getElementById("end_cidade");
const txtTel = document.getElementById("txtTel");
const end_Logradouro = document.getElementById("end_logradouro");
const NomeUser = document.getElementById("txtNU");
const txtSenha = document.getElementById("txtSenha");
const cep = document.getElementById("txtCep");
const novaSenha = document.getElementById("txtNovaSenha");
const txtSenhaConfirmar = document.getElementById("txtSenhaConfirmar");

const userId = localStorage.getItem("id_usuario");
const token = localStorage.getItem("jwt");
console.log(token);
document.getElementById("form_cadastro").addEventListener("submit", async function(event) {
    event.preventDefault(); 
    if (!validarCadastro()) return false;
    const userId = localStorage.getItem("id_usuario");
    const json = {
        id: parseInt(userId),

        nome: txtName.value,
        cpf: txtCPF.value,
        dataNasc: dataNasc.value,
        endereco: {
            cep: cep.value,
            pais: "Brasil",
            estado: end_estado.value,
            cidade: end_cidade.value,
            logradouro: end_Logradouro.value
        },
        email: txtEmail.value,
        telefone: txtTel.value,
        nomeUser: NomeUser.value,
        senha: txtSenha.value,
        novaSenha: novaSenha.value
    };
    
    try {
        const response = await fetch(`http://localhost:8080/user/alterardados`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(json)
        });
        console.log(json);
        if (response.ok) {
            Swal.fire({
                title:"Seus dados foram atualizados com sucesso!",
                icon:'success'});
            event.target.reset();
            window.location.href = "../html/home.html";
        } else {
            Swal.fire("Erro ao atualizar os dados.");
        }
    } catch (error) {
        Swal.fire({
            title:"Algo deu errado:(",
            text:"Erro de conexão com o servidor",
            icon:'warning'});
    }
});

function validarCadastro() {
    if (!txtName.value) return exibirErro("Nome", txtName);
    if (!txtCPF.value || !isCPF(txtCPF.value)) return exibirErro("CPF", txtCPF);
    if (!dataNasc.value || !isData(dataNasc.value)) return exibirErro("Data de nascimento", dataNasc);
    if (!txtEmail.value || !isEmail(txtEmail.value)) return exibirErro("E-mail", txtEmail);
    if (end_estado.value === "0") return exibirErro("CEP", end_estado);
    if (!end_cidade.value) return exibirErro("CEP", end_cidade);
    if (!txtTel.value) return exibirErro("Telefone (somente números)", txtTel);
    if (!NomeUser.value) return exibirErro("Nome de usuário", NomeUser);
    if (!txtSenha.value) return exibirErro("Senha", txtSenha);
    if (novaSenha.value !== txtSenhaConfirmar.value) return exibirErro("Senhas diferentes", txtSenhaConfirmar);

    return true;
}

function exibirErro(mensagem, campo) {
    Swal.fire({
      title: "Não foi possível realizar seu cadastro",
      text:"Erro no/a: " + mensagem,
      icon:'warning',
      confirmButtonText: 'OK'});("Erro no/a: " + mensagem);
    if (campo) campo.focus();
    return false;
}

function isCPF(cpf) {
    return /^\d{11}$/.test(cpf);
}

function isEmail(email) {
    return /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email);
}

function isData(data) {
    return /^\d{4}-\d{2}-\d{2}$/.test(data);
}

function isCep(cep) {
    return /^\d{5}-\d{3}$/.test(cep);
}

function mostrarDados(dados) {
  if (dados.erro) {
    Swal.fire("CEP não encontrado!");
    return false;
  }
  end_logradouro.value = dados.logradouro;
  end_estado.value = dados.uf;
  end_cidade.value = dados.localidade;

  end_logradouro.disabled = true;
  end_estado.disabled = true;
  end_cidade.disabled = true;
}
cep.addEventListener("blur", () => {
    const buscaCep = cep.value.replace("-", ""); 
    fetch(`https://viacep.com.br/ws/${buscaCep}/json/`)
        .then(response => response.json())
        .then(dados => mostrarDados(dados))
        .catch(erro => console.log("Erro ao buscar o CEP: " + erro.message));
});

document.addEventListener("DOMContentLoaded", () => {

    carregarDados();
});


async function carregarDados() {
    try {
        const response = await fetch(`http://localhost:8080/user/${userId}`,{
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
        });
        if (!response.ok) throw new Error("Usuário não encontrado");
        const dados = await response.json();
        console.log(dados); 
        const endereco = dados.endereco;
        console.log(endereco.cep);
        cep.value = endereco.cep;
        txtName.value = dados.nome;
        txtCPF.value = dados.cpf;
        dataNasc.value = dados.dataNasc;
        txtEmail.value = dados.email;
        txtTel.value = dados.telefone;
        NomeUser.value = dados.nomeUser;
        end_estado.value = endereco.estado;
        end_Logradouro.value = endereco.logradouro;
        end_cidade.value = endereco.cidade;





    } catch (error) {
        console.error("Erro ao carregar os dados do usuário:", error);
    }
}

txtTel.addEventListener("input", function () {
  this.value = this.value.replace(/\D/g, "");
});