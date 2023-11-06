package de.luxcars.backend.util;

import de.luxcars.backend.LuxCarsBackend;
import de.luxcars.backend.services.account.object.Account;
import java.text.SimpleDateFormat;
import org.jetbrains.annotations.NotNull;

public class AccountOnlineState {

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

  public static String getOnlineState(@NotNull Account account) {
    if (LuxCarsBackend.getInstance().getChatWebSocket().isOnline(account.getId())) {
      return "Online";
    }

    return SIMPLE_DATE_FORMAT.format(account.getLastOnline());
  }

}
