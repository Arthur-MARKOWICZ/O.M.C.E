let produtoAtualizado = { imagem: "", imagem_tipo: "", id: null };

document.addEventListener("DOMContentLoaded", async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const produtoId = urlParams.get("id");
 
    if (!produtoId) {
        Swal.fire({                
            text:"Produto não encontrado",
            icon:'error'});
        return;
    }
    const token = localStorage.getItem("jwt");
    const userId = localStorage.getItem("id_usuario");

    try {
        const response = await fetch(`http://localhost:8080/produto/visualizarDetalhesProduto/${produtoId}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
                "Id-Usuario": userId
            },
        });

        if (!response.ok) {
            Swal.fire({
                text:"Erro ao buscar produto",
                icon:'error'});
            return;
        }

        const produto = await response.json();
        console.log(produto);

        produtoAtualizado = {
            imagem: produto.Imagem, 
            imagem_tipo: produto.Imagem_tipo,
            id: produto.id 
        };

        document.getElementById("txtName").value = produto.nome;
        document.getElementById("txtPreco").value = produto.preco;
        document.getElementById("txtDetalhes").value = produto.detalhes || "";

        const imgPreview = document.createElement("img");
        imgPreview.src = `data:${produto.Imagem_tipo};base64,${produto.Imagem}`;
        imgPreview.style.maxWidth = "200px";
        imgPreview.style.marginTop = "10px";
        
        document.getElementById("previewContainer").appendChild(imgPreview);

    } catch (error) {
        console.error("Erro:", error);
    }
});

const nome = document.getElementById("txtName");
const preco = document.getElementById("txtPreco");
const detalhes = document.getElementById("txtDetalhes");
const imagem = document.getElementById("productImage");
const id_usuario = localStorage.getItem("id_usuario");
const id = Number(id_usuario);

let produtoDepois = { imagem: "", imagem_tipo: "" };

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

document.getElementById("form_alteraDadosProduto").addEventListener("submit", async function (event) {
    event.preventDefault();

    if (!btnSendOnClickProduto()) return; 

    try {
        const token = localStorage.getItem("jwt");
        const file = imagem.files[0];

        let produtoalterar = {
            id: produtoAtualizado.id,
            nome: nome.value,
            preco: parseFloat(preco.value),
            detalhes: detalhes.value,
            id_usuario: id,
            imagem: produtoAtualizado.imagem,  
            imagem_tipo: produtoAtualizado.imagem_tipo
        };

        if (file) {
            const base64 = await toBase64(file);
            produtoDepois.imagem = base64.split(",")[1]; 
            produtoDepois.imagem_tipo = file.type;

            produtoalterar.imagem = produtoDepois.imagem;
            produtoalterar.imagem_tipo = produtoDepois.imagem_tipo;
        }

        console.log(token);
        console.log(produtoalterar);

        const response = await fetch("http://localhost:8080/produto/alterarDadosProduto", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(produtoalterar)
        });

        if (response.ok) {
            Swal.fire({
                title:"Produto alterado com sucesso!",
                icon:'success'});
            window.location.href = "../html/feed.html";
        } else {
            Swal.fire("Erro na alteração.");
        }
    } catch (error) {
        console.error("Erro ao alterar produto:", error);
        Swal.fire("Falha na alteração.");
    }
});
