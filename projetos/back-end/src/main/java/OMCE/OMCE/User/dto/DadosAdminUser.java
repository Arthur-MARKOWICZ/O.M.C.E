package OMCE.OMCE.User.dto;

import OMCE.OMCE.User.Role;

public record DadosAdminUser(
        Long id,
        String nome,
        String nomeUser,
        String email,
        String telefone,
        Role role,
        boolean ativo
) {
}