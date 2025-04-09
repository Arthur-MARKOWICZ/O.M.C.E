const parametro = new URLSearchParams(window.location.search);
const produtoId = parametro.get('id'); 
console.log(produtoId);
fetch(`http://localhost:8080/produto/visualizarDetalhesProduto/${produtoId}`)
