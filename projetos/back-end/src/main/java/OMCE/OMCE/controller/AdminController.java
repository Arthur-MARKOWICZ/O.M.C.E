package OMCE.OMCE.controller;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.User.Role;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosAlterarRole;
import OMCE.OMCE.User.dto.DadosDashboardAdmin;
import OMCE.OMCE.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import OMCE.OMCE.User.dto.DadosAdminUser;
import OMCE.OMCE.Produto.dto.DadosAdminProduto;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProdutoRepository produtoRepository;


    // Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<DadosDashboardAdmin> dashboard() {

        long totalUsuarios = userRepository.count();
        long totalCompradores = userRepository.countByRole(Role.COMPRADOR);
        long totalVendedores = userRepository.countByRole(Role.VENDEDOR);
        long totalProdutos = produtoRepository.count();

        return ResponseEntity.ok(
                new DadosDashboardAdmin(
                        totalUsuarios,
                        totalCompradores,
                        totalVendedores,
                        totalProdutos
                )
        );
    }

// Usuarios
    @GetMapping("/usuarios")
    public ResponseEntity<List<DadosAdminUser>> listarUsuarios() {
        List<DadosAdminUser> usuarios = userRepository.findAll()
                .stream()
                .map(user -> new DadosAdminUser(
                        user.getId(),
                        user.getNome(),
                        user.getNomeUser(),
                        user.getEmail(),
                        user.getTelefone(),
                        user.getRole(),
                        user.isAtivo()
                ))
                .toList();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<DadosAdminUser> visualizarUsuario(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        DadosAdminUser dados = new DadosAdminUser(
                user.getId(),
                user.getNome(),
                user.getNomeUser(),
                user.getEmail(),
                user.getTelefone(),
                user.getRole(),
                user.isAtivo()
        );

        return ResponseEntity.ok(dados);
    }

    @PutMapping("/usuarios/{id}/role")
    @Transactional
    public ResponseEntity<Void> alterarRole(
            @PathVariable Long id,
            @RequestBody DadosAlterarRole dados
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        user.setRole(dados.role());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/usuarios/{id}/status")
    @Transactional
    public ResponseEntity<Void> alterarStatus(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        user.setAtivo(!user.isAtivo());

        return ResponseEntity.ok().build();
    }

// Produtos
    @GetMapping("/produtos")
    public ResponseEntity<List<DadosAdminProduto>> listarProdutos() {
        List<DadosAdminProduto> produtos = produtoRepository.findAll()
                .stream()
                .map(produto -> new DadosAdminProduto(
                        produto.getId(),
                        produto.getNome(),
                        produto.getPreco(),
                        produto.getCategoria(),
                        produto.isVendido()
                ))
                .toList();

        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<DadosAdminProduto> visualizarProduto(@PathVariable Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        DadosAdminProduto dados = new DadosAdminProduto(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getCategoria(),
                produto.isVendido()
        );

        return ResponseEntity.ok(dados);
    }

    @DeleteMapping("/produtos/{id}")
    @Transactional
    public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {

        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        produtoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}