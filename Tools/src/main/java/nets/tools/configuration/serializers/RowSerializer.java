package nets.tools.configuration.serializers;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import net.minecraft.world.level.EnumGamemode;
import nets.tools.configuration.builder.TabLineBuilder;

public class RowSerializer implements ObjectSerializer<TabLineBuilder> {
    @Override
    public boolean supports(Class<? super TabLineBuilder> type) {
        return TabLineBuilder.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(TabLineBuilder object, SerializationData data) {
        data.add("slot", object.slot());
        data.add("context", object.context());
        data.add("head.texture", object.texture());
        data.add("head.signature", object.signature());
        data.add("mode", object.gameMode());
        data.add("ping", object.ping());
    }

    @Override
    public TabLineBuilder deserialize(DeserializationData data, GenericsDeclaration generics) {
        return new TabLineBuilder(data.get("slot", int.class), data.get("context", String.class), data.get("head.texture", String.class), data.get("head.signature", String.class), data.get("mode", EnumGamemode.class), data.get("ping", int.class));
    }
}
