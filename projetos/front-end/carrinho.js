let carrinho = [];

window.onload = function() {
  const salvarCarrinho = localStorage.getItem('Carrinho');
  if (salvarCarrinho) {
    carrinho = JSON.parse(salvarCarrinho);
    atualizarCarrinho();
  }
};

function adicionarProduto(nome, preco, id) {
  carrinho.push({ nome: nome, preco: preco, id: id });
  salvarCarrinho();
  atualizarCarrinho();
}

function atualizarCarrinho() {
  const itensCarrinho = document.getElementById('');
  const valorTotal = document.getElementById('');
  itensCarrinho.innerHTML = '';

  let total = 0;
  carrinho.forEach(item => {
    const li = document.createElement('li');
    li.textContent = `${produto.nome} - R$ ${produto.preco.toFixed(2)}`;
    itensCarrinho.appendChild(li);
    total += produto.preco;
  });

  valorTotal.textContent = total.toFixed(2);
}

function limparCarrinho() {
  carrinho = [];
  localStorage.removeItem('');
  atualizarCarrinho();
}

function salvarCarrinho() {
  localStorage.setItem('', JSON.stringify(carrinho));
}
