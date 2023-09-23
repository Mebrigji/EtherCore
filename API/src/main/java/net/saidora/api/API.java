package net.saidora.api;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.saidora.api.builders.InventoryBuilder;
import net.saidora.api.events.EventBuilder;
import net.saidora.api.events.list.TaskEvent;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.helpers.glowing.GlowingEntities;
import net.saidora.api.listeners.GlowingBlocksListener;
import net.saidora.api.listeners.HandlerInjector;
import net.saidora.api.listeners.InventoryBuilderListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;

@Plugin(name = "Ether-API", version = "1.0.1.17")
@Author("Saidora")
@ApiVersion(ApiVersion.Target.v1_19)
@Dependency("NBTAPI")
@Description("[+] PlayerExtension#getExperience, PlayerExtension#addExperience, PlayerExtension#removeExperience")
public class API extends JavaPlugin {

    private static API instance;

    public static API getInstance() {
        return instance;
    }

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2);

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    private BukkitAudiences bukkitAudiences;

    public BukkitAudiences getBukkitAudiences() {
        return bukkitAudiences;
    }

    private GlowingEntities entities;

    public GlowingEntities getEntities() {
        return entities;
    }

    @Override
    public void onLoad() {
        instance = this;
        getLogger().log(Level.INFO, "Instance");
    }

    @Override
    public void onEnable() {
        this.bukkitAudiences = BukkitAudiences.create(this);

        entities = new GlowingEntities(this);

        getServer().getPluginManager().registerEvents(new HandlerInjector(), this);
        getServer().getPluginManager().registerEvents(new InventoryBuilderListener(), this);
        getServer().getPluginManager().registerEvents(new GlowingBlocksListener(), this);

        new PluginTask(100L, () -> {
            try {
                TaskEvent taskEvent = new TaskEvent(Bukkit.getOnlinePlayers());
                taskEvent.call();
                taskEvent.getPlayerList().forEach(player -> taskEvent.getExecute().forEach(playerConsumer -> playerConsumer.accept(player)));
            } catch (Exception e){
                e.printStackTrace();
            }
        }).setup();

        new EventBuilder<>(TaskEvent.class, taskEvent -> {
           taskEvent.execute(player -> PlayerExtension.getPlayerExtend(player, extension -> {
               InventoryBuilder inventoryBuilder = extension.getPersistentDataObject("inventory", InventoryBuilder.class);
               if(inventoryBuilder == null || inventoryBuilder.getInventory().getViewers().isEmpty() || inventoryBuilder.getUpdater() == null) return;
               inventoryBuilder.getUpdater().run();
           }));
        });
    }
}
