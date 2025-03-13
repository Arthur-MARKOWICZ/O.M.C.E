const nome = document.getElementById("txtName");
const preco = document.getElementById("txtPreco");
const detalhes = document.getElementById("txtDetalhes");
const id_usuario = localStorage.getItem("id_usuario");
const id = Number(id_usuario);
function btnSendOnClickProduto(){
    if (nome.value === "") {
        alert("Preenchimento obrigatório: Nome");
        nome.focus();
        return false;
    }
    if (preco.value === "") {
        alert("Preenchimento obrigatório: Preco");
        preco.focus();
        return false;
    }
    if (detalhes.value === "") {
        alert("Preenchimento obrigatório: Detalhes");
        detalhes.focus();
        return false;
    }
    return true;
}
document.getElementById("form_cadastroProduto").addEventListener("submit", async function(event)  {
    event.preventDefault();
    const token = localStorage.getItem("jwt");

    const produto = {
        nome : nome.value,
        preco : parseFloat(preco.value),
        detalhes : detalhes.value,
        id_usuario: id
    };
    console.log(produto);
    const response = await fetch("http://localhost:8080/auth/cadastroProduto", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(produto)
       
        

    });

    if (response.ok) {
        alert("Usuário Produto com sucesso!");
    } else {
        alert("Erro no cadastro.");
    }
})