let numeroPaginaAtual = 0;
let filtrosAtuais = {};

async function carregarFeed(filtros = {}) {
    const token = localStorage.getItem("jwt");
    const params = new URLSearchParams({
        page: numeroPaginaAtual,
        ...filtros
    });

    try {
        const resposta = await fetch(`http://localhost:8080/produto/filtro?${params.toString()}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            }
        });

        const dados = await resposta.json();
        const produtos = dados.content;
        const numTotalPaginas = dados.totalPages;
        const container = document.getElementById("feed-container");
        const paginacaoDiv = document.getElementById('numero-pagina');
        const botaoAnterior = document.getElementById('anterior');
        const botaoProximo = document.getElementById('proximo');

        container.innerHTML = "";
        paginacaoDiv.innerText = `Página ${numeroPaginaAtual + 1} de ${numTotalPaginas}`;

        produtos.forEach(p => {
            const card = document.createElement("div");
            card.classList.add("produto-card");

            card.innerHTML = `
                <h2>${p.nome}</h2>
                <img src="data:${p.imagem_tipo};base64,${p.imagem}" alt="${p.nome}" width="200">
                <p><strong>Preço:</strong> R$ ${p.preco.toFixed(2)}</p>
                <p><strong>Condicao:</strong> ${p.condicao}</p>
                <p><strong>Vendedor:</strong> ${p.nomeUsuario}</p>
                <button onclick="adicionarProduto('${p.nome}', ${p.preco}, ${p.id}, '${p.imagem}', '${p.imagem_tipo}', '${p.id_usuario}')">Adicionar ao Carrinho</button>
            `;

            card.addEventListener("click", (e) => {
                if (e.target.tagName !== "BUTTON") {
                    window.location.href = `../html/visualizarProduto.html?id=${p.id}`;
                }
            });

            container.appendChild(card);
        });

        botaoAnterior.disabled = numeroPaginaAtual === 0;
        botaoProximo.disabled = numeroPaginaAtual === (numTotalPaginas - 1);

    } catch (erro) {
        console.error("Erro ao carregar o feed:", erro);
        Swal.fire({
            title:"Erro ao carregar o feed",
            icon:'info'});
    }
}

document.getElementById('anterior').addEventListener('click', () => {
    if (numeroPaginaAtual > 0) {
        numeroPaginaAtual--;
        carregarFeed(filtrosAtuais);
    }
});

document.getElementById('proximo').addEventListener('click', () => {
    numeroPaginaAtual++;
    carregarFeed(filtrosAtuais);
});

document.getElementById('form-filtro').addEventListener('submit', (e) => {
    e.preventDefault();
    numeroPaginaAtual = 0;

    const nome = document.getElementById('filtro-nome').value;
    const categoria = document.getElementById('filtro-categoria').value;
    const precoMin = document.getElementById('filtro-preco-min').value;
    const precoMax = document.getElementById('filtro-preco-max').value;

    filtrosAtuais = {};
    if (nome) filtrosAtuais.nome = nome;
    if (categoria) filtrosAtuais.categoria = categoria;
    if (precoMin) filtrosAtuais.precoMin = precoMin;
    if (precoMax) filtrosAtuais.precoMax = precoMax;

    carregarFeed(filtrosAtuais);
});

document.addEventListener("DOMContentLoaded", () => {
    carregarFeed(); 
});
