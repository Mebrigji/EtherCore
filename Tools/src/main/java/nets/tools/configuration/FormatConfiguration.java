package nets.tools.configuration;

import eu.okaeri.configs.OkaeriConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormatConfiguration extends OkaeriConfig {

    public String GUILD_FORMAT_EXISTS_OWN = "<#54C506>[<guild>]";
    public String GUILD_FORMAT_EXISTS_ALLY = "<#E8AD10>[<guild>]";
    public String GUILD_FORMAT_EXISTS_ENEMY = "<#E82A10>[<guild>]";
    public String GUILD_FORMAT_EMPTY = "";

    public Map<String, String> formats = new LinkedHashMap<>(){{
        put("default", "<guild> <player>: <message>");
    }};

}
