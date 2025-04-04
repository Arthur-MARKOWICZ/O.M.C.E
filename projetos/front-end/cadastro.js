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


  document.getElementById("form_cadastro").addEventListener("submit", async function(event) {
      event.preventDefault(); 
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
          const response = await fetch("http://localhost:8080/auth/cadastro", {
              method: "POST",
              headers: {
                  "Content-Type": "application/json"
              },
              body: JSON.stringify(usuario)
          });

          if (response.ok) {
              alert("Usuário cadastrado com sucesso!");
              event.target.reset();
              window.location.href = "/front-end/login.html";
          } else {
              alert("Erro no cadastro.");
          }
      } catch (error) {
          alert("Erro de conexão.");
      }
  });

  function validarCadastro() {
      if (!txtName.value){
        return exibirErro("Nome", txtName);
      } 
      if (!txtCPF.value || !isCPF(txtCPF.value)){
        return exibirErro("CPF", txtCPF);
      }
      if (!dataNasc.value || !isData(dataNasc.value)){
        return exibirErro("Data de nascimento", dataNasc);
      }
      if (!document.querySelector('input[name="optGender"]:checked')) {
        return exibirErro("Preenchimento obrigatório: Sexo");
      }
      if (!txtEmail.value || !isEmail(txtEmail.value)){
        return exibirErro("E-mail", txtEmail);
      }
      if (!end_pais.value) {
        return exibirErro("País", end_pais);
      }
      if (end_estado.value === "0") {
        return exibirErro("Estado", end_estado);
      }
      if (!end_cidade.value){
        return exibirErro("Cidade", end_cidade);
      }
      if (!txtTel.value) {
        return exibirErro("Telefone", txtTel);
      }
      if (!NomeUser.value){
        return exibirErro("Nome de usuário", NomeUser);
      }
      if (!txtSenha.value) {
        return exibirErro("Senha", txtSenha);
      }
      if (txtSenha.value !== txtSenhaConfirmar.value){
        return exibirErro("Senha diferente em confirmar senha", txtSenhaConfirmar);
      }

      return true;
  }

  function exibirErro(mensagem, campo) {
      alert("Erro no/a: " + mensagem);
      campo.focus();
      return false;
  }   

  function isCPF(cpf) {
    const re = /^\d{11}$/;
    return re.test(cpf);
  }


  function isEmail(email) {
      const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      return re.test(email);
  }

  function isData(data) {
    const re = /^\d{4}-\d{2}-\d{2}$/;
    return re.test(data);
  }

