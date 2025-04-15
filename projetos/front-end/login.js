document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("loginForm")?.addEventListener("submit", async (e) => {
        e.preventDefault();

        const email = document.getElementById("email").value.trim();
        const senha = document.getElementById("senha").value.trim();

        if (!email || !senha) {
            alert("Preenchimento obrigatório: Email e Senha");
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
                alert("Login falhou! Verifique suas credenciais.");
                return;
            }

            const data = await response.json();
            console.log(data);
            if (data.token && data.id) { 
                localStorage.setItem("id_usuario", data.id); 
                localStorage.setItem("jwt", data.token);
                const token = localStorage.getItem("jwt");
                alert("Login bem-sucedido!");
                window.location.href = "/front-end/home.html"; 
            } else {
                alert("Erro: Nenhum token ou ID de usuário recebido!");
                console.error("Resposta do servidor:", data); 
            }
        } catch (error) {
            alert("Erro ao conectar com o servidor.");
            console.error("Erro:", error);
        }
    });
});
