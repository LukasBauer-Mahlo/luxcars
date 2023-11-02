package de.luxcars.backend.services.account;

import de.luxcars.backend.services.account.object.Account;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface AccountService {

  @NotNull Optional<Account> getAccount(@NotNull String mail);

  @NotNull Optional<Account> getAccount(int id);

  @NotNull Account createAccount(@NotNull String mail, @NotNull String firstName, @NotNull String lastName, @NotNull String password, boolean administrator);

  void updateAccount(@NotNull Account account);

  @NotNull List<Account> getAccounts();

}
