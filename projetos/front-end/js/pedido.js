function getUsuarioId() {
  const id = localStorage.getItem("id_usuario");
  if (!id) {
    Swal.fire({
      text: "Você precisa estar logado para acessar o ir para o finalizar pedido.",
      icon: "warning"
    });
    window.location.href = "../html/login.html";
    return null;
  }
  return id;
}

function isCep(cep) {
  const re = /^\d{5}\d{3}$/;
  return re.test(cep);
}

function mostrarDados(dados) {
  if (dados.erro) {
    Swal.fire({
      text: "CEP não encontrado!",
      icon: 'warning'
    });
    return false;
  }
  end_logradouro.value = dados.logradouro;
  end_estado.value = dados.uf;
  end_cidade.value = dados.localidade;
}

end_cep.addEventListener("blur", () => {
  let buscaCep = end_cep.value.replace("-", "");
  const opcoes = {
    method: 'GET',
    mode: 'cors',
    cache: 'default'
  };
  fetch(`https://viacep.com.br/ws/${buscaCep}/json/`, opcoes)
    .then(response => response.json())
    .then(dados => mostrarDados(dados))
    .catch(erro => console.log("Erro ao buscar o CEP: " + erro.message));
});

function exibirErro(mensagem, campo) {
  Swal.fire({
    title: "Não foi possível realizar seu pedido",
    text: "Verifique seu CEP",
    icon: 'warning',
    confirmButtonText: 'OK'
  });
  campo.focus();
  return false;
}

function validarPedido() {
  if (!end_estado.value || end_estado.value === "0") {
    return exibirErro("Estado", end_estado);
  }
  if (!end_cidade.value) {
    return exibirErro("Cidade", end_cidade);
  }
  return true;
}

async function pedido() {
  const id = getUsuarioId();
  if (!id) return;

  const carrinho = JSON.parse(localStorage.getItem('carrinho_pedido')) || [];
  if (carrinho.length === 0) {
    Swal.fire({
      text: "Nenhum item no carrinho!",
      icon: 'warning'
    });
    window.location.href = '../html/carrinho.html';
    return;
  }

  if (!validarPedido()) return;

  const id_produtos = carrinho.map(item => item.id);
  const valor = carrinho.reduce((total, item) => total + item.preco, 0);

  const json = {
    id_produtos: id_produtos,
    id_comprador: parseInt(id),
    endereco: {
      cep: document.getElementById('end_cep').value,
      pais: "Brasil",
      estado: document.getElementById('end_estado').value,
      cidade: document.getElementById('end_cidade').value,
      logradouro: document.getElementById('end_logradouro').value
    },
    valor: valor
  };

  try {
    const token = localStorage.getItem('jwt');
    const response = await fetch(`http://localhost:8080/pedido/cadastro`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(json)
    });

    if (response.ok) {
      Swal.fire({
        text: "Pedido realizado com sucesso!",
        icon: 'success'
      });

      localStorage.removeItem(`carrinho_${id}`);
      window.location.href = '../html/carrinho.html';
    } else {
      Swal.fire({
        text: "Erro ao realizar pedido!",
        icon: 'warning'
      });
    }
  } catch (e) {
    console.error("Erro:", e);
    Swal.fire({
      text: "Erro ao realizar pedido!",
      icon: 'warning'
    });
  }
}

document.getElementById('form_pedido').addEventListener('submit', function (e) {
  e.preventDefault();
  pedido();
});
