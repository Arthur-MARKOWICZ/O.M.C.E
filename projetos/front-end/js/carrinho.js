window.onload = function () {
  if (window.location.pathname.includes("carrinho.html")) {
    criarCarrinho();
    const salvarCarrinho = localStorage.getItem('carrinho');
    if (salvarCarrinho) {
      atualizarCarrinho();
    }
  }
};

<<<<<<< Updated upstream
=======
function getUsuarioId() {
  const id = localStorage.getItem("id_usuario");
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

>>>>>>> Stashed changes
function criarCarrinho() {
  const carrinhoDiv = document.createElement('div');
  carrinhoDiv.id = 'carrinho';
  carrinhoDiv.innerHTML = `
    <h3>Itens no Carrinho</h3>
    <ul id="itens-carrinho"></ul>
    <p><strong>Total: </strong> R$ <span id="valor-total">0.00</span></p>
    <button class="limpar" onclick="limparCarrinho()">Limpar Carrinho</button>
    <br>
    <button class="irPagamento" onclick="irPagamento()">Ir para pagamento</button>
  `;
  
  document.body.appendChild(carrinhoDiv); 
}

function adicionarProduto(nome, preco, id, imagem, imagem_tipo) {
  const carrinho = JSON.parse(localStorage.getItem('carrinho')) || [];

  const jaExiste = carrinho.some(produto => produto.id === id);

  if (jaExiste) {
    Swal.fire({
      title:"Este produto já está no seu carrinho.",
      icon:'info'});
    return;
  }

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
    card.addEventListener("click", (e) => {
      if (e.target.tagName !== "BUTTON") {
          window.location.href = `../html/visualizarProduto.html?id=${produto.id}`;
      }
  });
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

function irPagamento() {
  window.location.href="pagamento.html"
}
