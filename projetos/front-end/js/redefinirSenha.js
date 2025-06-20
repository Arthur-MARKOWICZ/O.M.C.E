const { default: Swal } = require("sweetalert2");

const Swal = new Swal()
document.getElementById("resetForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("email-reset").value.trim();

    if (!email) {
        Swal.fire({
            text:"Email inválido ou não cadastrado",
            icon: 'warning'
        })
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
            Swal.fire({
                text:"Email enviado com sucesso.",
                icon: 'warning'
            })
        } else {
            Swal.fire({
                text:"Erro ao tentar redefinir a senha.",
                icon:'warning'
            })
        }
    } catch (error) {
        console.error("Erro:", error);
        Swal.fire({
            text:"Erro ao conectar com o servidor.",
            icon: 'warning'
        })
    }
});
