window.onload = function() {

  criarCarrinho();

  const salvarCarrinho = localStorage.getItem('carrinho');
  if (salvarCarrinho) {
    atualizarCarrinho();
  }
};

function criarCarrinho() {
  const carrinhoDiv = document.createElement('div');
  carrinhoDiv.id = 'carrinho';
  carrinhoDiv.innerHTML = `
    <h3>Itens no Carrinho</h3>
    <ul id="itens-carrinho"></ul>
    <p><strong>Total: </strong> R$ <span id="valor-total">0.00</span></p>
    <button onclick="limparCarrinho()">Limpar Carrinho</button>
  `;
  
  document.body.appendChild(carrinhoDiv); 
}

function adicionarProduto(nome, preco, id, imagem, imagem_tipo) {
  const carrinho = JSON.parse(localStorage.getItem('carrinho')) || [];

  const produto = { 
    nome, 
    preco, 
    id, 
    imagem, 
    imagem_tipo
  };

  carrinho.push(produto);

  localStorage.setItem('carrinho', JSON.stringify(carrinho));

  atualizarCarrinho();
}

async function atualizarCarrinho() {
  const itensCarrinho = document.getElementById('itens-carrinho');
  const valorTotal = document.getElementById('valor-total');
  
  if (!itensCarrinho || !valorTotal) {
    console.error("Erro: Elementos do carrinho não foram encontrados");
    return;
  }

  itensCarrinho.innerHTML = ''; 

  let total = 0;
  const carrinho = JSON.parse(localStorage.getItem('carrinho')) || [];

  for (let produto of carrinho) {
    const card = document.createElement("div");
    card.classList.add("produto-card");

    let imgSrc = `data:${produto.imagem_tipo};base64,${produto.imagem}` // Usar imagem padrão caso falte

    card.innerHTML = `
      <h2>${produto.nome}</h2>
      <img src="${imgSrc}" alt="${produto.nome}" width="200">
      <p><strong>Preço:</strong> R$ ${produto.preco.toFixed(2)}</p>
      <button onclick="removerProduto(${produto.id})">Remover do Carrinho</button>
    `;

    itensCarrinho.appendChild(card);
    total += produto.preco;
  }

  valorTotal.textContent = ` ${total.toFixed(2)}`;
}

function removerProduto(id) {
  let carrinho = JSON.parse(localStorage.getItem('carrinho')) || [];
  carrinho = carrinho.filter(produto => produto.id !== id);

  localStorage.setItem('carrinho', JSON.stringify(carrinho));

  atualizarCarrinho();
}

function limparCarrinho() {
  localStorage.removeItem('carrinho');
  atualizarCarrinho();
}
