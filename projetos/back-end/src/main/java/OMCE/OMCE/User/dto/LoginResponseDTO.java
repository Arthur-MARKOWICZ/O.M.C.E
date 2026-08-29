package OMCE.OMCE.User.dto;

import jakarta.validation.constraints.NotNull;

public record LoginResponseDTO(@NotNull String token,@NotNull long id,@NotNull String nome,@NotNull String role) {
}
