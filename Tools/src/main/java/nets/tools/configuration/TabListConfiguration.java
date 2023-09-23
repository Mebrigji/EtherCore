package nets.tools.configuration;

import eu.okaeri.configs.OkaeriConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabListConfiguration extends OkaeriConfig {

    public List<String> HEADER = Arrays.asList("<gradient:#0EE008:#59EDF4>ᴇᴛʜᴇʀᴄʀᴀғᴛ.ᴘʟ</gradient>", " ");
    public List<String> FOOTER = Arrays.asList(" ", "<#C5C5C5>ᴘɪɴɢ: <gradient:#7DD604:#B0EA61><ping>ᴍs.</gradient>", "<#C5C5C5>sᴛᴀɴ ᴋᴏɴᴛᴀ: <gradient:#DCC309:#F1E167>$ <balance></gradient>", " ");

    public Map<Integer, String> CELLS = new HashMap<>(){{
        put(0, "                                                                                                          ");
        put(1, "    <gradient:#E08109:#EAB572>sᴛᴀᴛʏsᴛʏᴋɪ</gradient>");
        put(3, " <#C5C5C5>ɴɪᴄᴋ: <gradient:#F01230:#B2273A><playerName></gradient>");
        put(4, " <#C5C5C5>ʀᴀɴɢᴀ: <gradient:#F01230:#B2273A><group></gradient>");
        put(6, " <#C5C5C5>ᴘᴜɴᴋᴛʏ: <gradient:#F01230:#B2273A><rank_user-points></gradient>");
        put(7, " <#C5C5C5>ᴢᴀʙᴏᴊsᴛᴡᴀ: <gradient:#F01230:#B2273A><rank_user-kills> ⚔</gradient>");
        put(8, " <#C5C5C5>sᴍɪᴇʀᴄɪ: <gradient:#F01230:#B2273A><rank_user-deaths> ☠</gradient>");
        put(9, " <#C5C5C5>ᴋᴅ ʀᴀᴛɪᴏ: <gradient:#F01230:#B2273A><kd_ratio></gradient>");
        put(11, " <#C5C5C5>ᴡʏᴋᴏᴘᴀɴᴇ ʙʟᴏᴋɪ: <gradient:#F01230:#B2273A><stats_blocks-mined> ⛏</gradient>");
        put(12, " <#C5C5C5>ᴘᴏsᴛᴀᴡɪᴏɴᴇ ʙʟᴏᴋɪ: <gradient:#F01230:#B2273A><stats_blocks-placed> ⚒</gradient>");
        put(13, " <#C5C5C5>ᴘʀᴢᴇʙʏᴛʏ ᴅʏsᴛᴀɴs: <gradient:#F01230:#B2273A><stats_traveled-distance> ✈</gradient>");
        put(14, " <#C5C5C5>ᴄᴢᴀs ɢʀʏ: <gradient:#F01230:#B2273A><stats_time-spend> \uD83D\uDD50</gradient>");
        put(15, " <#C5C5C5>ᴏsᴛᴀᴛɴɪᴀ sᴍɪᴇʀᴄ: <gradient:#F01230:#B2273A><stats_lastdeath> temu. \uD83D\uDD50</gradient>");
        put(18, " <#C5C5C5>ʀᴇsᴢᴛᴀ sᴛᴀᴛʏsᴛʏᴋ");
        put(19, " &7➤ <gradient:#7DD604:#B0EA61>/sᴛᴀᴛʏsᴛʏᴋɪ</gradient>");
        put(21, "    <gradient:#E08109:#EAB572>ʙʀᴀᴄᴛᴡᴏ</gradient>");
        put(23, " <#C5C5C5>ɴᴀᴢᴡᴀ: <gradient:#F01230:#B2273A><brotherhood_info-name></gradient>");
        put(24, " <#C5C5C5>ᴢᴀʟᴏᴢʏᴄɪᴇʟ: <gradient:#F01230:#B2273A><brotherhood_info-owner></gradient>");
        put(26, " <#C5C5C5>ᴘᴜɴᴋᴛʏ: <gradient:#F01230:#B2273A><rank_brotherhood-points></gradient>");
        put(27, " <#C5C5C5>ᴢᴀʙᴏᴊsᴛᴡᴀ: <gradient:#F01230:#B2273A><rank_brotherhood-kills> ⚔</gradient>");
        put(28, " <#C5C5C5>sᴍɪᴇʀᴄɪ: <gradient:#F01230:#B2273A><rank_brotherhood-deaths> ☠</gradient>");
        put(30, " <#C5C5C5>ᴀᴋᴛʏᴡɴɪ ᴄᴢʟᴏɴᴋᴏᴡɪᴇ: <gradient:#F01230:#B2273A><brotherhood_info-members-online>/<brotherhood_info-members-total></gradient>");
    }};

}
