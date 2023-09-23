package nets.tools.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BrotherhoodConfiguration extends OkaeriConfig {

    public int SIZE = 100;
    public int DISTANCE_BETWEEN = 5;

    public List<ItemStack> REQUIREMENTS = Arrays.asList(
            new ItemStack(Material.DIAMOND, 64),
            new ItemStack(Material.GOLD_INGOT, 64),
            new ItemStack(Material.ANVIL, 32),
            new ItemStack(Material.GOLDEN_APPLE, 32),
            new ItemStack(Material.HAY_BLOCK, 64),
            new ItemStack(Material.BOOKSHELF, 32),
            new ItemStack(Material.REDSTONE_BLOCK, 32));

    @Comment("Command Messages")
    public String COMMAND_HELP = """
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ sᴛᴡᴏʀᴢ [ᴛᴀɢ] [ɴᴀᴢᴡᴀ]</gradient> &7- &fsᴛᴡᴏʀᴢ ᴡʟᴀsɴᴇ ʙʀᴀᴄᴛᴡᴏ
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ ᴜsᴜɴ</gradient> &7- &fᴜsᴜɴ ʙʀᴀᴄᴛᴡᴏ
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ ᴡʏᴍᴀɢᴀɴɪᴀ</gradient> &7- &fsᴘʀᴀᴡᴅᴢ ᴡʏᴍᴀɢᴀɴɪᴀ ᴅᴏ ᴢᴀʟᴏᴢᴇɴɪᴀ ʙʀᴀᴄᴛᴡᴀ
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ ᴢᴀᴘʀᴏs [ɴɪᴄᴋ]</gradient> &7- &fᴢᴀᴘʀᴏs ɢʀᴀᴄᴢᴀ ᴅᴏ ʙʀᴀᴄᴛᴡᴀ
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ ᴡʏʀᴢᴜᴄ [ɴɪᴄᴋ]</gradient> &7- &fᴡʏʀᴢᴜᴄ ɢʀᴀᴄᴢᴀ ᴢ ʙʀᴀᴄᴛᴡᴀ
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ ᴍɪᴀɴᴜᴊ [ɴɪᴄᴋ] [ʀᴏʟᴀ]</gradient> &7- &fᴍɪᴀɴᴜᴊ ᴄᴢʟᴏɴᴋᴀ ʙʀᴀᴄᴛᴡᴀ
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ ᴘᴠᴘ</gradient> &7- &fᴡʏʟᴀᴄᴢ sʏsᴛᴇᴍ ᴘᴠᴘ ᴡ ʙʀᴀᴄᴛᴡɪᴇ
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ ʟᴏᴋᴀʟɪᴢᴀᴄᴊᴀ</gradient> &7- &fsᴘʀᴀᴡᴅᴢ ᴋᴏᴏʀᴅʏɴᴀᴛʏ ʙʀᴀᴄᴛᴡᴀ
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ ᴛᴇʟᴇᴘᴏʀᴛᴇʀ</gradient> &7- &fsᴘʀᴀᴡᴅᴢ ɴᴀᴊʙʟɪᴢsᴢʏ ɢɪʟᴅɪɪ ᴛᴇʟᴇᴘᴏʀᴛᴇʀ
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ ᴘᴀɴᴇʟ</gradient> &7- &fᴏᴛᴡᴏʀᴢ ᴘᴀɴᴇʟ ᴢᴀʀᴢᴀᴅᴢᴀɴɪᴀ ʙʀᴀᴄᴛᴡᴇᴍ
            <gradient:#0681C3:#78BADE>[⚓] /ʙʀᴀᴄᴛᴡᴏ sᴏᴊᴜsᴢ</gradient> &7- &fsᴘʀᴀᴡᴅᴢ ᴋᴏᴍᴇɴᴅʏ ᴅᴏ ᴢᴀʀᴢᴀᴅᴢᴀɴɪᴀ sᴏᴊᴜsᴢᴀᴍɪ
            """;

    public String COMMAND_REQUIREMENTS = "";

    public String COMMAND_CREATE_TAG_INVALID = "&c[⚑] &7Tag bractwa zawiera niedozwolone znaki.";
    public String COMMAND_CREATE_TAG_TOO_SHORT = "&c[⚑] &7Tag bractwa jest zbyt krótki. Tag musi zawierać conajmniej &c1 litere.";
    public String COMMAND_CREATE_TAG_TOO_LONG = "&c[⚑] &7Tag bractwa jest zbyt długi. Tag może zawierać maksymalnie &c8 liter.";

    public String COMMAND_CREATE_NAME_INVALID = "&c[⚑] &7Nazwa bractwa zawiera niedozwolone znaki.";
    public String COMMAND_CREATE_NAME_TOO_SHORT = "&c[⚑] &7Nazwa bractwa jest zbyt krótka. Nazwa musi zawierać conajmniej &c5 liter.";
    public String COMMAND_CREATE_NAME_TOO_LONG = "&c[⚑] &7Nazwa bractwa jest zbyt długa. Nazwa może zawierać maksymalnie &c30 liter.";

    @Comment("Event Messages")
    public String EVENT_BLOCK_BREAK = "&c[⛏] &7ɴɪᴇ ᴍᴏᴢᴇsᴢ &cɴɪsᴢᴄᴢʏᴄ&7 ɴᴀ ᴛᴇʀᴇɴɪᴇ ᴛᴇɢᴏ ʙʀᴀᴄᴛᴡᴀ.";
    public String EVENT_BLOCK_PLACE = "&c[⚒] &7ɴɪᴇ ᴍᴏᴢᴇsᴢ &cʙᴜᴅᴏᴡᴀc&7 ɴᴀ ᴛᴇʀᴇɴɪᴇ ᴛᴇɢᴏ ʙʀᴀᴄᴛᴡᴀ.";

    public String EVENT_PVP_DISALLOWED = "&c[⚔] &7ʙɪᴄɪᴇ sɪᴇ ᴢ ɪɴɴʏᴍɪ ᴄᴢʟᴏɴᴋᴀᴍɪ ʙʀᴀᴄᴛᴡᴀ &cɴɪᴇ ᴊᴇsᴛ ᴅᴏᴢᴡᴏʟᴏɴᴇ";
    public String EVENT_PVP_DISALLOWED_BOW = "&c[\uD83C\uDFF9] &7ɴɪᴇ ᴍᴏᴢᴇsᴢ &cᴢᴀsᴛʀᴢᴇʟɪᴄ&7 ᴄᴢʟᴏɴᴋᴀ sᴡᴏᴊᴇɢᴏ ʙʀᴀᴄᴛᴡᴀ";


}
