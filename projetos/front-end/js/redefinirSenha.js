document.getElementById("resetForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("email-reset").value.trim();

    if (!email) {
         Swal.fire("Email inválido ou não cadastrado");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/user/redefinirSenha", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email })
        });

        if (response.ok) {
             Swal.fire("Email enviado com sucesso.");
        } else {
             Swal.fire("Erro ao tentar redefinir a senha.");
        }
    } catch (error) {
        console.error("Erro:", error);
         Swal.fire("Erro ao conectar com o servidor.");
    }
});
