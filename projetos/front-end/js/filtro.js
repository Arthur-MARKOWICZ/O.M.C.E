let Categoria = "ARDUINO";
let numeroPaginaAtual  = 0;
async function carregarFeed() {
  const resposta = await fetch(`http://localhost:8080/produto/produtosCategoria?categoria=${Categoria}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "categoria": Categoria
            },
        });
        const dados = await resposta.json();
        console.log(dados);
        const produtos  = dados.content;
        console.log(produtos);
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
                <p><strong>Detalhes:</strong> ${p.detalhes}</p>
                <p><strong>Condicao:</strong> ${p.condicao}</p>
                <p><strong>Vendedor:</strong> ${p.nomeUsuario}</p>
                <button onclick="adicionarProduto('${p.nome}', ${p.preco}, ${p.id},'${p.imagem}' ,'${p.imagem_tipo}' ,'${p.id_usuario}')">Adicionar ao Carrinho</button>
            `;

            card.addEventListener("click", (e) => {
                if (e.target.tagName !== "BUTTON") {
                    window.location.href = `../html/visualizarProduto.html?id=${p.id}`;
                }
            });

            container.appendChild(card);
        });
        botaoAnterior.disabled = numeroPaginaAtual === 0; 
        botaoProximo.disabled = numeroPaginaAtual === (numTotalPaginas-1); 

    }
    document.getElementById('anterior').addEventListener('click', () => {
    if (numeroPaginaAtual > 0) {
        numeroPaginaAtual--;
        carregarFeed();
    }
});

document.getElementById('proximo').addEventListener('click', () => {
    numeroPaginaAtual++;
    carregarFeed();
});
document.addEventListener("DOMContentLoaded", () => {
    carregarFeed();
});