function abrirFormularioAvaliacao(event, idProduto) {
    event.stopPropagation(); // impede de ir pra página do produto
    document.getElementById('id-produto').value = idProduto;
    document.getElementById('avaliacao-modal').style.display = 'block';
}

function fecharModal() {
    document.getElementById('avaliacao-modal').style.display = 'none';
}

document.getElementById('avaliacao-form').addEventListener('submit', function (e) {
    e.preventDefault();

    const dto = {
        score: parseInt(document.getElementById('score').value),
        comment: document.getElementById('comment').value,
        id_produto: parseInt(document.getElementById('id-produto').value)
    };

    fetch('http://localhost:8080/reviews', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dto)
    }).then(res => {
        if (res.ok) {
            alert('Avaliação enviada!');
            fecharModal();
            carregarAvaliacao(dto.id_produto);
        } else {
            alert('Erro ao enviar avaliação');
        }
    });

    const nome = document.getElementById('filtro-nome').value;
    const categoria = document.getElementById('filtro-categoria').value;
    const precoMin = document.getElementById('filtro-preco-min').value;
    const precoMax = document.getElementById('filtro-preco-max').value;

    filtrosAtuais = {};
    if (nome) filtrosAtuais.nome = nome;
    if (categoria) filtrosAtuais.categoria = categoria;
    if (precoMin) filtrosAtuais.precoMin = precoMin;
    if (precoMax) filtrosAtuais.precoMax = precoMax;

    carregarFeed(filtrosAtuais);
});

document.addEventListener("DOMContentLoaded", () => {
    carregarFeed(); 
});
