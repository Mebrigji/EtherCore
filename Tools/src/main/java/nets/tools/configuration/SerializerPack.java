package nets.tools.configuration;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import nets.tools.configuration.serializers.RowSerializer;

public class SerializerPack implements OkaeriSerdesPack {
    @Override
    public void register(SerdesRegistry registry) {
        registry.register(new RowSerializer());
    }
}
