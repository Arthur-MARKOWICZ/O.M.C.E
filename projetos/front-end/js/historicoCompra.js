
let numeroPaginaAtual = 0;
const nomeProduto = document.getElementById("produto-nome");

async function carregarProdutos() {
    try {
        const token = localStorage.getItem("jwt");
        const userId = localStorage.getItem("id_usuario");
        const response = await fetch(`http://localhost:8080/historico/compra?page=${numeroPaginaAtual}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
                "Id-Usuario": userId
            },
        });
        
        if (!response.ok) throw new Error("Usuário ou produtos não encontrado");
        
        const dados = await response.json();
        const produtos = dados.content
        console.log(produtos);
        const numTotalPaginas = dados.totalPages;
        const container = document.getElementById("product-list");
        const paginacaoDiv = document.getElementById('numero-pagina');
        const botaoAnterior = document.getElementById('anterior');
        const botaoProximo = document.getElementById('proximo');

        container.innerHTML = '';

        paginacaoDiv.innerText = `Página ${numeroPaginaAtual + 1} de ${numTotalPaginas}`;
        botaoAnterior.disabled = numeroPaginaAtual === 0;
        botaoProximo.disabled = numeroPaginaAtual === (numTotalPaginas - 1);

        produtos.forEach(produto => {
            const card = document.createElement("div");
            card.classList.add("product-card");

            const imagemContainer = document.createElement("div");
            imagemContainer.classList.add("product-image");
            const imagem = document.createElement("img");
            imagem.src = `data:${produto.imagem_tipo};base64,${produto.imagem}`;
            imagemContainer.appendChild(imagem);

            const infoContainer = document.createElement("div");
            infoContainer.classList.add("product-info");
            const titulo = document.createElement("h1");
            titulo.classList.add("product-title");
            titulo.textContent = produto.nome;
            const preco = document.createElement("p");
            preco.classList.add("product-price");
            preco.textContent = `R$ ${produto.preco}`;

            const detalhes = document.createElement("p");
            detalhes.classList.add("product-detalhes");
            infoContainer.appendChild(titulo);
            infoContainer.appendChild(preco);
            infoContainer.appendChild(detalhes);
            card.appendChild(imagemContainer);
            card.appendChild(infoContainer);
            container.appendChild(card);

            card.addEventListener("click", (e) => {
                if (e.target.tagName !== "BUTTON") {
                    window.location.href = `../html/alterarDadosProduto.html?id=${produto.id}`; 
                }
            });
        
        });
    }
    catch(e){

    }
}
document.getElementById('anterior').addEventListener('click', () => {
    if (numeroPaginaAtual > 0) {
        numeroPaginaAtual--;
        carregarProdutos();
    }
});

document.getElementById('proximo').addEventListener('click', () => {
    numeroPaginaAtual++;
    carregarProdutos();
});

document.addEventListener("DOMContentLoaded", () => {
    carregarProdutos();
});