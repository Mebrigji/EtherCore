package nets.tools.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;

import java.util.Arrays;
import java.util.List;


@Names(strategy = NameStrategy.IDENTITY, modifier = NameModifier.TO_UPPER_CASE)
public class Messages extends OkaeriConfig {

    public List<String> WHITELIST_DISALLOW = Arrays.asList("<gradient:#0EE008:#59EDF4>ᴇᴛʜᴇʀᴄʀᴀғᴛ.ᴘʟ</gradient>",
            "&7&m                                       ",
            " ",
            "&fTworzymy paczke na serwer",
            " ",
            "&7&m                                       ",
            "<gradient:#46CB03:#89E35B>ᴊᴇsʟɪ ᴘᴏᴛʀᴢᴇʙᴜᴊᴇsᴢ ᴡɪᴇᴄᴇᴊ ɪɴғᴏʀᴍᴀᴄᴊɪ</gradient>",
            "<gradient:#89E35B:#46CB03>ᴢᴀᴘʀᴀsᴢᴀᴍʏ ɴᴀ ɴᴀsᴢᴇɢᴏ ᴅɪsᴄᴏʀᴅᴀ</gradient>",
            "<gradient:#B1D006:#FFFFFF>https://dc.ethercraft.pl/</gradient>");

    @Comment("Admin Command messages")

    public String COMMAND_ORE_ADD = "&c[⚑] &7Dodano nową rude do generatora.";
    public String COMMAND_ORE_REMOVE = "&c[⚑] &7Usunięto rude z generatora.";

    public String COMMAND_ORE_EDIT_CHANCE = "&c[⚑] &7Szansa na wygenerowanie się rudy <gradient:#F01230:#B2273A><ore></gradient> została zmieniona na <#B2273A><chance>%";

    public String COMMAND_FLY_ON = "&c[⚑] &7Pomyślnie uzyskano dostęp do latania.\n&aLatanie zostało włączone.";
    public String COMMAND_FLY_OFF = "&c[⚑] &7Usunięto dostęp do latania.\n&cLatanie zostało wyłączona.";

    public String COMMAND_GAMEMODE_ALREADY = "&c[⚑] &7Zmiana trybu gry nie powiodła się. &cJuż posiadasz ten tryb gry.";
    public String COMMAND_GAMEMODE_CHANGED = "&c[⚑] &7Wymuszono zmiane trybu gry z <gradient:#F01230:#B2273A><old></gradient> &7na <gradient:#F01230:#B2273A><new></gradient>";
    public String COMMAND_GAMEMODE_FORCE = "&c[⚑] &7Wymuszono zmiane trybu użytkownika <gradient:#F01230:#B2273A><player> # <old></gradient> &7na <gradient:#F01230:#B2273A><new></gradient>";

    public String COMMAND_INVSEE = "&c[⚑] &7Uruchomiono przeglądanie ekwipunku użytkownika <gradient:#F01230:#B2273A><player></gradient>";
    public String COMMAND_ENDERSEE = "&c[⚑] &7Uruchomiono przeglądanie enderchesta użytkownika <gradient:#F01230:#B2273A><player></gradient>";

    public String COMMAND_ITEM_NAME = "&c[⚑] &7Wymuszono zmiane nazwy przedmiotu.\n&a[✚] <new_name>\n&c[✖] <old_name>";
    public String COMMAND_ITEM_NAME_AIR = "&4[ITEM-NAME] &cPrzedmiot nie może być potwietrzem.";
    public String COMMAND_ITEM_NAME_CLEAR = "&c[⚑] &7Wymoszono usunięcie nazwy przedmiotu.";

    public String COMMAND_ITEM_LORE = "&c[⚑] &7Wymuszono zmiane opisu przedmiotu.\n<gradient:#F01230:#B2273A><show_item></gradient>";
    public String COMMAND_ITEM_LORE_CLEAR = "&c[⚑] &7Wymuszono usunięcie opisu z przedmiotu.";
    public String COMMAND_ITEM_LORE_AIR = "&4[ITEM-LORE] &cPrzedmiot nie może być potwietrzem.";

    public String COMMAND_ITEM_MORE_AIR = "&4[ITEM-MORE] &cPrzedmiot nie może być potwietrzem.";
    public String COMMAND_ITEM_MORE_INCREASE = "&4[ITEM-MORE] &7Podniesiono liczbę przedmiotu z &a<old> &7do &2<new>";
    public String COMMAND_ITEM_MORE_DECREASE = "&4[ITEM-MORE] &7Zmniejszono liczbę przedmiotu z &c<old> &7do &4<new>";
    public String COMMAND_ITEM_MORE_EQUAL = "&4[ITEM-MORE] &7Nie zmniejszono ani nie zwiekszono liczby przedmiotu, ponieważ nowa ilość jest &6taka sama&7.";

    public String COMMAND_ENCHANT_APPLY = "&c[⚑] &7Zaklęcie <gradient:#F01230:#B2273A><enchantment></gradient> &7poziomu <gradient:#F01230:#B2273A><level></gradient> &7zostało dodane do przedmiotu.";
    public String COMMAND_ENCHANT_ALREADY = "&c[⚑] &7Przedmiot posiada już zaklęcie <gradient:#F01230:#B2273A><enchantment></gradient> &7o tym samym poziomie.";
    public String COMMAND_ENCHANT_REMOVE = "&c[⚑] &7Zaklęcie <gradient:#F01230:#B2273A><enchantment></gradient> &7zostało usunięte z przedmiotu.";
    public String COMMAND_ENCHANT_EDIT = "&c[⚑] &7Zmieniono poziom zaklęcia <gradient:#F01230:#B2273A><enchantment></gradient> &7z &c<old_level> &7na &a<new_level>";
    public String COMMAND_ENCHANT_NOT_FOUND = "&4[ITEM-ENCHANT] &cPrzedmiot nie posiada tego zaklecia wiec nie mozna go usunac.";
    public String COMMAND_ENCHANT_AIR = "&4[ITEM-ENCHANT] &cPrzedmiot nie może być potwietrzem.";


    public String COMMAND_PING = "&c[⚑] &Twój ping wynosi <gradient:#F01230:#B2273A><ping>ms.</gradient>";
    public String COMMAND_PING_OTHER = "&c[⚑] &7Ping użytkownika <gradient:#F01230:#B2273A><player></gradient> &7wynosi <gradient:#F01230:#B2273A><ping>ms.</gradient>";

    public String COMMAND_GIVE = "&c[⚑] &7Gracz <gradient:#F01230:#B2273A><player></gradient> &7pomyślnie otrzymał nowy przedmiot. <gradient:#F01230:#B2273A><item></gradient>";
    public String COMMAND_GIVE_SELF = "&c[⚑] &7Otrzymano nowy przedmiot. <gradient:#F01230:#B2273A><item></gradient>";

    public String COMMAND_CHAT = """
            &c[⚑] &7Dostępne komendy dla <gradient:#F01230:#B2273A>/chat</gradient>
            &c[⚑] <gradient:#F01230:#B2273A>/ᴄʜᴀᴛ ᴄʟᴇᴀʀ</gradient> &8- &7Wyczyść czat.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴄʜᴀᴛ ᴏɴ</gradient> &8- &7Włącz pisanie na czacie.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴄʜᴀᴛ ᴏғғ</gradient> &8- &7Wyłącz pisanie na czacie.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴄʜᴀᴛ ʟᴇᴠᴇʟ [ᴍɪɴɪɴɢ-ʟᴇᴠᴇʟ]</gradient> &8- &7Ustaw ile potrzeba wykopać bloków, aby pisać na czacie.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴄʜᴀᴛ ғʟᴀɢs</gradient> &8- &7Sprawdź dostępne flagi dla czatu.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴄʜᴀᴛ ғʟᴀɢ [ғʟᴀɢ-ɴᴀᴍᴇ] [ᴛʀᴜᴇ/ғᴀʟsᴇ]</gradient> &8- &7Zmień wartości dla poszczególnej flagi.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴄʜᴀᴛ ᴘᴀɴᴇʟ &8- &7Otwórz panel sterowania czatem.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴄʜᴀᴛ ᴘᴇʀᴍɪssɪᴏɴ [ᴘᴇʀᴍɪssɪᴏɴ]</gradient> &8- &7Zmien wymagane uprawnienia do pisania na czacie.
            &c[⚑] <gradient:#F01230:#B2273A>/ᴄʜᴀᴛ ᴘᴇʀᴍɪssɪᴏɴ ᴄʟᴇᴀʀ</gradient> &8- &7Usun wymagane uprawnienia do pisania na czacie.""";

    public String COMMAND_CHAT_ENABLE_GLOBAL = "\n[<gradient:#D5AC04:#EDD262>sʏsᴛᴇᴍ</gradient>] ᴄᴢᴀᴛ ɢʟᴏʙᴀʟɴʏ ᴢᴏsᴛᴀʟ <gradient:#04BD12:#60EB6B>ᴡʟᴀᴄᴢᴏɴʏ</gradient>\n";
    public String COMMAND_CHAT_DISABLE_GLOBAL = "\n[<gradient:#D5AC04:#EDD262>sʏsᴛᴇᴍ</gradient>] ᴄᴢᴀᴛ ɢʟᴏʙᴀʟɴʏ ᴢᴏsᴛᴀʟ <gradient:#CE0A01:#F77069>ᴡʏʟᴀᴄᴢᴏɴʏ</gradient>\n";

    public String COMMAND_CHAT_DISABLE_ALREADY = "&c[⚑] &7ᴄᴢᴀᴛ ᴊᴇsᴛ ᴊᴜᴢ &cᴡʏʟᴀᴄᴢᴏɴʏ";
    public String COMMAND_CHAT_ENABLE_ALREADY = "&c[⚑] &7ᴄᴢᴀᴛ ᴊᴇsᴛ ᴊᴜᴢ &aᴡʟᴀᴄᴢᴏɴʏ";

    public String COMMAND_CHAT_PERMISSION_CLEAR = "\n[<gradient:#D5AC04:#EDD262>sʏsᴛᴇᴍ</gradient>] ᴜsᴜɴɪᴇᴛᴏ ᴡʏᴍᴀɢᴀɴᴇ ᴜᴘʀᴀᴡɴɪᴇɴɪᴀ ᴅᴏ ᴘɪsᴀɴɪᴀ ɴᴀ ᴄᴢᴀᴄɪᴇ\n";
    public String COMMAND_CHAT_PERMISSION_SET = "\n[<gradient:#D5AC04:#EDD262>sʏsᴛᴇᴍ</gradient>] ᴜsᴛᴀᴡɪᴏɴᴏ ɴᴏᴡᴇ ᴡʏᴍᴀɢᴀɴᴇ ᴜᴘʀᴀᴡɴɪᴇɴɪᴀ. &7➡ &c<old> &7- &a<new>\n";

    public String COMMAND_CHAT_CLEAR = "<loops>\n[<gradient:#D5AC04:#EDD262>sʏsᴛᴇᴍ</gradient>] ᴄᴢᴀᴛ ᴢᴏsᴛᴀʟ ᴡʏᴄᴢʏsᴢᴄᴢᴏɴʏ ᴘʀᴢᴇᴢ ᴀᴅᴍɪɴɪsᴛʀᴀᴛᴏʀᴀ <gradient:#F01230:#B2273A><admin></gradient>\n";

    public String COMMAND_CHAT_LEVEL_CHANGED = "\n[<gradient:#D5AC04:#EDD262>sʏsᴛᴇᴍ</gradient>] ᴢᴍɪᴇɴɪᴏɴᴏ <gradient:#0575CC:#66B0E9>ᴡʏᴍᴀɢᴀɴᴀ ɪʟᴏsᴄ</gradient> ᴡʏᴋᴏᴘᴀɴʏᴄʜ ʙʟᴏᴋᴏᴡ ᴀʙʏ ᴘɪsᴀᴄ ɴᴀ ᴄᴢᴀᴄɪᴇ. ᴏᴅ ᴛᴇʀᴀᴢ ᴏʙᴏᴡɪᴀᴢʏᴡᴀɴᴇ ᴍɪɴɪᴍᴜᴍ ᴛᴏ <gradient:#0575CC:#66B0E9><amount> ʙʟᴏᴋᴏᴡ ⛏</gradient>\n";

    public String COMMAND_CHAT_FLAGS = "&c[⚑] &7ᴅᴏsᴛᴇᴘɴᴇ ғʟᴀɢɪ ᴅʟᴀ ᴄᴢᴀᴛᴜ:\n<items>\n<inventory>\n<enderchest>\n<kills>";

    public String COMMAND_CHAT_FLAG_FORMAT_ENABLED = " &7- &fFlaga: <flag-name> <click:run_command:\"/chat flag <flag-name> false\">&c[ ᴡʏʟᴀᴄᴢ << ᴋʟɪᴋɴɪᴊ ]</click>";
    public String COMMAND_CHAT_FLAG_FORMAT_DISABLED = " &7- &fFlaga: <flag-name> <click:run_command:\"/chat flag <flag-name> true\">&a[ ᴡʟᴀᴄᴢ << ᴋʟɪᴋɴɪᴊ ]</click>";

    public String COMMAND_TELEPORT_TO_PLAYER = "&c[⚑] &7Pomyślnie przeteleportowano do gracza <gradient:#F01230:#B2273A><player></gradient>";
    public String COMMAND_TELEPORT_TARGET_TO_PLAYER = "&c[⚑] &7Pomyślnie przeteleportowano gracza <gradient:#F01230:#B2273A><target></gradient> na lokalizacje gracza <gradient:#F01230:#B2273A><player></gradient>";

    public String COMMAND_TELEPORT_TO_LOCATION = "&c[⚑] &7Pomyślnie przeteleportowano na koordynaty <gradient:#F01230:#B2273A><location></gradient>";
    public String COMMAND_TELEPORT_TARGET_TO_LOCATION = "&c[⚑] &7Pomyślnie przeteleportowano gracza <gradient:#F01230:#B2273A><target></gradient> na koordynaty <gradient:#F01230:#B2273A><location></gradient>";

    public String COMMAND_CLEAR = "&c[⚑] &7Zawartość twojego ekwipunku została usunięta.";
    public String COMMAND_CLEAR_SUPPOSED_ITEM = "&c[⚑] &7Usunięto przedmioty o zgodnym typie <gradient:#F01230:#B2273A><type></gradient> &7z twojego ekwipunku.";

    public String COMMAND_CLEAR_OTHER = "&c[⚑] &7Zawartość ekwipunku gracza <gradient:#F01230:#B2273A><player></gradient> &7została usunięta.";
    public String COMMAND_CLEAR_OTHER_SUPPOSED_ITEM = "&c[⚑] &7Usunięto przedmioty o zgodnym typie <gradient:#F01230:#B2273A><type></gradient> &7od użytkownika <gradient:#F01230:#B2273A><player></gradient>";

    public String COMMAND_CLEAR_USER_NOT_FOUND = "&c[✖] &7Użytkownik o tej nazwie nie został odnaleziony w bazie danych";
    public String COMMAND_CLEAR_MATERIAL_NOT_FOUND = "&c[✖] &7Określony typ przedmiotu nie został odnaleziony.";

    public String COMMAND_SUDO_INVOKED_COMMAND = "&c[⚑] &7Wymuszono polecenie <gradient:#F01230:#B2273A><command></gradient> &7na graczu <gradient:#F01230:#B2273A><player></gradient>";
    public String COMMAND_SUDO_INVOKED_CHAT = "&c[⚑] &7Wymuszono wysłanie wiadomości <gradient:#F01230:#B2273A><message></gradient> &7na graczu <gradient:#F01230:#B2273A><player></gradient>";

    public String COMMAND_EXP_BOTTLE_CREATED = "&c[⚑] &7Stworzono butlke z zawartym <gradient:#F01230:#B2273A><exp></gradient> exp oraz dano ją graczowi <gradient:#F01230:#B2273A><player></gradient>";

    public String COMMAND_ADMIN_KIT_ENABLE_ALREADY_ENABLED = "&c[⚑] &7Zestaw <gradient:#F01230:#B2273A><kit></gradient> &7jest już &awłączony&7.";
    public String COMMAND_ADMIN_KIT_ENABLE_ALREADY_DISABLED = "&c[⚑] &7Zestaw <gradient:#F01230:#B2273A><kit></gradient> &7jest już &cwyłączony&7.";

    public String COMMAND_ADMIN_KIT_ENABLE_ENABLED = "&c[⚑] &7Zestaw <gradient:#F01230:#B2273A><kit></gradient> &7został &awłączony&7.";
    public String COMMAND_ADMIN_KIT_ENABLE_DISABLED = "&c[⚑] &7Zestaw <gradient:#F01230:#B2273A><kit></gradient> &7został &cwyłączony&7.";

    public String COMMAND_ADMIN_KIT_ENABLE_ALL_ENABLED = "&c[⚑] &7Pomyślnie &awłączono&7 wszystkie zestawy.";
    public String COMMAND_ADMIN_KIT_ENABLE_ALL_DISABLED = "&c[⚑] &7Pomyślnie &cwyłączono&7 wszystkie zestawy.";

    public String COMMAND_ADMIN_KIT_CREATE = "&c[⚑] &7Pomyślnie stworzono zestaw <gradient:#F01230:#B2273A><kit></gradient> oraz umieszczono go na pozycji <#B2273A><slot>&7.";
    public String COMMAND_ADMIN_KIT_CREATE_ALREADY = "&c[⚑] &7Zestaw <gradient:#F01230:#B2273A><kit></gradient> &7jest już stworzony.";

    public String COMMAND_ADMIN_KIT_DELETE = "&c[⚑] &7Zestaw <gradient:#F01230:#B2273A><kit></gradient> został pomyślnie usunięty.";

    public String COMMAND_ADMIN_KIT_EDIT_PERMISSION_CLEAR = "&c[⚑] &7Aby użyć zestawu <gradient:#F01230:#B2273A><kit></gradient> nie potrzeba od teraz żadnych uprawnień.";
    public String COMMAND_ADMIN_KIT_EDIT_PERMISSION_SET = "&c[⚑] &7Aby użyć zestawu <gradient:#F01230:#B2273A><kit></gradient> &7od teraz potrzeba posiadać uprawnienia <gradient:#F01230:#B2273A><permission></gradient>";

    public String COMMAND_ADMIN_KIT_EDIT_ITEMS = "&c[⚑] &7Zapisano <gradient:#F01230:#B2273A><items></gradient> przedmiotów w zestawie <gradient:#F01230:#B2273A><kit></gradient>.";

    public String COMMAND_ADMIN_KIT_EDIT_DELAY_CLEAR = "&c[⚑] &7Usunięto opóźnienie w odbiorze zestawu <gradient:#F01230:#B2273A><kit></gradient>";
    public String COMMAND_ADMIN_KIT_EDIT_DELAY_SET = "&c[⚑] &7Aby odebrać zestaw <gradient:#F01230:#B2273A><kit></gradient> trzeba od teraz odczekać <gradient:#F01230:#B2273A><time></gradient>.";

    public String COMMAND_ADMIN_KIT_EDIT_ICON = "&c[⚑] &7Zmieniono ikone dla zestawu <gradient:#F01230:#B2273A><kit></gradient>.";

    public String COMMAND_ADMIN_KIT_EDIT_SLOT = "&c[⚑] &7Zmieniono slot dla zestawu <gradient:#F01230:#B2273A><kit></gradient> na <#B2273A><slot>.";

    public String COMMAND_ADMIN_KIT_EDIT_DISPLAY_NAME = "&c[⚑] &7Ustawiono nową wyświetlaną nazwe dla zestawu <gradient:#F01230:#B2273A><kit></gradient>.";

    public String COMMAND_ADMIN_KIT_EDIT_LORE_CLEAR = "&c[⚑] &7Usunięto opis dla zestawu <gradient:#F01230:#B2273A><kit></gradient>.";
    public String COMMAND_ADMIN_KIT_EDIT_LORE_SET = "&c[⚑] &7Dodano opis dla zestawu <gradient:#F01230:#B2273A><kit></gradient>.";

    public String COMMAND_ADMIN_KIT_GRANT = "&c[⚑] &7Przyznano zestaw <gradient:#F01230:#B2273A><kit></gradient> &7graczowi o nicku <gradient:#F01230:#B2273A><player></gradient>";
    public String COMMAND_ADMIN_KIT_GRANT_USER_NOT_FOUND = "&c[✖] &7Użytkownik o tej nazwie nie został odnaleziony w bazie danych";

    @Comment("Player Command Messages")

    public String COMMAND_HOLOGRAM_HELP = """
            &c[⚑] &7Dostępne komendy dla <gradient:#F01230:#B2273A>/hologram</gradient>
            &c[⚑] <gradient:#F01230:#B2273A>/ʜᴏʟᴏɢʀᴀᴍ ᴄʀᴇᴀᴛᴇ <ɴᴀᴢᴡᴀ> <ᴛᴇxᴛ> <ᴀᴄᴛɪᴠᴀᴛɪᴏɴ ʀᴀɴɢᴇ></gradient> &8- &7Stwórz nowy hologram.
            """;

    public String COMMAND_MESSAGE_FORMAT_SENDER = "<gradient:#96D005:#C5E17D>[✉]</gradient> <gradient:#F01230:#B2273A>Ja</gradient> &f-> <gradient:#F01230:#B2273A><player></gradient> <#E5E5E5>: <gradient:#B4B4B4:#CBCBCB><message></gradient>";
    public String COMMAND_MESSAGE_FORMAT_RECEIVER = "<gradient:#96D005:#C5E17D>[✉]</gradient> <gradient:#F01230:#B2273A><player></gradient><#E5E5E5> &f-> <gradient:#F01230:#B2273A>Ja</gradient>: <gradient:#B4B4B4:#CBCBCB><message></gradient>";
    public String COMMAND_MESSAGE_REPLY_NOT_FOUND = "&c[⚑] &7Nie znaleziono nikogo komu możesz odpisać.";

    public String COMMAND_HAT_AIR = "&c[⚑] Nie możesz założyć na głowe powietrza.";
    public String COMMAND_HAT_OCCUPIED = "&c[⚑] Nie możesz mieć nic założonego na głowie.";
    public String COMMAND_HAT = "&c[⚑] &7Przedmiot został założony na głowe.";

    public String COMMAND_CHANGE_NOT_ENOUGH = "&c[⚑] Nie posiadasz wymaganej ilości bruku. &4<amount> / <required>";
    public String COMMAND_CHANGE = "&c[⚑] &7Wymieniono <gradient:#F01230:#B2273A><amount></gradient> &7bruku na <gradient:#F01230:#B2273A><exp></gradient> &7doświadczenia.";
    public String COMMAND_CHANGE_MANY = "&c[⚑] &7Możesz wymienić <gradient:#F01230:#B2273A><amount></gradient> bruku i zyskać na tym <gradient:#F01230:#B2273A><experience></gradient> doświadczenia.";

    public String COMMAND_EXP_BOTTLE_HAVE = "&c[⚑] &7Posiadane doświadczenie: <gradient:#F01230:#B2273A><amount></gradient>";
    public String COMMAND_EXP_BOTTLE_NOT_ENOUGH = "&c[⚑] &7Posiadasz zbyt mało doświadczenia, <gradient:#BE6FF1:#7B10C1><have>/<required></gradient>";
    public String COMMAND_EXP_BOTTLE_WITHDRAW = "&c[⚑] &7Wypłacono <gradient:#F01230:#B2273A><amount></gradient> &7doświadczenia.";
    public String COMMAND_EXP_BOTTLE_FULL = "&c[⚑] Nie posiadasz miejsca w ekwipunku.";
    public String COMMAND_EXP_BOTTLE_TOTAL_ZERO = "&c[⚑] Nie posiadasz doświadczenia do wypłacenia.";

    @Comment("Events")

    public String EVENT_CHAT_DISABLED = "&c[✖] Czat jest aktualnie wyłączony.";
    public String EVENT_CHAT_NOT_ENOUGH_LEVEL = "&c[⛏] Wykopałeś(aś) zbyt mało bloków, aby pisać na chacie.\n<#EA0E00>Musisz wykopać jeszcze &n<amount>&r<#EA0E00> bloków, aby odblokować te możliwość.";
}
