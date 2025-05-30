const params = new URLSearchParams(window.location.search);
const token = localStorage.getItem("jwt");
const id_vendedor = params.get('vendedor');
if (id_vendedor) {
    // remove os parâmetros da URL
    window.history.replaceState({}, document.title, window.location.pathname);
}
const nota = document.getElementById("nota-avaliacao");
const comentario = document.getElementById("Comentario");
document.getElementById("form-avaliarVendedor").addEventListener("submit", async function(event) {
    event.preventDefault();
const avaliacao ={
    nota: nota.value,
    comentario: comentario.value,
    vendedor_id: id_vendedor
}
const response = await fetch("http://localhost:8080/avaliacaoVendedor/cadastro", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(avaliacao)
        });
        const text = await response.text();
        let json;
        try {
          json = text ? JSON.parse(text) : {};
        } catch {
          json = {};
        }
       if(!response.ok) {

            alert("erro ao conectar ao servidor")
          return;
        }
        alert("avaliacao cadastrado com sucesso");
            event.target.reset();
            window.location.href = "../html/feed.html";
    }); 
