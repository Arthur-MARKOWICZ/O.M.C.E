window.onload = function () {
  const usuarioId = getUsuarioId();
  if (!usuarioId) return;

  if (window.location.pathname.includes("carrinho.html")) {
    criarCarrinho();
    const salvarCarrinho = localStorage.getItem(`carrinho_${usuarioId}`);
    if (salvarCarrinho) {
      atualizarCarrinho();
    }
  }
};

function getUsuarioId() {
  const id = localStorage.getItem("id_usuario");
  if (!id) {
   Swal.fire({
    text: "Você precisa estar logado para acessar o carrinho.",
    icon: 'warning'
   })
    window.location.href = "../html/login.html";
    return null;
  }
  return id;
}

function getCarrinho() {
  const id = getUsuarioId();
  return JSON.parse(localStorage.getItem(`carrinho_${id}`)) || [];
}

function salvarCarrinho(carrinho) {
  const id = getUsuarioId();
  localStorage.setItem(`carrinho_${id}`, JSON.stringify(carrinho));
}

function criarCarrinho() {
  const carrinhoDiv = document.createElement('div');
  carrinhoDiv.id = 'carrinho';
  carrinhoDiv.innerHTML = `
    <h3>Itens no Carrinho</h3>
    <ul id="itens-carrinho"></ul>
    <p><strong>Total: </strong> R$ <span id="valor-total">0.00</span></p>
    <button onclick="limparCarrinho()">Limpar Carrinho</button>
    <button onclick="finalizarCompra()">Finalizar Compra</button>
    <button onclick="logout()">Sair</button>
  `;
  document.body.appendChild(carrinhoDiv);
}

function adicionarProduto(nome, preco, id, imagem, imagem_tipo) {
  const carrinho = getCarrinho();
  const jaExiste = carrinho.some(produto => produto.id === id);

  if (jaExiste) {
    Swal.fire({
      title:"Este produto já está no seu carrinho.",
      icon:'info'});
    return;
  }

  const produto = { nome, preco, id, imagem, imagem_tipo };
  carrinho.push(produto);
  salvarCarrinho(carrinho);
  atualizarCarrinho();
}

function atualizarCarrinho() {
  const itensCarrinho = document.getElementById('itens-carrinho');
  const valorTotal = document.getElementById('valor-total');

  if (!itensCarrinho || !valorTotal) {
    console.error("Erro: Elementos do carrinho não foram encontrados");
    return;
  }

  itensCarrinho.innerHTML = '';
  let total = 0;
  const carrinho = getCarrinho();

  for (let produto of carrinho) {
    const card = document.createElement("div");
    card.classList.add("produto-card");

    let imgSrc = `data:${produto.imagem_tipo};base64,${produto.imagem}`;

    card.innerHTML = `
      <h2>${produto.nome}</h2>
      <img src="${imgSrc}" alt="${produto.nome}" width="200">
      <p><strong>Preço:</strong> R$ ${produto.preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}</p>
      <button>Remover do Carrinho</button>
    `;

    const btn = card.querySelector("button");
    btn.addEventListener("click", (e) => {
      e.stopPropagation();
      removerProduto(produto.id);
    });

    card.addEventListener("click", () => {
      window.location.href = `../html/visualizarProduto.html?id=${produto.id}`;
    });

    itensCarrinho.appendChild(card);
    total += produto.preco;
  }

  valorTotal.textContent = `${total.toFixed(2)}`;
}

function removerProduto(id) {
  let carrinho = getCarrinho();
  carrinho = carrinho.filter(produto => produto.id !== id);
  salvarCarrinho(carrinho);
  atualizarCarrinho();
}

function limparCarrinho() {
  const id = getUsuarioId();
  localStorage.removeItem(`carrinho_${id}`);
  atualizarCarrinho();
}

function logout() {
  localStorage.removeItem("id_usuario");
  localStorage.removeItem("jwt");
  window.location.href = "../html/login.html";
}

function finalizarCompra() {
  const carrinho = getCarrinho();
  if (carrinho.length === 0) {
   Swal.fire({
    text: "Seu carrinho está vazio!",
    icon: 'warning'
   })
    return;
  }
  
  localStorage.setItem('carrinho_pedido', JSON.stringify(carrinho));
  

  window.location.href = '../html/pedido.html';
}
