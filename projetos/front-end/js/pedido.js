function getUsuarioId() {
  const id = localStorage.getItem("id_usuario");
  if (!id) {
     Swal.fire("Você precisa estar logado para acessar o ir para o finalizar pedido.");
    window.location.href = "../html/login.html";
    return null;
  }
  return id;
}
async function pedido(){
  const id = getUsuarioId();
  const carrinho = JSON.parse(localStorage.getItem('carrinho_pedido')) || [];
  if (carrinho.length === 0) {
     Swal.fire("Nenhum item no carrinho!");
    window.location.href = '../html/carrinho.html';
    return;
  }
  document.querySelector("#form_pedido").addEventListener("submit", function (e) {
  document.getElementById("end_logradouro").disabled = false;
  document.getElementById("end_estado").disabled = false;
  document.getElementById("end_cidade").disabled = false;
});

  const id_produtos = carrinho.map(item => item.id);
  

  const valor = carrinho.reduce((total, item) => total + item.preco, 0);

  const json = {
    id_produtos: id_produtos,
    id_comprador: parseInt(localStorage.getItem('id_usuario')),
    endereco: {
      cep: document.getElementById('end_cep').value,
      pais: "Brasil",
      estado: document.getElementById('end_estado').value,
      cidade: document.getElementById('end_cidade').value,
      logradouro: document.getElementById('end_logradouro').value
    },
    valor: valor
  }

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
       Swal.fire("Pedido realizado com sucesso!");

      localStorage.removeItem(`carrinho_${id}`);
      window.location.href = '../html/carrinho.html';
    } else {
       Swal.fire("Erro ao realizar pedido!");
    }
  } catch(e) {
    console.error("Erro:", e);
     Swal.fire("Erro ao realizar pedido!");
  }
}

document.getElementById('form_pedido').addEventListener('submit', function(e) {
  e.preventDefault();
  pedido();
});
function isCep(cep) {
  const re = /^\d{5}\d{3}$/;
  return re.test(cep);
}
function mostrarDados(dados) {
  if (dados.erro) {
    alert("CEP não encontrado!");
    return false;
  }
  end_logradouro.value = dados.logradouro;
  end_estado.value = dados.uf;
  end_cidade.value = dados.localidade;
}

end_cep.addEventListener("blur", (e)=>{
let buscaCep = end_cep.value.replace("-", "");
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