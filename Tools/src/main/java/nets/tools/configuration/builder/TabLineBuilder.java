package nets.tools.configuration.builder;

import net.minecraft.world.level.EnumGamemode;

import java.util.Map;

public record TabLineBuilder(int slot, String context, String texture, String signature, EnumGamemode gameMode, int ping) {

    public static void add(Map<Integer, TabLineBuilder> map, TabLineBuilder tabLineBuilder){
        map.put(tabLineBuilder.slot, tabLineBuilder);
    }

}