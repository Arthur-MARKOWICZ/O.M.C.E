let carrinho = [];

window.onload = function() {

  criarCarrinho();

  const salvarCarrinho = localStorage.getItem('Carrinho');
  if (salvarCarrinho) {
    carrinho = JSON.parse(salvarCarrinho);
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
  
  document.body.appendChild(carrinhoDiv);  // Pode ser ajustado para outro local
}

function adicionarProduto(nome, preco, id) {
  carrinho.push({ nome: nome, preco: preco, id: id });
  salvarCarrinho();
  atualizarCarrinho();
}

function atualizarCarrinho() {
  const itensCarrinho = document.getElementById('itens-carrinho');
  const valorTotal = document.getElementById('valor-total');
  
  if (!itensCarrinho || !valorTotal) {
    console.error("Erro: Elementos do carrinho nao foram  encontrados");
    return;
  }

  itensCarrinho.innerHTML = ''; 

  let total = 0;
  carrinho.forEach(item => {
    const li = document.createElement('li');
    li.textContent = `${item.nome} - R$ ${item.preco.toFixed(2)}`;
    itensCarrinho.appendChild(li);
    total += item.preco;
  });

  valorTotal.textContent = `R$ ${total.toFixed(2)}`;
}

function limparCarrinho() {
  carrinho = [];
  localStorage.removeItem('Carrinho');
  atualizarCarrinho();
}

function salvarCarrinho() {
  localStorage.setItem('Carrinho', JSON.stringify(carrinho));
}
