const user_id = localStorage.getItem("id_usuario");
const token =  localStorage.getItem("jwt");
let paginaAtual = 0;
async function carregarAvaliacao(){
const resposta = await fetch(`http://localhost:8080/avaliacoes/criar`, {

        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${localStorage.getItem('jwt')}` },
        })
        const dados = await resposta.json();
        const avaliacao = dados.content;
        const totalPaginas = dados.totalPages;
        const container = document.getElementById("avaliacao-container");
        const paginacaoTexto = document.getElementById('numero-pagina');
        const botaoAnterior = document.getElementById('anterior');
        const botaoProximo = document.getElementById('proximo');

        container.innerHTML = "";
        paginacaoTexto.innerText = `Página ${paginaAtual + 1} de ${totalPaginas}`;

        avaliacao.forEach(avaliacao => {
            const card = document.createElement("div");
            card.classList.add("avaliacao-card");

            card.innerHTML = `
                <h2>${avaliacao.nota}</h2>
                <p><strong>comentario:</strong> ${avaliacao.comentario}</p>
              
            `;

            container.appendChild(card);

        });

        

        botaoAnterior.disabled = paginaAtual === 0;
        botaoProximo.disabled = paginaAtual === (totalPaginas - 1);

;
}
document.addEventListener("DOMContentLoaded", () => {
    carregarAvaliacao();
});