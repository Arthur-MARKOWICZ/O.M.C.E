const { default: Swal } = require("sweetalert2");
document.getElementById("novaSenhaForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const novaSenha = document.getElementById("novaSenha").value.trim();
  const confirmarSenha = document.getElementById("confirmarSenha").value.trim();

  if (novaSenha !== confirmarSenha) {
    Swal.fire({
      text: "As senhas não coincidem.",
      icon: 'warning'
    })
    return;
  }

  const urlParams = new URLSearchParams(window.location.search);
  const token = urlParams.get("token");

  if (!token) {
    Swal.fire({
      text: "Token de redefinição inválido ou ausente.",
      icon: 'warning'
    })
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/user/novaSenha", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        token: token,
        novaSenha: novaSenha
      })
    });

    if (response.ok) {
      window.location.href = "login.html";
    } else {
      const erro = await response.text();
      Swal.fire({
        text: "Erro ao redefinir a senha: " + erro,
        icon: 'warning'
      })
    }
  } catch (err) {
    console.error("Erro:", err);
    Swal.fire({
      text: "Erro ao conectar com o servidor.",
      icon: 'warning'
    })
  }
});