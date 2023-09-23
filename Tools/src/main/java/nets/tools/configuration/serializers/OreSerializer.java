package nets.tools.configuration.serializers;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import nets.tools.model.Ore;
import nets.tools.objects.OreController;
import org.bukkit.Material;

public class OreSerializer implements ObjectSerializer<Ore> {
    @Override
    public boolean supports(Class<? super Ore> type) {
        return Ore.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(Ore object, SerializationData data) {
        data.add("what", object.what());
        data.add("chance", object.chance());
        data.add("y", object.y());
        data.add("displayName", object.displayName());
        data.add("lore", object.lore());
    }

    @Override
    public Ore deserialize(DeserializationData data, GenericsDeclaration generics) {
        return new OreController(data.get("what", Material.class), data.get("chance", double.class), data.get("y", int.class), data.get("displayName", String.class), data.getAsList("lore", String.class));
    }
}
