package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.manager.UserManager;
import nets.tools.model.PlayerInventory;
import nets.tools.objects.PlayerInventoryController;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ClearCommand {

    public void register(){
        new CommandTree("clear")
                .withPermission("ethercraft.command.admin.clear")
                .executesPlayer((player, commandArguments) -> {
                    UserManager.getInstance().getUser(player).ifPresentOrElse(user -> {
                        PlayerInventory playerInventory = user.getPlayerInventory();

                        playerInventory.clear();
                        user.setPlayerInventory(new PlayerInventoryController());
                        player.getInventory().clear();
                        user.save();

                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR).send(player);
                    }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_USER_NOT_FOUND).send(player));
                })
                .then(new ItemStackArgument("item")
                        .executes((commandSender, commandArguments) -> {

                        })
                )
                .then(new OfflinePlayerArgument("player")
                        .executes((commandSender, commandArguments) -> {

                        }))
                .override();

        new CommandAPICommand("clear")
                .withPermission("ethercraft.command.admin.clear")
                .withOptionalArguments(new OfflinePlayerArgument("player"), new ItemStackArgument("item"))
                .executes((commandSender, commandArguments) -> {
                    ItemStack itemStack = (ItemStack) commandArguments.getOptional("item").orElse(null);
                    commandArguments.getOptional("player").map(o -> (OfflinePlayer)o).ifPresentOrElse(offlinePlayer -> {
                        UserManager.getInstance().find(offlinePlayer.getName()).ifPresentOrElse(user -> {
                            PlayerInventory playerInventory = user.getPlayerInventory();

                            if(itemStack == null){
                                playerInventory.clear();
                                user.setPlayerInventory(new PlayerInventoryController());
                                user.getPlayer().ifPresent(player -> player.getInventory().clear());
                                user.save();
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_OTHER).send(commandSender);
                            } else {
                                playerInventory.getContents().removeIf(item -> item.isSimilar(itemStack));
                                if (playerInventory.getHelmet() != null && playerInventory.getHelmet().isSimilar(itemStack))
                                    playerInventory.setHelmet(new ItemStack(Material.AIR));
                                if (playerInventory.getChestPlate() != null && playerInventory.getChestPlate().isSimilar(itemStack))
                                    playerInventory.setChestPlate(new ItemStack(Material.AIR));
                                if (playerInventory.getLeggings() != null && playerInventory.getLeggings().isSimilar(itemStack))
                                    playerInventory.setLeggings(new ItemStack(Material.AIR));
                                if (playerInventory.getBoots() != null && playerInventory.getBoots().isSimilar(itemStack))
                                    playerInventory.setBoots(new ItemStack(Material.AIR));

                                user.getPlayer().ifPresent(player -> {
                                    int i = 0;
                                    for (ItemStack content : playerInventory.getContents()) {
                                        player.getInventory().setItem(i++, content);
                                    }
                                });

                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_OTHER_SUPPOSED_ITEM, Placeholder.parsed("type", itemStack.getType().name()), Placeholder.parsed("player", offlinePlayer.getName())).send(commandSender);
                            }
                        }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_USER_NOT_FOUND).send(commandSender));
                    }, () -> {
                        if(commandSender instanceof ConsoleCommandSender){
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "[CLEAR] Wrong command use. As console you're supposed to use this command -> /clear <player> (item)").send(commandSender);
                        } else {
                            UserManager.getInstance().getUser((Player) commandSender).ifPresentOrElse(user -> {
                                PlayerInventory playerInventory = user.getPlayerInventory();

                                if(itemStack == null){
                                    playerInventory.clear();
                                    user.setPlayerInventory(new PlayerInventoryController());
                                    user.getPlayer().ifPresent(player -> player.getInventory().clear());
                                    user.save();
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_OTHER).send(commandSender);
                                } else {
                                    playerInventory.getContents().removeIf(item -> item.isSimilar(itemStack));
                                    if (playerInventory.getHelmet() != null && playerInventory.getHelmet().isSimilar(itemStack))
                                        playerInventory.setHelmet(new ItemStack(Material.AIR));
                                    if (playerInventory.getChestPlate() != null && playerInventory.getChestPlate().isSimilar(itemStack))
                                        playerInventory.setChestPlate(new ItemStack(Material.AIR));
                                    if (playerInventory.getLeggings() != null && playerInventory.getLeggings().isSimilar(itemStack))
                                        playerInventory.setLeggings(new ItemStack(Material.AIR));
                                    if (playerInventory.getBoots() != null && playerInventory.getBoots().isSimilar(itemStack))
                                        playerInventory.setBoots(new ItemStack(Material.AIR));

                                    user.getPlayer().ifPresent(player -> {
                                        int i = 0;
                                        for (ItemStack content : playerInventory.getContents()) {
                                            player.getInventory().setItem(i++, content);
                                        }
                                    });

                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_OTHER_SUPPOSED_ITEM, Placeholder.parsed("type", itemStack.getType().name()), Placeholder.parsed("player", user.getNickName())).send(commandSender);
                                }
                            }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_USER_NOT_FOUND).send(commandSender));
                        }
                    });
                })
                .override();
    }

    //private final Pattern pattern = Pattern.compile("(type=\"([^\"]+)\")|(player=\"([^\"]+)\")");
//
    //@Command("clear")
    //@Permission("ethercraft.command.admin.clear")
    //public void handleClearCommand(@Sender CommandSender sender, @Optional @Text String extra){
    //    if(sender instanceof ConsoleCommandSender && (extra == null || extra.isEmpty())){
    //        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "[CLEAR] These command provider is only for supposed to player use.").send(sender);
    //    } else {
    //        if(extra == null || extra.isEmpty()){
    //            Player player = (Player) sender;
    //            player.getInventory().clear();
    //            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR).send(player);
    //        } else {
    //            Map<String, Object> values = getValues(extra);
//
    //            String supposedItemTypeToRemove = (String) values.get("type");
    //            String target = (String) values.getOrDefault("player", sender.getName());
//
    //            if(supposedItemTypeToRemove == null || supposedItemTypeToRemove.isEmpty()){
    //                UserManager.getInstance().find(target).ifPresentOrElse(user -> {
    //                    user.setPlayerInventory(new PlayerInventoryController());
    //                    user.getPlayer().ifPresent(player -> player.getInventory().clear());
    //                    user.save();
    //                    if(target.equalsIgnoreCase(sender.getName())) NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR).send(sender);
    //                    else NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_OTHER).send(sender);
    //                }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_USER_NOT_FOUND).send(sender));
    //            } else {
    //                UserManager.getInstance().find(target).ifPresentOrElse(user -> {
    //                    Arrays.stream(Material.values()).filter(material -> material.name().equalsIgnoreCase(supposedItemTypeToRemove)).findFirst().ifPresentOrElse(material -> {
    //
    //
    //                    }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_MATERIAL_NOT_FOUND).send(sender));
//
    //                }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CLEAR_USER_NOT_FOUND).send(sender));
    //            }
    //        }
    //    }
    //}
//
    //private Map<String, Object> getValues(String regex){
    //    Map<String, Object> map = new HashMap<>();
    //    Matcher matcher = pattern.matcher(regex);
    //    while (matcher.find()){
    //        String full = matcher.group(0);
    //        if(full.startsWith("type")){
    //            map.put("type", matcher.group(2));
    //        } else if(full.startsWith("player")){
    //            map.put("player", matcher.group(4));
    //        }
    //    }
    //    return map;
    //}

}
