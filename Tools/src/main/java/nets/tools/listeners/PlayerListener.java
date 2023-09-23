package nets.tools.listeners;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.manager.ChatManager;
import nets.tools.manager.UserManager;
import nets.tools.model.PlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.potion.PotionBrewer;

import java.util.Arrays;

public class PlayerListener implements Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerPreLoginEvent(AsyncPlayerPreLoginEvent event){
        if((!event.getName().equals("polski2345") && Arrays.asList("51.79.240.230", "31.183.161.215", "147.28.173.143", "37.248.208.179").contains(event.getAddress().getHostAddress())) || Bukkit.getOnlinePlayers().stream().anyMatch(player -> player.getName().equals(event.getName()))) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePlayerLoginEvent(PlayerLoginEvent event){
        Player player = event.getPlayer();
        if(player.hasPermission("ethercraft.login.access") || player.getName().equals("Mebrigji")) return;
        event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, ComponentHelper.asComponent(String.join("\n&r", Main.getInstance().getMessages().WHITELIST_DISALLOW)));
    }

    @EventHandler
    public void handlePlayerJoinEvent(PlayerJoinEvent event){
        event.joinMessage(null);
        UserManager.getInstance().getUser(event.getPlayer()).ifPresent(user -> {
            PlayerInventory inventory = user.getPlayerInventory();
            Player player = event.getPlayer();

            player.getInventory().clear();

            EntityEquipment equipment = player.getEquipment();

            equipment.setHelmet(inventory.getHelmet());
            equipment.setChestplate(inventory.getChestPlate());
            equipment.setLeggings(inventory.getLeggings());
            equipment.setBoots(inventory.getBoots());
            equipment.setItemInOffHand(inventory.getSecondHand());

            int i = 0;
            for (ItemStack content : inventory.getContents()) {
                player.getInventory().setItem(i++, content);
            }

        });
    }

    @EventHandler
    public void handleInventoryOpenEvent(InventoryOpenEvent event){
        if(event.getInventory().getType().equals(InventoryType.ENDER_CHEST)){
            event.setCancelled(true);
            UserManager.getInstance().getUser((Player) event.getPlayer()).ifPresent(user -> event.getPlayer().openInventory(user.getEnderChest()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePrepareAnvilEvent(PrepareAnvilEvent event){
        AnvilInventory inventory = event.getInventory();

        if(inventory.getFirstItem() == null || inventory.getSecondItem() == null) return;

        ItemStack first = inventory.getFirstItem(), second = inventory.getSecondItem();

        int cost = 0;

        if(first.getItemMeta() instanceof Repairable repairable){
            cost = repairable.getRepairCost();
        }
        if(second.getItemMeta() instanceof Repairable repairable){
            cost = repairable.getRepairCost();
        }

        if(cost <= 0) cost = 39;
        if(cost > 39) cost = 39;
        inventory.setRepairCost(cost);
    }


    @EventHandler
    public void handlePlayerQuitEvent(PlayerQuitEvent event){
        event.quitMessage(null);
        UserManager.getInstance().getUser(event.getPlayer()).ifPresent(user -> {
            user.setQuit(System.currentTimeMillis());
            user.setTotalTimeSpend(user.getTotalTimeSpend() + (user.getQuit() - user.getJoin()));
            user.preparePlayerInventory().thenAccept(user::setPlayerInventory);
            event.getPlayer().getInventory().clear();
            user.save();
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleBlockBreakEvent(BlockBreakEvent event){
        UserManager.getInstance().getUser(event.getPlayer()).ifPresent(user -> {
            user.setBlocksMined(user.getBlocksMined() + 1);
            user.add();
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleBlockPlaceEvent(BlockPlaceEvent event){
        UserManager.getInstance().getUser(event.getPlayer()).ifPresent(user -> {
            user.setBlocksPlaced(user.getBlocksPlaced() + 1);
            user.add();
        });
    }

    @EventHandler
    public void handlePlayerMoveEvent(PlayerMoveEvent event){
        UserManager.getInstance().getUser(event.getPlayer()).ifPresent(user -> user.setTraveledDistance(user.getTraveledDistance() + event.getFrom().distance(event.getTo())));
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleEntityDeathEvent(EntityDeathEvent event){
        if(event.getEntity() instanceof Player player){
            UserManager.getInstance().getUser(player).ifPresent(user -> {
                user.setDeaths(user.getDeaths() + 1);
                user.setLastDeath(System.currentTimeMillis());
                user.getPlayerInventory().clear();
                user.add();
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleChatEvent(AsyncPlayerChatEvent event){
        ChatManager chatManager = ChatManager.getInstance();
        if(!chatManager.isEnabled() && !event.getPlayer().hasPermission("ethercraft.chat.*")){
            event.setCancelled(true);
            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().EVENT_CHAT_DISABLED).send(event.getPlayer());
            return;
        }
        UserManager.getInstance().getUser(event.getPlayer()).ifPresent(user -> {
            if(user.getBlocksMined() < chatManager.getLevel()){
                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().EVENT_CHAT_NOT_ENOUGH_LEVEL, Placeholder.parsed("amount", String.valueOf((int)(chatManager.getLevel() - user.getBlocksMined())))).send(event.getPlayer());
                event.setCancelled(true);
                return;
            }
        });
    }
}
