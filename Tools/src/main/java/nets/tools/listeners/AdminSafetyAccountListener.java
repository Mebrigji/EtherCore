package nets.tools.listeners;

import net.saidora.api.events.EventBuilder;
import net.saidora.api.events.list.PlayerInjectExtensionEvent;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class AdminSafetyAccountListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerCommandEvent(PlayerCommandPreprocessEvent event){
        PlayerExtension.getPlayerExtend(event.getPlayer(), playerExtension -> {
            if(playerExtension.containsPersistentDataObject("accountBlocked")){
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
        PlayerExtension.getPlayerExtend(event.getPlayer(), playerExtension -> {
            if(playerExtension.containsPersistentDataObject("accountBlocked")){
                event.setCancelled(true);
                if(event.getMessage().equals(String.valueOf(Main.getInstance().getSafety_pin()))){
                    playerExtension.removePersistentDataObject("accountBlocked");
                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "<red>Konto zostało odblokowane.").send(event.getPlayer());
                } else NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "<red>Konto jest zablokowane").send(event.getPlayer());
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerMoveEvent(PlayerMoveEvent event){
        PlayerExtension.getPlayerExtend(event.getPlayer(), playerExtension -> {
            if(playerExtension.containsPersistentDataObject("accountBlocked")){
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerTeleportEvent(PlayerTeleportEvent event){
        PlayerExtension.getPlayerExtend(event.getPlayer(), playerExtension -> {
            if(playerExtension.containsPersistentDataObject("accountBlocked")){
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerInventoryClickEvent(InventoryClickEvent event){
        PlayerExtension.getPlayerExtend((Player) event.getWhoClicked(), playerExtension -> {
            if(playerExtension.containsPersistentDataObject("accountBlocked")){
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerDropItemEvent(PlayerDropItemEvent event){
        PlayerExtension.getPlayerExtend(event.getPlayer(), playerExtension -> {
            if(playerExtension.containsPersistentDataObject("accountBlocked")){
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerDamageEvent(EntityDamageEvent event){
        if(event.getEntity() instanceof Player player){
            PlayerExtension.getPlayerExtend(player, playerExtension -> {
                if(playerExtension.containsPersistentDataObject("accountBlocked")){
                    event.setCancelled(true);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerProjectileStartEvent(ProjectileLaunchEvent event){
        if(event.getEntity() instanceof Player player){
            PlayerExtension.getPlayerExtend(player, playerExtension -> {
                if(playerExtension.containsPersistentDataObject("accountBlocked")){
                    event.setCancelled(true);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleMonsterStartFocusEvent(EntityTargetEvent event){
        if(event.getTarget() instanceof Player player){
            PlayerExtension.getPlayerExtend(player, playerExtension -> {
                if(playerExtension.containsPersistentDataObject("accountBlocked")){
                    event.setCancelled(true);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInteractEvent(PlayerInteractEvent event){
        PlayerExtension.getPlayerExtend(event.getPlayer(), playerExtension -> {
            if(playerExtension.containsPersistentDataObject("accountBlocked")){
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleHealthRegainEvent(EntityRegainHealthEvent event){
        if(event.getEntity() instanceof Player player){
            PlayerExtension.getPlayerExtend(player, playerExtension -> {
                if(playerExtension.containsPersistentDataObject("accountBlocked")){
                    event.setCancelled(true);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleHungerEvent(EntityExhaustionEvent event){
        if(event.getEntity() instanceof Player player){
            PlayerExtension.getPlayerExtend(player, playerExtension -> {
                if(playerExtension.containsPersistentDataObject("accountBlocked")){
                    event.setCancelled(true);
                }
            });
        }
    }

    public void setupEvent(){
        new EventBuilder<>(PlayerInjectExtensionEvent.class, event -> {
            Player player = event.getPlayer();
            PlayerExtension extension = event.getExtension();

            if(player.hasPermission("ethercraft.admin")) extension.addPersistentDataObject("accountBlocked", 1);
        });
    }

}
