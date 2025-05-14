package OMCE.OMCE.User;

import jakarta.validation.constraints.NotNull;

public record LoginResponseDTO(@NotNull String token,@NotNull long id) {
}
