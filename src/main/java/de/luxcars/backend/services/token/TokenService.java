package de.luxcars.backend.services.token;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface TokenService {

  @NotNull String generateToken(int userId);

  @NotNull Optional<Integer> getUserIdByToken(@NotNull String token);

  void invalidateToken(@NotNull String token);

  void invalidateAllTokens(int userId);

}
