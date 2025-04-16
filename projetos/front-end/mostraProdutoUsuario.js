const nomeProduto = document.getElementById("produto-nome");
async function carregarProdutos() {
    const token = localStorage.getItem("jwt");
    const userId = localStorage.getItem("id_usuario");
        const response = await fetch(`http://localhost:8080/produto/todosProdutosUsuario`,{
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
                "Id-Usuario": userId
            },
        });
        if (!response.ok) throw new Error("Usuário ou produtos não encontrado");
        const produtos = await response.json();

        console.log(produtos);
        const listaProdutos = document.getElementById("product-list");

        listaProdutos.innerHTML = '';

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
            listaProdutos.appendChild(card);
            card.addEventListener("click", (e) => {
                if (e.target.tagName !== "BUTTON") {
                    window.location.href = `alterarDadosProduto.html?id=${produto.id}`; 
                }
            });
        });



}

document.addEventListener("DOMContentLoaded", () => {

    carregarProdutos();
});