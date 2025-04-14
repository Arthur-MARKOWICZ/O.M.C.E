async function carregarFeed() {
    try {
        const token = localStorage.getItem("jwt");

        const resposta = await fetch("http://localhost:8080/produto/todos", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });

        const produtos = await resposta.json();

        const container = document.getElementById("feed-container");
        console.log(produtos);
        produtos.forEach(p => {
            const card = document.createElement("div");
            card.classList.add("produto-card");

            card.innerHTML = `
                <h2>${p.nome}</h2>
                <img src="data:${p.imagem_tipo};base64,${p.imagem}" alt="${p.nome}" width="200">
                <p><strong>Preço:</strong> R$ ${p.preco.toFixed(2)}</p>
                <p><strong>Detalhes:</strong> ${p.detalhes}</p>
                <p><strong>Vendedor:</strong> ${p.nome_usuario}</p>
                <button onclick="adicionarProduto('${p.nome}', ${p.preco}, ${p.id},'${p.imagem}' ,'${p.imagem_tipo  }')">Adicionar ao Carrinho</button>
            `;

            container.appendChild(card);
        });

    } catch (erro) {
        console.error("Erro ao carregar o feed:", erro);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    carregarFeed();
});
