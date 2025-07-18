const txtName = document.getElementById("txtName");
const txtCPF = document.getElementById("txtCPF");
const dataNasc = document.getElementById("txtNasc");
const txtEmail = document.getElementById("txtEmail");
const txtCep = document.getElementById("end_cep");
const end_estado = document.getElementById("end_estado");
const end_cidade = document.getElementById("end_cidade");
const txtTel = document.getElementById("txtTel");
const end_logradouro = document.getElementById("end_logradouro");
const NomeUser = document.getElementById("txtNU");
const txtSenha = document.getElementById("txtSenha");
const txtSenhaConfirmar = document.getElementById("txtSenhaConfirmar");
console.log(dataNasc.value);

document.getElementById("form_cadastro").addEventListener("submit", async function(event) {
    event.preventDefault(); 
    console.log(dataNasc.value);
    if (!validarCadastro()) {
      return false;
  }
    
    const sexo = document.querySelector('input[name="optGender"]:checked');
    
    const usuario = {
        nome: txtName.value,
        cpf: txtCPF.value,
        dataNasc: dataNasc.value,
        sexo: sexo.value,
        endereco: {
            cep: end_cep.value,
            pais: "Brasil",
            estado: end_estado.value,
            cidade: end_cidade.value,
            logradouro: end_logradouro.value
        },
        email: txtEmail.value,
        telefone: txtTel.value,
        nomeUser: NomeUser.value,
        senha: txtSenha.value
    };

        const response = await fetch("http://localhost:8080/auth/cadastro", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(usuario)
        });
        const text = await response.text();
        let json;
        try {
          json = text ? JSON.parse(text) : {};
        } catch {
          json = {};
        }
       if(!response.ok) {

            mostrarErro(json.mensagem || "Erro desconhecido");
          return;
        }
        Swal.fire({
              title:"Seu cadastro foi concluído com sucesso!",
              text: "Seja bem-vindo ao O.M.C.E",
              icon: 'success'});
            event.target.reset();
            window.location.href = "../html/login.html";
    
});

function validarCadastro() {
    if (!txtName.value){
      return exibirErroEmBranco("Nome", txtName);
    } 
    if (!txtCPF.value || !isCPF(txtCPF.value)){
      return exibirErro("CPF", txtCPF);
    }
    if (!dataNasc.value || !isData(dataNasc.value)){
      return exibirErro("Data de nascimento", dataNasc);
         
    }
    if(!isDataValida(dataNasc.value)){
      return exibirErro("Data de nascimento inválida",dataNasc)
    }
    if (!document.querySelector('input[name="optGender"]:checked')) {
      return exibirErro("Preenchimento obrigatório: Sexo");
    }
    if (!txtEmail.value || !isEmail(txtEmail.value)){
      return exibirErro("E-mail", txtEmail);
    }

    if (end_estado.value === "0") {
      return exibirErro("CEP", end_estado);
    }
    if (!end_cidade.value){
      return exibirErro("CEP", end_cidade);
    }
    if (!txtTel.value || !/^\d+$/.test(txtTel.value)) {
      return exibirErro("Telefone (somente números)", txtTel);
    }
    if (!NomeUser.value){
      return exibirErro("Nome de usuário", NomeUser);
    }
    if (!txtSenha.value ) {
      return exibirErro("Senha", txtSenha);
    }
    if( !isSenhaForte(txtSenha.value)){
      return exibirErro("Senha fraca, a senha precisa ser maior que 8 caracteres, letras maiúsculas e minúsculas, número e caracteres especiais")
    }
    if (txtSenha.value !== txtSenhaConfirmar.value){
      return exibirErro("Senha diferente em confirmar senha", txtSenhaConfirmar);
    }

    return true;
}


function exibirErro(mensagem, campo) {
    Swal.fire({
      title: "Não foi possível realizar seu cadastro",
      text:"Erro no/a: " + mensagem,
      icon:'warning',
      confirmButtonText: 'OK'});("Erro no/a: " + mensagem);
    campo.focus();
    return false;


}   


function exibirErroEmBranco(mensagem, campo) {
    Swal.fire({
        title: "Não foi possível realizar seu cadastro",
        text:"Preenchimento obrigatório: "+ mensagem,
        icon:'warning',
        confirmButtonText: 'OK'});("Erro no/a: " + mensagem);
       campo.focus();
       return false;
    }
  function isSenhaForte(senha) {
  const minLength = 8;
  const hasUpperCase = /[A-Z]/.test(senha);
  const hasLowerCase = /[a-z]/.test(senha);
  const hasNumber = /[0-9]/.test(senha);
  const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(senha);

  if (senha.length < minLength) {
    return false;
  }
  
  if (!hasUpperCase || !hasLowerCase || !hasNumber || !hasSpecialChar) {
    return false;
  }

  return true;
}


function isCPF(cpf) {
  const re = /^\d{11}$/;
  if (!re.test(cpf)) return false;
  let soma = 0;
  for (let i = 0; i < 9; i++){
    soma += parseInt(cpf.charAt(i)) * (10 - i);
    }
  let primeiroDigitoVerificador = 11 - (soma % 11);
  if (primeiroDigitoVerificador >= 10) primeiroDigitoVerificador = 0;
  if (primeiroDigitoVerificador !== parseInt(cpf.charAt(9))) return false;
  soma = 0;
  for (let i = 0; i < 10; i++){
    soma += parseInt(cpf.charAt(i)) * (11 - i);
    }
  let segundoDigitoVerificador = 11 - (soma % 11);
  if (segundoDigitoVerificador >= 10) segundoDigitoVerificador = 0;
  if (segundoDigitoVerificador !== parseInt(cpf.charAt(10))) return false; 
  return true
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
function isEmail(email) {
    const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return re.test(email);
}

function isData(data) {
  const re = /^\d{4}-\d{2}-\d{2}$/;
  return re.test(data);
  
}
function isDataValida(dataNasc) {
  const hoje = new Date();
  const data = new Date(dataNasc);

  return data <= hoje;
}
function isCep(cep) {
  const re = /^\d{5}\d{3}$/;
  return re.test(cep);
}
function mostrarDados(dados) {
  if (dados.erro) {
    Swal.fire("CEP não encontrado!");
    return false;
  }
  end_logradouro.value = dados.logradouro;
  end_estado.value = dados.uf;
  end_cidade.value = dados.localidade;
}

txtCep.addEventListener("blur", (e)=>{
let buscaCep = txtCep.value.replace("-", "");
const opcoes={
  method: 'GET',
  mode: 'cors',
  cache: 'default'
}
fetch(`https://viacep.com.br/ws/${buscaCep}/json/`, opcoes)
.then(response => response.json())
.then(dados => mostrarDados(dados))
.catch(erro => console.log("Erro ao buscar o CEP: " + erro.message));
})

txtTel.addEventListener("input", function () {
  this.value = this.value.replace(/\D/g, "");
});