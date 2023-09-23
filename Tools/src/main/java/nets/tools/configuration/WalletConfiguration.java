package nets.tools.configuration;

import eu.okaeri.configs.OkaeriConfig;

public class WalletConfiguration extends OkaeriConfig {

    public String WALLET_ID = "EtherCraft-Guilds";

    public boolean WALLED_ENABLED = true;

    public String COMMAND_WALLET = "&6[\uD83D\uDCB0] &7Twój aktualny stan konta wynosi <gradient:#7DD604:#B0EA61><balance> zł</gradient>";

    public String COMMAND_WALLET_TRANSFER_SENDER = "&6[\uD83D\uDCB0] &7Pomyślnie przelano <gradient:#D6C00E:#EFE27C><amount> zł</gradient> &7na rzecz użytkownika <gradient:#7DD604:#B0EA61><player></gradient>";
    public String COMMAND_WALLET_TRANSFER_TARGET = "&6[\uD83D\uDCB0] &7Otrzymano przelew w wysokości <gradient:#D6C00E:#EFE27C><amount> zł</gradient> &7od użytkownika <gradient:#7DD604:#B0EA61><player></gradient>";
    public String COMMAND_WALLET_TRANSFER_SAME = "&c[⚑] &7Nie możesz przesłać sobie pieniędzy.";
    public String COMMAND_WALLET_TRANSFER_MUST_BE_HIGHER_THAT_ONE = "&c[⚑] &7Kwota którą chcesz przelać musi być większa lub równa 1";
    public String COMMAND_WALLET_TRANSFER_NOT_ENOUGH_MONEY = "&c[⚑] &7Nie posiadasz wystarczającej ilości pieniędzy.";

    public String COMMAND_WALLET_ERROR_NOT_EXISTS = "&c[⚑] &7Nie odnaleziono określonego użytkownika w bazie danych.";
    public String COMMAND_WALLET_SET = "&c[⚑] &7Stan konta użytkownika <gradient:#F01230:#B2273A><player></gradient> &7został ustawiony na <gradient:#D6C00E:#EFE27C><amount> zł</gradient>";
    public String COMMAND_WALLET_WITHDRAW = "&c[⚑] &7Stan konta użytkownika <gradient:#F01230:#B2273A><player></gradient> &7został zmniejszony o <gradient:#D6C00E:#EFE27C><amount> zł</gradient>";
    public String COMMAND_WALLET_DEPOSIT = "&c[⚑] &7Stan konta użytkownika <gradient:#F01230:#B2273A><player></gradient> &7został zwiększony o <gradient:#D6C00E:#EFE27C><amount> zł</gradient>";

    public String COMMAND_WALLET_HELP = """
            &c[⚑] &7Dostępne komendy dla <gradient:#F01230:#B2273A>/wallet</gradient>
            &c[⚑] <gradient:#F01230:#B2273A>/ᴡᴀʟʟᴇᴛ</gradient> &8- &7Sprawdź aktualny stan konta.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴛʀᴀɴsғᴇʀ [ɴᴀᴍᴇ] [ᴀᴍᴏᴜɴᴛ]</gradient> &8- &7Przelej &eX &7zł użytkownikowi.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴇᴄᴏ sᴇᴛ [ɴᴀᴍᴇ] [ʙᴀʟᴀɴᴄᴇ]</gradient> &8- &7Ustaw nowy stan konta użytkownikowi.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴇᴄᴏ ᴀᴅᴅ [ɴᴀᴍᴇ] [ᴀᴍᴏᴜɴᴛ]</gradient> &8- &7Dodaj &eX &7zł użytkownikowi.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴇᴄᴏ ʀᴇᴍᴏᴠᴇ [ɴᴀᴍᴇ] [ᴀᴍᴏᴜɴᴛ]</gradient> &8- &7Usuń &eX &7zł użytkownikowi.""";

}
