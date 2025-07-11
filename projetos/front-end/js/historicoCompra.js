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
        const produtos = dados.content;
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

            const avaliacaoProduto = document.createElement("button");
            avaliacaoProduto.classList.add("product-avaliacao");
            avaliacaoProduto.textContent = "Avaliar";
            avaliacaoProduto.addEventListener("click",  (event) => {
                event.stopPropagation();
                abrirFormularioAvaliacao(produto.id, card);
            });

            const avaliarVendedor = document.createElement("button");
            avaliarVendedor.classList.add("avaliacaoVendedor");
            avaliarVendedor.textContent = "Avaliar Vendedor";
            avaliarVendedor.addEventListener("click", (event) => {
                event.stopPropagation();    
                window.location.href = `../html/AvaliarVendedor.html?vendedor=${encodeURIComponent(produto.id_vendedor)}`;
            });

            infoContainer.appendChild(titulo);
            infoContainer.appendChild(preco);
            infoContainer.appendChild(detalhes);
            infoContainer.appendChild(avaliacaoProduto);
            infoContainer.appendChild(avaliarVendedor);

            card.appendChild(imagemContainer);
            card.appendChild(infoContainer);
            container.appendChild(card);
        });
    }
    catch(e){
        console.error("Erro ao carregar produtos:", e);
        Swal.fire({
            title: "Erro",
            text: "Erro ao carregar produtos. Tente novamente mais tarde.",
            icon: "error"
        });
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

let idProdutoAtual = null;

function abrirFormularioAvaliacao(idProduto, card) {
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

    if (!card.querySelector('#form-avaliacao')) {
        card.insertAdjacentHTML('beforeend', formulario);
    }
}

async function enviarAvaliacao() {
    const nota = parseInt(document.getElementById("nota-avaliacao").value);
    const comentario = document.getElementById("comentario-avaliacao").value;

    if (isNaN(nota) || nota < 1 || nota > 5 || comentario.trim() === "") {
        Swal.fire({
            title: "Erro",
            text: "Por favor, insira uma nota entre 1 e 5 e um comentário.",
            icon: "warning"
        });
        return;
    }

    const resposta = await fetch(`http://localhost:8080/avaliacoes/criar`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem('jwt')}`
        },
        body: JSON.stringify({ nota: nota, comentario: comentario, idProduto: idProdutoAtual })
    });

    if (resposta.ok) {
        Swal.fire({
            title:"Avaliação enviada com sucesso!",
            icon: 'success'
        });
        document.getElementById("form-avaliacao").remove();
        exibirMedia(idProdutoAtual); 
    } else {
        Swal.fire({
            text: "Erro ao enviar avaliação.",
            icon: "warning"
        });
    }
}
