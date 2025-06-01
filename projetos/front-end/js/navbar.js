document.addEventListener("DOMContentLoaded", function () {
    fetch("navbar.html")
        .then((response) => {
            if (!response.ok) throw new Error("Erro ao carregar navbar.");
            return response.text();
        })
        .then((data) => {
            const navbarContainer = document.getElementById("navbar-container");
            if (navbarContainer) {
                navbarContainer.innerHTML = data;

                console.log("Navbar carregada com sucesso!");
            } else {
                console.warn("Elemento de ID 'navbar-container' não encontrado.");
            }
        })
        .catch((error) => {
            console.error("Erro:", error.message);
        });
});
function logout(){
   localStorage.removeItem("id_usuario");
   localStorage.removeItem("jwt");
}