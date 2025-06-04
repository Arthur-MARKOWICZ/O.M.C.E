let paginaAtual = 0;
let filtrosSelecionados = {};

async function carregarFeed(filtros = {}) {
    const parametros = new URLSearchParams({
        page: paginaAtual,
        ...filtros
    });

    try {
        const resposta = await fetch(`http://localhost:8080/produto/filtro?${parametros.toString()}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            }
        });

        const dados = await resposta.json();
        const produtos = dados.content;
        const totalPaginas = dados.totalPages;
        const container = document.getElementById("feed-container");
        const paginacaoTexto = document.getElementById('numero-pagina');
        const botaoAnterior = document.getElementById('anterior');
        const botaoProximo = document.getElementById('proximo');

        container.innerHTML = "";
        paginacaoTexto.innerText = `Página ${paginaAtual + 1} de ${totalPaginas}`;

        produtos.forEach(produto => {
            const card = document.createElement("div");
            card.classList.add("produto-card");

            card.innerHTML = `
                <h2>${produto.nome}</h2>
                <img class="produto-imagem" src="data:${produto.imagem_tipo};base64,${produto.imagem}" alt="${produto.nome}" width="200" data-id="${produto.id}" style="cursor: pointer;">
                <p><strong>Preço:</strong> R$ ${produto.preco.toFixed(2)}</p>
                <p><strong>Condição:</strong> ${produto.condicao}</p>
                <p><strong>Vendedor:</strong> ${produto.nomeUsuario}</p>
                <p id="media-avaliacao-${produto.id}">Média: -</p>
                <button onclick="adicionarProduto('${produto.nome}', ${produto.preco}, ${produto.id}, '${produto.imagem}', '${produto.imagem_tipo}', '${produto.id_usuario}')">Adicionar ao Carrinho</button>
                <button onclick="abrirFormularioAvaliacao(${produto.id})">Avaliar Produto</button>
            `;


            container.appendChild(card);
            console.log("Produto:", produto);
            exibirMedia(produto.id);
        });


        
        document.querySelectorAll('.produto-imagem').forEach(img => {
            img.addEventListener('click', (evento) => {
                const id = evento.target.getAttribute('data-id');
                window.location.href = `../html/visualizarProduto.html?id=${id}`;
            });
        });

        botaoAnterior.disabled = paginaAtual === 0;
        botaoProximo.disabled = paginaAtual === (totalPaginas - 1);

    } catch (erro) {
        console.error("Erro ao carregar o feed:", erro);
        Swal.fire({
            title:"Erro ao carregar o feed",
            icon:'info'});
    }
}

document.getElementById('anterior').addEventListener('click', () => {
    if (paginaAtual > 0) {
        paginaAtual--;
        carregarFeed(filtrosSelecionados);
    }
});

document.getElementById('proximo').addEventListener('click', () => {
    paginaAtual++;
    carregarFeed(filtrosSelecionados);
});

document.getElementById('form-filtro').addEventListener('submit', (evento) => {
    evento.preventDefault();
    paginaAtual = 0;

    const nome = document.getElementById('filtro-nome').value;
    const categoria = document.getElementById('filtro-categoria').value;
    const precoMin = document.getElementById('filtro-preco-min').value;
    const precoMax = document.getElementById('filtro-preco-max').value;

    filtrosSelecionados = {};
    if (nome) filtrosSelecionados.nome = nome;
    if (categoria) filtrosSelecionados.categoria = categoria;
    if (precoMin) filtrosSelecionados.precoMin = precoMin;
    if (precoMax) filtrosSelecionados.precoMax = precoMax;

    carregarFeed(filtrosSelecionados);
});


document.addEventListener("DOMContentLoaded", () => {
    carregarFeed();
});

let idProdutoAtual = null;

function abrirFormularioAvaliacao(idProduto) {
    idProdutoAtual = idProduto;
    const formulario = `
        <div id="form-avaliacao" style="margin-top: 10px;">
            <h4>Avaliar Produto</h4>
            <label>Nota (1-5):</label>
            <input type="number" id="nota-avaliacao" min="1" max="5"><br>
            <label>Comentário:</label><br>
            <textarea id="comentario-avaliacao"></textarea><br>
            <button onclick="enviarAvaliacao()">Enviar Avaliação</button>
        </div>`;

    const cardProduto = document.querySelector(`#media-avaliacao-${idProduto}`).parentElement;
    if (!document.getElementById('form-avaliacao')) {
        cardProduto.insertAdjacentHTML('beforeend', formulario);
    }
}

async function enviarAvaliacao() {
    const nota = parseInt(document.getElementById("nota-avaliacao").value);
    const comentario = document.getElementById("comentario-avaliacao").value;



    
    const resposta = await fetch(`http://localhost:8080/avaliacoes/criar`, {

        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${localStorage.getItem('jwt')}` },
        body: JSON.stringify({ nota: nota, comentario: comentario, idProduto: idProdutoAtual })

    });
    console.log(nota);
    console.log(comentario);
    console.log(idProdutoAtual);




    if (resposta.ok) {
        Swal.fire({
              title:"Avaliação enviada com sucesso!",
              icon: 'success'});
        document.getElementById("form-avaliacao").remove();
        exibirMedia(idProdutoAtual);
    } else {
        alert("Erro ao enviar avaliação.");
    }
}

async function exibirMedia(idProduto) {
    try {
        const resposta = await fetch(`http://localhost:8080/avaliacoes/produto/${idProduto}/media`);
        const media = await resposta.json();
        const elementoMedia = document.getElementById(`media-avaliacao-${idProduto}`);
        if (elementoMedia) elementoMedia.innerText = `Média: ${media.toFixed(1)} ⭐`;
    } catch (erro) {
        console.error("Erro ao carregar a média de avaliação:", erro);
    }
}
