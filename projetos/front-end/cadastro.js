


const txtName = document.getElementById("txtName");
const txtCPF = document.getElementById("txtCPF");
const dataNasc = document.getElementById("txtNasc");
const txtEmail = document.getElementById("txtEmail");
const end_pais = document.getElementById("end_pais");
const end_estado = document.getElementById("end_estado");
const end_cidade = document.getElementById("end_cidade");
const txtTel = document.getElementById("txtTel");
const end_Logradouro = document.getElementById("end_logradouro");
const NomeUser = document.getElementById("txtNU");
const txtSenha = document.getElementById("txtSenha");
const txtSenhaConfirmar = document.getElementById("txtSenhaConfirmar");

function btnSendOnClick() {
  if (txtName.value === "") {
    alert("Preenchimento obrigatório: Nome");
    txtName.focus();
    return false;
  }
  if (txtCPF.value === "") {
    alert("Preenchimento obrigatório: CPF");
    txtCPF.focus();
    return false;
  }
  if (dataNasc.value === "") {
    alert("Preenchimento obrigatório: Data de nascimento");
    dataNasc.focus();
    return false;
  }
  if (!document.querySelector('input[name="optGender"]:checked')) {
    alert("Preenchimento obrigatório: Sexo");
    return false;
  }
  if (txtEmail.value === "") {
    alert("Preenchimento obrigatório: E-mail");
    txtEmail.focus();
    return false;
  }
  if (end_pais.value === "") {
    alert("Preenchimento obrigatório: País");
    end_pais.focus();
    return false;
  }
  if (end_estado.value === "0") {
    alert("Preenchimento obrigatório: Estado");
    end_estado.focus();
    return false;
  }
  if (end_cidade.value === "") {
    alert("Preenchimento obrigatório: Cidade");
    end_cidade.focus();
    return false;
  }
  if (txtTel.value === "") {
    alert("Preenchimento obrigatório: Telefone");
    txtTel.focus();
    return false;
  }
  if (NomeUser.value === "") {
    alert("Preenchimento obrigatório: Nome de usuário");
    NomeUser.focus();
    return false;
  }
  if (txtSenha.value === "") {
    alert("Preenchimento obrigatório: Senha");
    txtSenha.focus();
    return false;
  }
  if(txtSenha.value !== txtSenhaConfirmar.value){
    alert("Senha diferente em confirmar senha");
    txtSenhaConfirmar.focus();
    return false;
  }
  if (!isCPF(txtCPF)) {
    alert("O CPF está incorreto, por favor insira um CPF válido.");
    txtCPF.focus();
    return false;
  }
  if (!isEmail(txtEmail)) {
    alert("O e-mail está incorreto, por favor insira um e-mail válido.");
    txtEmail.focus();
    return false;
  }
  if (!isdata(txtNasc)) {
    alert("A data de nascimento está incorreta, por favor insira uma data de nascimento válida.");
    txtNasc.focus();
    return false;
  }
  return true;
}
function isCPF(txtcpf) {
    const cpf = txtcpf.value;
    const re = /^\d{11}$/;
    return re.test(cpf)
  
}

function isEmail(txtemail) {
    const email =  txtemail.value;
  const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return re.test(email);
}
function isdata(txtNasc){
  const data  =  dataNasc.value;
  const re = /^(19|20)\d{2}[-/.](0[1-9]|1[0-2])[-/.](0[1-9]|[12]\d|3[01])$/;
  return re.test(data);
}
document.getElementById("form_cadastro").addEventListener("submit", async function(event)  {
    event.preventDefault();
    const sexo = document.querySelector('input[name="optGender"]:checked');

    if (!sexo) {  // Verifica se nenhum sexo foi selecionado
        alert("Preenchimento obrigatório: Sexo");
        return;
    }
    const usuario = {
        nome: txtName.value,
        cpf: txtCPF.value,
        dataNasc :  dataNasc.value,
        sexo: sexo.value,
        endereco: {  
            
            pais: end_pais.value,
            estado: end_estado.value,
            cidade: end_cidade.value,
            logradouro: end_Logradouro.value
        },
        email:txtEmail.value,
        telefone: txtTel.value,
        nomeUser:  NomeUser.value,
        senha: txtSenha.value
    };

    const response = await fetch("http://localhost:8080/auth/cadastro", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(usuario)

    });

    if (response.ok) {
        alert("Usuário cadastrado com sucesso!");
        window.location.href = "/projetos/front-end/login.html"; 
    } else {
        alert("Erro no cadastro.");
    }
});
