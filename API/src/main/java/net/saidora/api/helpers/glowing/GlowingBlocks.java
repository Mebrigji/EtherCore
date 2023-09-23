package net.saidora.api.helpers.glowing;

import net.saidora.api.API;
import net.saidora.api.extension.PlayerExtension;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GlowingBlocks {

    private static final Map<Location, Shulker> glowingBlocks = new HashMap<>();

    public static Consumer<BlockBreakEvent> handlePlayerBlockEvent(){
        return blockBreakEvent -> {
            Shulker shulker = glowingBlocks.get(blockBreakEvent.getBlock().getLocation());
            if(shulker != null) shulker.remove();
        };
    }

    public static void sendGlowingBlock(Player p, Location loc, long lifetime){
        PlayerExtension.getPlayerExtend(p, playerExtension -> {
            Bukkit.getScheduler().scheduleSyncDelayedTask(API.getInstance(), () -> {

                Shulker shulker = loc.getWorld().spawn(loc.clone().add(0.5, 0, 0.5), Shulker.class);
                shulker.setInvisible(true);
                shulker.setAI(false);
                shulker.setCollidable(false);
                shulker.setInvulnerable(true);

                glowingBlocks.put(loc, shulker);

                try {
                    API.getInstance().getEntities().setGlowing(shulker, p, ChatColor.WHITE);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(API.getInstance(), shulker::remove, lifetime + (long) ((Math.random() + 1) * 100));

            }, (long) ((Math.random() + 1) * 40));
        });
    }

}
