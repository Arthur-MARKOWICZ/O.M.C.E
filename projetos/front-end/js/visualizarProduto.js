const parametro = new URLSearchParams(window.location.search);
const produtoId = parametro.get('id');

if (!produtoId) {
    alert("Produto não especificado!");
    window.location.href = "pagina-de-erro.html";
}

fetch(`http://localhost:8080/produto/visualizarDetalhesProduto/${produtoId}`)
    .then(resposta => resposta.json())
    .then(produto => {
        const id_vendedor = produto.id_vendedor;

        document.getElementById('produto-nome').textContent = produto.nome;
        document.getElementById('produto-detalhes').textContent = produto.detalhes;
        document.getElementById('produto-preco').textContent = produto.preco;
        document.getElementById('nome-vendedor').textContent = produto.nome_do_usuario;
        document.getElementById('condicao-produto').textContent = produto.condicao;

        if (produto.Imagem && produto.Imagem_tipo) {
            const img = document.createElement('img');
            img.src = `data:${produto.Imagem_tipo};base64,${produto.Imagem}`;
            img.alt = 'Imagem do Produto';
            document.getElementById('product-image-container').appendChild(img);
        }

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

        document.getElementById('ver-avaliacoes').addEventListener('click', () => {
            window.location.href = `../html/visualizarAvaliacoesProduto.html?id=${encodeURIComponent(produto.id)}`;
        });

        fetch(`http://localhost:8080/avaliacaoVendedor/media/${id_vendedor}`)
            .then(resposta => resposta.json())
            .then(media => {
                const elementoMedia = document.getElementById('avaliacao');
                if (elementoMedia) {
                    elementoMedia.innerText = `Média de avaliação do vendedor: ${media.toFixed(1)} ⭐`;
                }
            })
            .catch(() => {
                document.getElementById('avaliacao').innerText = "Não foi possível carregar a média.";
            });
    })
    .catch(() => {
        alert("Erro ao carregar os dados do produto.");
    });
