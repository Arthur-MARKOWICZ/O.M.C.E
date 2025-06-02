const parametro = new URLSearchParams(window.location.search);
const produtoId = parametro.get('id'); 
console.log(produtoId);
fetch(`http://localhost:8080/produto/visualizarDetalhesProduto/${produtoId}`)
    .then(resposta => resposta.json())
    .then(produto => {
        console.log(produto);
        
        const id_vendedor = produto.id_vendedor;
        document.getElementById('produto-nome').textContent = produto.nome;
        document.getElementById('subistituir').textContent = produto.subistituir;
        document.getElementById('produto-detalhes').textContent = produto.detalhes;
        document.getElementById('produto-preco').textContent = produto.preco;
        document.getElementById('nome-vendedor').textContent = produto.nome_do_usuario;
        document.getElementById('condicao-produto').textContent = produto.condicao;

        console.log(id_vendedor)
        console.log(produto.nome_do_usuario)
        const img = document.createElement('img');
        img.src = `data:${produto.Imagem_tipo};base64,${produto.Imagem}`;
        img.alt = 'Imagem do Produto';
        document.getElementById('product-image-container').appendChild(img);
        document.getElementById('botaocarrinho').addEventListener('click', () => {
            adicionarProduto(
                produto.nome,
                produto.preco,
                produto.id,
                produto.condicao,
                produto.Imagem,
                produto.Imagem_tipo,
                produto.id_usuario
            );
        });
        document.getElementById('botao').addEventListener('click', () => {
    window.location.href = `../html/AvaliarVendedor.html?vendedor=${encodeURIComponent(id_vendedor)}`;
});
fetch(`http://localhost:8080/avaliacaoVendedor/media/${id_vendedor}`)
    .then(resposta => resposta.json())
    .then(media => {
         const elementoMedia = document.getElementById('avaliacao');
        if (elementoMedia) {
            elementoMedia.innerText = `Média de avaliacao do vendedor: ${media.toFixed(1)} ⭐`;
        }
    });

});
