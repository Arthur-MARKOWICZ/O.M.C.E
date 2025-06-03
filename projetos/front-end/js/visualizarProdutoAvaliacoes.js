const parametro = new URLSearchParams(window.location.search);
const produtoId = parametro.get('id'); 

let paginaAtual = 0;

async function carregarAvaliacoesProduto(pagina = 0) {
    const resposta = await fetch(`http://localhost:8080/avaliacoes/produto/${produtoId}?page=${pagina}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json", 
        }
    });

    if (!resposta.ok) {
        console.error("Erro ao buscar avaliações");
        return;
    }

    const dados = await resposta.json();

    const container = document.getElementById("avaliacao-container");
    const paginacaoTexto = document.getElementById('numero-pagina');
    const botaoAnterior = document.getElementById('anterior');
    const botaoProximo = document.getElementById('proximo');

    container.innerHTML = "";

    dados.content.forEach(avaliacao => {
        const card = document.createElement("div");
        card.classList.add("avaliacao-card");
        card.innerHTML = `
            <p>${avaliacao.subistituir}</p>
            <h3>Nota: ${avaliacao.nota} ⭐</h3>
            <p>${avaliacao.comentario}</p>
            
        `;
        container.appendChild(card);
    });

    paginacaoTexto.innerText = `Página ${dados.number + 1} de ${dados.totalPages}`;

    botaoAnterior.disabled = dados.first;
    botaoProximo.disabled = dados.last;

    botaoAnterior.onclick = () => {
        if (paginaAtual > 0) {
            paginaAtual--;
            carregarAvaliacoesProduto(paginaAtual);
        }
    };

    botaoProximo.onclick = () => {
        if (paginaAtual < dados.totalPages - 1) {
            paginaAtual++;
            carregarAvaliacoesProduto(paginaAtual);
        }
    };
}

document.addEventListener("DOMContentLoaded", () => {
    carregarAvaliacoesProduto();
});
