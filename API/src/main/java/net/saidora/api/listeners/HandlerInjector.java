package net.saidora.api.listeners;

import net.saidora.api.extension.PlayerExtension;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class HandlerInjector implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePlayerLoginEvent(PlayerLoginEvent event){
        PlayerExtension playerExtension = new PlayerExtension(event.getPlayer());
        playerExtension.inject();
    }
}
