const parametro = new URLSearchParams(window.location.search);
const produtoId = parametro.get('id'); 
console.log(produtoId);
fetch(`http://localhost:8080/produto/visualizarDetalhesProduto/${produtoId}`)
    .then(resposta => resposta.json())
    .then(produto => {
        console.log(produto);
        document.getElementById('produto-nome').textContent = produto.nome;
        document.getElementById('produto-detalhes').textContent = produto.detalhes;
        document.getElementById('produto-preco').textContent = produto.preco;
        
        const img = document.createElement('img');
        img.src = `data:${produto.Imagem_tipo};base64,${produto.Imagem}`;
        img.alt = 'Imagem do Produto';
        document.getElementById('product-image-container').appendChild(img);
        document.getElementById('botaocarrinho').addEventListener('click', () => {
            adicionarProduto(
                produto.nome,
                produto.preco,
                produto.id,
                produto.Imagem,
                produto.Imagem_tipo,
                produto.id_usuario
            );
        });
    });