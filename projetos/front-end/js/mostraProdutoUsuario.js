let numeroPaginaAtual = 0;
const nomeProduto = document.getElementById("produto-nome");

async function carregarProdutos() {
    try {
        const token = localStorage.getItem("jwt");
        const userId = localStorage.getItem("id_usuario");
        const response = await fetch(`http://localhost:8080/produto/todosProdutosUsuario?page=${numeroPaginaAtual}`, {
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

            const botaoDeletar = document.createElement("button");
            botaoDeletar.classList.add("delete-button");
            botaoDeletar.textContent = "Deletar";
            botaoDeletar.addEventListener("click", async (e) => {
                e.stopPropagation();

                Swal.fire({
                    title: 'Tem certeza?',
                    text: `Deseja excluir o produto "${produto.nome}"? Esta ação não pode ser desfeita!`,
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Sim, excluir!',
                    cancelButtonText: 'Cancelar',
                }).then(async (result) => {
                    if (result.isConfirmed) {
                        try {
                            const token = localStorage.getItem("jwt");

                            const response = await fetch(`http://localhost:8080/produto/deletar/${produto.id}`, {
                                method: "DELETE",
                                headers: {
                                    "Authorization": `Bearer ${token}`
                                }
                            });

                            if (response.ok) {
                                Swal.fire({
                                    title: "Produto deletado com sucesso!",
                                    icon: "success",
                                });
                                carregarProdutos();
                            } else {
                                Swal.fire({
                                    title: "Erro ao deletar o produto",
                                    text: "Ocorreu um problema ao tentar deletar o produto.",
                                    icon: "error",
                                });
                            }
                        } catch (error) {
                            Swal.fire({
                                title: "Erro",
                                text: "Houve um erro ao processar sua solicitação.",
                                icon: "error",
                            });
                        }
                    }
                });
            });

            infoContainer.appendChild(botaoDeletar);
        });
    } catch (error) {
        console.error("Erro ao carregar produtos:", error);
        Swal.fire("Erro ao carregar produtos. Por favor, tente novamente.");
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