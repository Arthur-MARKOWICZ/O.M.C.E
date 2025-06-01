document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("loginForm")?.addEventListener("submit", async (e) => {
        e.preventDefault();

        const email = document.getElementById("email").value.trim();
        const senha = document.getElementById("senha").value.trim();

        if (!email || !senha) {
            Swal.fire({
                title:"Não foi possível realizar seu login",
                text:"Preenchimento obrigatório: E-mail e senha",
                icon:'warning'});
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, senha })
            });

            if (!response.ok) {
                Swal.fire({
                    title:"Não foi possível realizar seu login",
                    text:"Verifique suas credenciais",
                    icon:'warning'});
                return;
            }

            const data = await response.json();
            console.log(data);
            if (data.token && data.id) { 
                localStorage.setItem("id_usuario", data.id); 
                localStorage.setItem("jwt", data.token);
                localStorage.setItem("nome",data.nome);
                const token = localStorage.getItem("jwt");
                Swal.fire({
                    Title:"Login bem-sucedido!",
                    text:"Bem-vindo ao O.M.C.E",
                    icon:'success'});
                window.location.href = "../html/home.html"; 
            } else {
                Swal.fire("Erro: Nenhum token ou ID de usuário recebido!");
                console.error("Resposta do servidor:", data); 
            }
        } catch (error) {
            Swal.fire({
            title:"Algo deu errado:(",
            text:"Erro de conexão com o servidor",
            icon:'warning'});
            console.error("Erro:", error);
        }
    });
});
function irCadastro(){
     window.location.href = "../html/cadastro.html"; 
}