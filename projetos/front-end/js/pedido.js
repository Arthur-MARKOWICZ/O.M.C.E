async function pedido(){

  const carrinho = JSON.parse(localStorage.getItem('carrinho_pedido')) || [];
  if (carrinho.length === 0) {
    alert("Nenhum item no carrinho!");
    window.location.href = '../html/carrinho.html';
    return;
  }

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
      alert("Pedido realizado com sucesso!");

      localStorage.removeItem('carrinho_pedido');
      window.location.href = '../html/carrinho.html';
    } else {
      alert("Erro ao realizar pedido!");
    }
  } catch(e) {
    console.error("Erro:", e);
    alert("Erro ao realizar pedido!");
  }
}

document.getElementById('form_pedido').addEventListener('submit', function(e) {
  e.preventDefault();
  pedido();
});