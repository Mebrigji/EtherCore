package net.saidora.api.listeners;

import net.saidora.api.builders.InventoryBuilder;
import net.saidora.api.extension.PlayerExtension;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryBuilderListener implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void handleInventoryClickEvent(InventoryClickEvent event){

        PlayerExtension playerExtension = PlayerExtension.getPlayerExtend((Player) event.getWhoClicked());
        if(playerExtension == null) return;

        InventoryBuilder builder = playerExtension.getPersistentDataObject("inventory", InventoryBuilder.class);
        if(builder == null || !event.getInventory().equals(builder.getInventory()) || builder.getClickEventConsumer() == null) return;

        builder.getClickEventConsumer().accept(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleInventoryCloseEvent(InventoryCloseEvent event){
        PlayerExtension playerExtension = PlayerExtension.getPlayerExtend((Player) event.getPlayer());
        if(playerExtension == null) return;

        InventoryBuilder builder = playerExtension.getPersistentDataObject("inventory", InventoryBuilder.class);
        if(builder == null || !builder.getInventory().equals(event.getInventory()) || builder.getCloseEventConsumer() == null) return;

        builder.getCloseEventConsumer().accept(event);
    }

}
