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

            if (data.token) {
                localStorage.setItem("jwt", data.token); 
                alert("Login bem-sucedido!");
                window.location.href = "/home.html"; 
            } else {
                alert("Erro: Nenhum token recebido!");
            }
        } catch (error) {
            console.error("Erro ao conectar com o servidor:", error);
            alert("Erro na comunicação com o servidor.");
        }
    });
});
