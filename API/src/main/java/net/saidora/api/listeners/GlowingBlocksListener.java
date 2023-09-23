package net.saidora.api.listeners;

import net.saidora.api.helpers.glowing.GlowingBlocks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class GlowingBlocksListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleBlockBreakEvent(BlockBreakEvent event){
        GlowingBlocks.handlePlayerBlockEvent().accept(event);
    }

}
