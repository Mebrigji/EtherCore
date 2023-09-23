package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.builders.InventoryBuilder;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.helpers.ItemHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.manager.UserManager;
import nets.tools.model.PlayerInventory;
import nets.tools.model.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventorySeeCommand {

    public void register(){
        new CommandAPICommand("invsee")
                .withPermission("ethercraft.command.admin.invsee")
                .withArguments(new OfflinePlayerArgument("target"))
                .executesPlayer((player, commandArguments) -> {
                    OfflinePlayer offlinePlayer = (OfflinePlayer) commandArguments.get("target");
                    if (offlinePlayer == null || offlinePlayer.getName() == null) {
                        ComponentHelper.futureComponent("&c[✖] &7Użytkownik o tej nazwie nie został odnaleziony w bazie danych").thenAccept(player::sendMessage);
                        return;
                    }
                    UserManager.getInstance().find(offlinePlayer.getName()).ifPresentOrElse(user -> {
                        user.getPlayer().ifPresentOrElse(online -> {
                            InventoryBuilder inventoryBuilder = new InventoryBuilder(online.getInventory());

                            inventoryBuilder.click(event -> {
                                if(!player.hasPermission("ethercraft.command.admin.invsee.intraction"))
                                    event.setCancelled(true);
                            });

                            inventoryBuilder.open(player);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_INVSEE, Placeholder.parsed("player", user.getNickName())).send(player);
                        }, () -> {
                            openContentsInventory(player, user);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_INVSEE, Placeholder.parsed("player", user.getNickName())).send(player);
                        });
                    }, () -> ComponentHelper.futureComponent("&c[✖] &7Użytkownik o tej nazwie nie został odnaleziony w bazie danych").thenAccept(player::sendMessage));
                }).register();
    }

    private int getSlot(int actual, List<Integer> blocked){
        if(blocked.contains(actual)) return getSlot(actual+1, blocked);
        else return actual;
    }

    private void openContentsInventory(Player player, User user){
        PlayerInventory playerInventory = user.getPlayerInventory();

        InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(null, 6*9, Component.text("Podgląd ekwipunku")));
        List<Integer> ignored_slots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);

        inventoryBuilder.setItem(ignored_slots, itemStack -> itemStack.setType(Material.GRAY_STAINED_GLASS_PANE), itemMeta -> itemMeta.displayName(Component.empty()));

        int slot = 9;
        for (ItemStack content : playerInventory.getContents()) {
            slot = getSlot(slot, ignored_slots);
            if(content == null) continue;
            inventoryBuilder.setItem(slot, content);
        }

        inventoryBuilder.setItem(49, itemStack -> itemStack.setType(Material.DIAMOND_HELMET), itemMeta -> {
            itemMeta.displayName(ComponentHelper.asComponent("<gradient:#D88F06:#F0C471>⚔ ᴜᴢʙʀᴏᴊᴇɴɪᴇ</gradient>"));
            itemMeta.lore(ComponentHelper.asComponents(" &f<!italic>Kliknij aby przejść do uzbrojenia gracza."));
        });

        inventoryBuilder.click(event -> {
            if(!player.hasPermission("ethercraft.command.admin.invsee.intraction")) {
                event.setCancelled(true);
                return;
            }
            if(event.getClickedInventory() != null && event.getClickedInventory().equals(player.getInventory())) return;
            if(ignored_slots.contains(event.getSlot())) event.setCancelled(true);
            if(event.getSlot() == 49){
                playerInventory.getContents().clear();
                for (int i = 0; i < event.getInventory().getSize(); i++) {
                    if (ignored_slots.contains(i)) continue;
                    ItemStack itemStack = event.getInventory().getItem(i);
                    playerInventory.getContents().add(itemStack);
                }
                user.add();
                openArmorInventory(player, user);
            }
        });

        inventoryBuilder.close(event -> {
            playerInventory.getContents().clear();
            for (int i = 0; i < event.getInventory().getSize(); i++) {
                if (ignored_slots.contains(i)) continue;
                ItemStack itemStack = event.getInventory().getItem(i);
                playerInventory.getContents().add(itemStack);
            }
            user.add();
        });
        inventoryBuilder.open(player);
    }

    private void openArmorInventory(Player player, User user){
        PlayerInventory playerInventory = user.getPlayerInventory();

        InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(null, 4*9, Component.text("Podgląd ekwipunku")));
        List<Integer> ignored_slots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 14, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35);


        inventoryBuilder.setItem(ignored_slots, itemStack -> itemStack.setType(Material.GRAY_STAINED_GLASS_PANE), itemMeta -> itemMeta.displayName(Component.empty()));

        if(playerInventory.getHelmet() != null) inventoryBuilder.setItem(10, playerInventory.getHelmet());
        if(playerInventory.getChestPlate() != null) inventoryBuilder.setItem(11, playerInventory.getChestPlate());
        if(playerInventory.getLeggings() != null) inventoryBuilder.setItem(12, playerInventory.getLeggings());
        if(playerInventory.getBoots() != null) inventoryBuilder.setItem(13, playerInventory.getBoots());

        var ref = new Object() {
            boolean blocked = true;
        };

        if(playerInventory.getMainHand() != null) inventoryBuilder.setItem(15, playerInventory.getMainHand());
        else {
            ref.blocked = false;
            inventoryBuilder.setItem(15, ItemHelper.edit(new ItemStack(Material.BARRIER)).getItemStack(), itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<!italic><gradient:#D50808:#F26A6A>ᴢᴍɪᴀɴᴀ ᴛᴇɢᴏ ɪᴛᴇᴍᴜ ᴢᴀʙʟᴏᴋᴏᴡᴀɴᴀ</gradient>")));
        };

        if(playerInventory.getSecondHand() != null) inventoryBuilder.setItem(16, playerInventory.getSecondHand());

        inventoryBuilder.setItem(19, itemStack -> itemStack.setType(Material.YELLOW_STAINED_GLASS_PANE),
                itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<gradient:#DB6903:#EFAE75>ᴍɪᴇᴊsᴄᴇ ɴᴀ ʜᴇʟᴍ</gradient>")));

        inventoryBuilder.setItem(20, itemStack -> itemStack.setType(Material.YELLOW_STAINED_GLASS_PANE),
                itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<gradient:#DB6903:#EFAE75>ᴍɪᴇᴊsᴄᴇ ɴᴀ ɴᴀᴘɪᴇʀsɴɪᴋ</gradient>")));

        inventoryBuilder.setItem(21, itemStack -> itemStack.setType(Material.YELLOW_STAINED_GLASS_PANE),
                itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<gradient:#DB6903:#EFAE75>ᴍɪᴇᴊsᴄᴇ ɴᴀ sᴘᴏᴅɴɪᴇ</gradient>")));

        inventoryBuilder.setItem(22, itemStack -> itemStack.setType(Material.YELLOW_STAINED_GLASS_PANE),
                itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<gradient:#DB6903:#EFAE75>ᴍɪᴇᴊsᴄᴇ ɴᴀ ʙᴜᴛʏ</gradient>")));

        inventoryBuilder.setItem(24, itemStack -> itemStack.setType(Material.YELLOW_STAINED_GLASS_PANE),
                itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<gradient:#DB6903:#EFAE75>ᴍɪᴇᴊsᴄᴇ ᴡ ᴘɪᴇʀᴡsᴢᴇᴊ ʀᴇᴄᴇ</gradient>")));

        inventoryBuilder.setItem(25, itemStack -> itemStack.setType(Material.YELLOW_STAINED_GLASS_PANE),
                itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<gradient:#DB6903:#EFAE75>ᴍɪᴇᴊsᴄᴇ ᴡ ᴅʀᴜɢɪᴇᴊ ʀᴇᴄᴇ</gradient>")));

        inventoryBuilder.setItem(31, itemStack -> itemStack.setType(Material.CHEST), itemMeta -> {
            itemMeta.displayName(ComponentHelper.asComponent("<gradient:#D88F06:#F0C471>⛏ ɪɴᴡᴇɴᴛᴀʀᴢ</gradient>"));
            itemMeta.lore(ComponentHelper.asComponents(" &f<!italic>Kliknij aby przejść do inwentarzu gracza."));
        });

        inventoryBuilder.click(event -> {
            if(!player.hasPermission("ethercraft.command.admin.invsee.intraction")) {
                event.setCancelled(true);
                return;
            }

            if(event.getClickedInventory() != null && event.getClickedInventory().equals(player.getInventory())) return;
            if(ignored_slots.contains(event.getSlot()) || event.getSlot() == 15) event.setCancelled(true);
            if(event.getSlot() == 31){
                playerInventory.setHelmet(null);
                playerInventory.setChestPlate(null);
                playerInventory.setLeggings(null);
                playerInventory.setBoots(null);
                playerInventory.setMainHand(null);
                playerInventory.setSecondHand(null);
                int[] slots = new int[]{10, 11, 12, 13, 16};
                for (int slot : slots) {

                    ItemStack itemStack = event.getInventory().getItem(slot);
                    if (itemStack == null) continue;

                    if (slot == 10) playerInventory.setHelmet(itemStack);
                    else if (slot == 11) playerInventory.setChestPlate(itemStack);
                    else if (slot == 12) playerInventory.setLeggings(itemStack);
                    else if (slot == 13) playerInventory.setBoots(itemStack);
                    else playerInventory.setSecondHand(itemStack);

                }
                user.add();
                openContentsInventory(player, user);
            }
        });

        inventoryBuilder.close(event -> {
            playerInventory.setHelmet(null);
            playerInventory.setChestPlate(null);
            playerInventory.setLeggings(null);
            playerInventory.setBoots(null);
            playerInventory.setMainHand(null);
            playerInventory.setSecondHand(null);
            int[] slots = new int[]{10, 11, 12, 13, 15, 16};
            for (int slot : slots) {

                ItemStack itemStack = event.getInventory().getItem(slot);
                if (itemStack == null) continue;

                if (slot == 10) playerInventory.setHelmet(itemStack);
                else if (slot == 11) playerInventory.setChestPlate(itemStack);
                else if (slot == 12) playerInventory.setLeggings(itemStack);
                else if (slot == 13) playerInventory.setBoots(itemStack);
                else if (slot == 15) {
                    if(!ref.blocked) playerInventory.setMainHand(itemStack);
                }
                else playerInventory.setSecondHand(itemStack);

            }
            user.add();
        });
        inventoryBuilder.open(player);
    }

}
