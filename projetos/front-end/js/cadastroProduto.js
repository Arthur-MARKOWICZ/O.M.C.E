
const nome = document.getElementById("txtName");
const preco = document.getElementById("txtPreco");
const detalhes = document.getElementById("txtDetalhes");
const imagem = document.getElementById("productImage");
const id_usuario = localStorage.getItem("id_usuario");
const id = Number(id_usuario);


function btnSendOnClickProduto() {

    if (nome.value === "") {

        Swal.fire("Preenchimento obrigatório: Nome");

        nome.focus();
        return false;
    }
    if (preco.value === "") {

        Swal.fire("Preenchimento obrigatório: Preço");

        preco.focus();
        return false;
    }
    if (detalhes.value === "") {

        Swal.fire("Preenchimento obrigatório: Detalhes");

        detalhes.focus();
        return false;
    }
    if (!imagem.files[0]) {
        Swal.fire("Preenchimento obrigatório: Imagem");
        imagem.focus();
        return false;
    }
    return true;
}

function toBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
        reader.readAsDataURL(file); 
    });
}

document.getElementById("form_cadastroProduto").addEventListener("submit", async function (event) {
    event.preventDefault();

    if (!btnSendOnClickProduto()) return;

    try {
        const condicao = document.querySelector('input[name="Condicao"]:checked');
        const categoria = document.querySelector('input[name="categoria"]:checked');
        const token = localStorage.getItem("jwt");

        if (!condicao || !categoria) {
            Swal.fire({
                text: "selecione uma condicao e categoria",
                icon: 'warning'
            })
            return;
        }

        if (!token) {
            Swal.fire({
                text: "Token JWT não encontrado. Faça login novamente.",
                icon: 'warning'
            })
            return;
        }

        const file = imagem.files[0];
        const base64 = await toBase64(file);

        const produto = {
            nome: nome.value,
            preco: parseFloat(preco.value.replace(',', '.')),
            detalhes: detalhes.value,
            condicao: condicao.value,
            categoria: categoria.value,
            id_usuario: id,
            imagem: base64.split(",")[1],
            imagem_tipo: file.type
        };

        const response = await fetch("http://localhost:8080/produto/cadastroProduto", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(produto)
        });

        if (response.ok) {
            Swal.fire({
              title:"Seu produto foi anunciado com sucesso!",
              icon: 'success'});
            window.location.href = "../html/feed.html";
        } else {

            Swal.fire("Erro no cadastro.");
        }
    } catch (error) {
        console.error("Erro ao cadastrar produto:", error);
        Swal.fire("Falha no cadastro.");

    }
});

