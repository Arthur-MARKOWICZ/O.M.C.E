const API_BASE = 'http://localhost:8080';

window.onload = function () {
  const usuarioId = getUsuarioId();
  if (!usuarioId) return;

  if (window.location.pathname.includes("carrinho.html")) {
    criarCarrinho();
    atualizarCarrinho();
  }
};

function getUsuarioId() {
  const id = localStorage.getItem("id_usuario");
  if (!id) {
    Swal.fire({
      text: "Você precisa estar logado para acessar o carrinho.",
      icon: 'warning'
    });
    window.location.href = "../html/login.html";
    return null;
  }
  return id;
}


async function getCarrinhoBackend() {
  const usuarioId = getUsuarioId();
  try {
    const response = await fetch(`${API_BASE}/carrinho/${usuarioId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwt')}`
      }
    });
    
    if (response.ok) {
      return await response.json();
    }
    return [];
  } catch (error) {
    console.error("Erro ao buscar carrinho:", error);
    return [];
  }
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
  
  const carrinho = await getCarrinhoBackend();

  for (let produto of carrinho) {
    const card = document.createElement("div");
    card.classList.add("produto-card");

    let imgSrc = `data:${produto.imageTipo};base64,${btoa(String.fromCharCode(...new Uint8Array(produto.imagem)))}`;

    card.innerHTML = `
      <h2>${produto.nome}</h2>
      <img src="${imgSrc}" alt="${produto.nome}" width="200">
      <p><strong>Preço:</strong> ${produto.preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}</p>
      <button>Remover do Carrinho</button>
    `;

    const btn = card.querySelector("button");
    btn.addEventListener("click", (e) => {
      e.stopPropagation();
      removerProdutoBackend(produto.id);
    });

    card.addEventListener("click", () => {
      window.location.href = `../html/visualizarProduto.html?id=${produto.id}`;
    });

    itensCarrinho.appendChild(card);
    total += produto.preco;
  }

  valorTotal.textContent = `${total.toFixed(2)}`;
}

async function adicionarProdutoBackend(produtoId) {
  const usuarioId = getUsuarioId();
  if (!usuarioId) return;

  try {
    const response = await fetch(`${API_BASE}/carrinho/${usuarioId}/adicionar/${produtoId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('jwt')}`
      }
    });

    if (response.ok) {
      Swal.fire({ 
        title: "Produto adicionado ao carrinho!", 
        icon: 'success' 
      });
      if (window.location.pathname.includes("carrinho.html")) {
        atualizarCarrinho();
      }
    } else {
      Swal.fire({ title: "Erro ao adicionar produto", icon: 'error' });
    }
  } catch (error) {
    Swal.fire({ title: "Erro de conexão", text: "Tente novamente", icon: 'error' });
  }
}

async function removerProdutoBackend(produtoId) {
  const usuarioId = getUsuarioId();
  try {
    const response = await fetch(`${API_BASE}/carrinho/${usuarioId}/remover/${produtoId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwt')}`
      }
    });

    if (response.ok) {
      Swal.fire({ title: "Produto removido!", icon: 'success' });
      atualizarCarrinho();
    }
  } catch (error) {
    Swal.fire({ title: "Erro ao remover produto", icon: 'error' });
  }
}

async function limparCarrinho() {
  const usuarioId = getUsuarioId();
  try {
    const response = await fetch(`${API_BASE}/carrinho/${usuarioId}/limpar`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwt')}`
      }
    });

    if (response.ok) {
      Swal.fire({ title: "Carrinho limpo!", icon: 'success' });
      atualizarCarrinho();
    }
  } catch (error) {
    Swal.fire({ title: "Erro ao limpar carrinho", icon: 'error' });
  }
}

async function finalizarCompra() {
  const usuarioId = getUsuarioId();
  try {
    const response = await fetch(`${API_BASE}/carrinho/${usuarioId}/finalizar`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwt')}`
      }
    });

    if (response.ok) {
      Swal.fire({ 
        title: "Compra finalizada com sucesso!", 
        icon: 'success' 
      });
      window.location.href = '../html/pedido.html';
    }
  } catch (error) {
    Swal.fire({ title: "Erro ao finalizar compra", icon: 'error' });
  }
}

function criarCarrinho() {
  const carrinhoDiv = document.createElement('div');
  carrinhoDiv.id = 'carrinho';
  carrinhoDiv.innerHTML = `
    <h3>Itens no Carrinho</h3>
    <ul id="itens-carrinho"></ul>
    <p><strong>Total: R$ </strong><span id="valor-total">0.00</span></p>
    <button onclick="limparCarrinho()">Limpar Carrinho</button>
    <button onclick="finalizarCompra()">Finalizar Compra</button>
    <button onclick="logout()">Sair</button>
  `;
  document.body.appendChild(carrinhoDiv);
}

function logout() {
  localStorage.removeItem("id_usuario");
  localStorage.removeItem("jwt");
  window.location.href = "../html/login.html";
}