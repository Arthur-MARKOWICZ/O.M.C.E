const nome = document.getElementById("txtName");
const preco = document.getElementById("txtPreco");
const detalhes = document.getElementById("txtDetalhes");
const imagem = document.getElementById("productImage");
const id_usuario = localStorage.getItem("id_usuario");
const id = Number(id_usuario);

function btnSendOnClickProduto() {
    if (nome.value === "") {
        alert("Preenchimento obrigatório: Nome");
        nome.focus();
        return false;
    }
    if (preco.value === "") {
        alert("Preenchimento obrigatório: Preço");
        preco.focus();
        return false;
    }
    if (detalhes.value === "") {
        alert("Preenchimento obrigatório: Detalhes");
        detalhes.focus();
        return false;
    }
    if (!imagem.files[0]) {
        alert("Preenchimento obrigatório: Imagem");
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
        const token = localStorage.getItem("jwt");
        const file = imagem.files[0];
        const base64 = await toBase64(file);
        console.log(token)
        const produto = {
            nome: nome.value,
            preco: parseFloat(preco.value),
            detalhes: detalhes.value,
            id_usuario: id,
            imagem: base64.split(",")[1], 
            imagem_tipo: file.type 
        };

        console.log(produto);

        const response = await fetch("http://localhost:8080/produto/cadastroProduto", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(produto)
        }); 
        if (response.ok) {
            alert("Produto cadastrado com sucesso!");
        } else {
            alert("Erro no cadastro.");
        }
    } catch (error) {
        console.error("Erro ao cadastrar produto:", error);
        alert("Falha no cadastro.");
    }
});
