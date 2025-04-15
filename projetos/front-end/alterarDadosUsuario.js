
const txtName = document.getElementById("txtName");
const txtCPF = document.getElementById("txtCPF");
const dataNasc = document.getElementById("txtNasc");
const txtEmail = document.getElementById("txtEmail");
const txtCep = document.getElementById("end_cep");
const end_pais = document.getElementById("end_pais");
const end_estado = document.getElementById("end_estado");
const end_cidade = document.getElementById("end_cidade");
const txtTel = document.getElementById("txtTel");
const end_Logradouro = document.getElementById("end_logradouro");
const NomeUser = document.getElementById("txtNU");
const txtSenha = document.getElementById("txtSenha");
const txtSenhaConfirmar = document.getElementById("txtSenhaConfirmar");

const userId = localStorage.getItem("id_usuario");
const token = localStorage.getItem("jwt");

document.getElementById("form_cadastro").addEventListener("submit", async function(event) {
    event.preventDefault(); 
    if (!validarCadastro()) return false;

    const sexo = document.querySelector('input[name="optGender"]:checked');

    const usuario = {
        id: userId,
        nome: txtName.value,
        cpf: txtCPF.value,
        dataNasc: dataNasc.value,
        sexo: sexo.value,
        endereco: {
            cep: txtCep.value,
            pais: end_pais.value,
            estado: end_estado.value,
            cidade: end_cidade.value,
            logradouro: end_Logradouro.value
        },
        email: txtEmail.value,
        telefone: txtTel.value,
        nomeUser: NomeUser.value,
        senha: txtSenha.value
    };

    try {
        const response = await fetch(`http://localhost:8080/user/${userId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(usuario)
        });

        if (response.ok) {
            alert("Seus dados foram atualizados com sucesso!");
            event.target.reset();
            window.location.href = "/front-end/index.html";
        } else {
            alert("Erro ao atualizar os dados.");
        }
    } catch (error) {
        alert("Erro de conexão.");
    }
});

function validarCadastro() {
    if (!txtName.value) return exibirErro("Nome", txtName);
    if (!txtCPF.value || !isCPF(txtCPF.value)) return exibirErro("CPF", txtCPF);
    if (!dataNasc.value || !isData(dataNasc.value)) return exibirErro("Data de nascimento", dataNasc);
    if (!document.querySelector('input[name="optGender"]:checked')) return exibirErro("Sexo");
    if (!txtEmail.value || !isEmail(txtEmail.value)) return exibirErro("E-mail", txtEmail);
    if (!end_pais.value) return exibirErro("País", end_pais);
    if (end_estado.value === "0") return exibirErro("Estado", end_estado);
    if (!end_cidade.value) return exibirErro("Cidade", end_cidade);
    if (!txtTel.value) return exibirErro("Telefone", txtTel);
    if (!NomeUser.value) return exibirErro("Nome de usuário", NomeUser);
    if (!txtSenha.value) return exibirErro("Senha", txtSenha);
    if (txtSenha.value !== txtSenhaConfirmar.value) return exibirErro("Senhas diferentes", txtSenhaConfirmar);

    return true;
}

function exibirErro(mensagem, campo) {
    alert("Erro no/a: " + mensagem);
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
        alert("CEP não encontrado!");
        return;
    }
    end_Logradouro.value = dados.logradouro;
    end_estado.value = dados.uf;
    end_cidade.value = dados.localidade;
}

txtCep.addEventListener("blur", () => {
    const buscaCep = txtCep.value.replace("-", "");
    fetch(`https://viacep.com.br/ws/${buscaCep}/json/`)
        .then(response => response.json())
        .then(dados => mostrarDados(dados))
        .catch(erro => console.log("Erro ao buscar o CEP: " + erro.message));
});

document.addEventListener("DOMContentLoaded", () => {
    carregarDados();
});

async function carregarDados() {
    console.log(userId);
    console.log(token);
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

        txtName.value = dados.nome;
        txtCPF.value = dados.cpf;
        dataNasc.value = dados.dataNasc;
        txtEmail.value = dados.email;
        txtTel.value = dados.telefone;
        NomeUser.value = dados.nomeUser;
        
        if (dados.sexo === "masculino") {
            document.getElementById("masculino").checked = true;
        } else if (dados.sexo === "feminino") {
            document.getElementById("feminino").checked = true;
        }
        txtCep.value = dados.endereco.cep;
        end_Logradouro.value = dados.endereco.logradouro;
        end_estado.value = dados.endereco.estado;
        end_cidade.value = dados.endereco.cidade;
        end_pais.value = dados.endereco.pais;


    } catch (error) {
        console.error("Erro ao carregar os dados do usuário:", error);
        alert("Não foi possível carregar os dados do usuário.");
    }
}
