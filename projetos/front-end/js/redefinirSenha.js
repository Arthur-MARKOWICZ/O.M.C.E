document.getElementById("resetForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("email-reset").value.trim();

    if (!email) {
        alert("Email inválido ou não cadastrado");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/auth/redefinirSenha", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email })
        });

        if (response.ok) {
            alert("Se o email estiver cadastrado, enviaremos um link para redefinir sua senha.");
        } else {
            alert("Erro ao tentar redefinir a senha.");
        }
    } catch (error) {
        console.error("Erro:", error);
        alert("Erro ao conectar com o servidor.");
    }
});
