document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("loginForm")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const email = document.getElementById("email").value;
        const password = document.getElementById("senha").value;

        const response = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });
        

        if (response.ok) {
            alert("Login bem-sucedido!");
            window.location.href = "/front-end/home.html";
        } else {
            alert("Login falhou!");
        }
    });
});
