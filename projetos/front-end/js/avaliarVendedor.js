const params = new URLSearchParams(window.location.search);
const token = localStorage.getItem("jwt");
const id_vendedor = params.get('vendedor');
if (id_vendedor) {
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
if(nota.value == ""){
    exibirErro("Campo de nota e obrigatório");
    return;
}
if(comentario.value == ""){
    exibirErro("Campo de comentario e obrigatório");
        return;

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
            Swal.fire({
                text:"Erro ao conectar ao servidor",
                icon: 'warning',
            })
          return;
        }
        alert("Avaliação cadastrado com sucesso");
            event.target.reset();
            window.location.href = "../html/feed.html";
    }); 
function exibirErro(mensagem, campo) {
    Swal.fire({
      title: "Não foi possível realizar seu cadastro",
      text:"Erro no/a: " + mensagem,
      icon:'warning',
      confirmButtonText: 'OK'});("Erro no/a: " + mensagem);
    campo.focus();
    return false;
}