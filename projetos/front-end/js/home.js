let numeroPaginaAtual = 0;

async function carregarProdutosHome() {
    try {
        const token = localStorage.getItem("jwt");

        const resposta = await fetch(`http://localhost:8080/produto/filtro?page=${numeroPaginaAtual}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });

        const dados = await resposta.json();
        let produtos = dados.content;


        if (produtos.length > 3) {
            produtos = produtos.slice(-3);
        }

        const container = document.getElementById("produtos-container");
        container.innerHTML = "";

        produtos.forEach(p => {
            const card = document.createElement("div");
            card.classList.add("produto-card");

            card.innerHTML = `
        <div class="imagem-container">
            <img src="data:${p.imagem_tipo};base64,${p.imagem}" alt="${p.nome}" width="200">
            <div class="nome-sobreposto">${p.nome}</div>
            </div>
                <p><strong>Preço:</strong> R$ ${p.preco.toFixed(2)}</p>
                <p><strong>Condicao:</strong> ${p.condicao}</p>
                <p><strong>Vendedor:</strong> ${p.nomeUsuario}</p>
        `;

            card.addEventListener("click", (e) => {
                if (e.target.tagName !== "BUTTON") {
                    window.location.href = `../html/visualizarProduto.html?id=${p.id}`;
                }
            });

            container.appendChild(card);
        });
    } catch (erro) {
        console.error("Erro ao carregar os produtos:", erro);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    carregarProdutosHome();
});
