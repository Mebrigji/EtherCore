package net.saidora.api.factory;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;

import java.util.function.Consumer;

public class ConfigurationFactory {

    public <T extends OkaeriConfig> T createDefault(Class<T> type, String path){
        return ConfigManager.create(type, config -> {
            config.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            config.withBindFile(path);
            config.saveDefaults();
            config.load(true);
        });
    }

    public <T extends OkaeriConfig> T create(Class<T> type, String path, Consumer<OkaeriConfig> consumer){
        return ConfigManager.create(type, config -> {
            config.withBindFile(path);
            consumer.accept(config);
        });
    }

}
