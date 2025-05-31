document.getElementById("novaSenhaForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const novaSenha = document.getElementById("novaSenha").value.trim();
  const confirmarSenha = document.getElementById("confirmarSenha").value.trim();

  if (novaSenha !== confirmarSenha) {
    alert("As senhas não coincidem.");
    return;
  }

  const urlParams = new URLSearchParams(window.location.search);
  const token = urlParams.get("token");

  if (!token) {
    alert("Token de redefinição inválido ou ausente.");
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
      alert("Senha atualizada com sucesso!");
      window.location.href = "login.html";
    } else {
      const erro = await response.text();
      alert("Erro ao redefinir a senha: " + erro);
    }
  } catch (err) {
    console.error("Erro:", err);
    alert("Erro ao conectar com o servidor.");
  }
});