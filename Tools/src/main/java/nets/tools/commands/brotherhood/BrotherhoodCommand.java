package nets.tools.commands.brotherhood;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.saidora.api.builders.InventoryBuilder;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.manager.BrotherhoodManager;
import nets.tools.model.Brotherhood;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Pattern;

public class BrotherhoodCommand {

    Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");

    public void register() {
        new CommandTree("brotherhood")
                .withAliases("bh", "g", "guild", "gildia", "bractwo")
                .then(new LiteralArgument("stworz")
                        .combineWith(new StringArgument("tag"), new StringArgument("nazwa"))
                        .executesPlayer((player, commandArguments) -> {
                            String tag = (String) commandArguments.get(0);
                            String name = (String) commandArguments.get(1);
                            if (!pattern.matcher(tag).matches()) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_TAG_INVALID).send(player);
                            } else if (tag.length() < 1) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_TAG_TOO_SHORT).send(player);
                            } else if (tag.length() > 8) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_TAG_TOO_LONG).send(player);
                            } else if (!pattern.matcher(name).matches()) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_NAME_INVALID).send(player);
                            } else if (name.length() < 5) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_NAME_TOO_SHORT).send(player);
                            } else if (name.length() > 30) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_NAME_TOO_LONG).send(player);
                            } else {
                                Brotherhood brotherhood = BrotherhoodManager.getInstance().createBrotherhood(player, tag, name);
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Stworzono bractwo.").send(player);
                            }
                        })
                ).then(new LiteralArgument("wymagania")
                        .executesPlayer((player, commandArguments) -> {
                            int size = Main.getInstance().getBrotherhoodConfiguration().REQUIREMENTS.size();
                            InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(null, Math.max(9, (int) Math.ceil((double) size / 9) * 9), ComponentHelper.asComponent("Wymagane przedmioty")));
                            for (ItemStack requirement : Main.getInstance().getBrotherhoodConfiguration().REQUIREMENTS) {
                                inventoryBuilder.addItem(requirement.clone(), itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("  <!italic>&fᴡʏᴍᴀɢᴀɴᴀ ɪʟᴏsᴄ: <#78BADE>" + requirement.getAmount())));
                            }
                            inventoryBuilder.click(event -> event.setCancelled(true));
                            inventoryBuilder.open(player);
                        })
                ).then(new LiteralArgument("usun")
                        .combineWith(new StringArgument("tag"))
                        .executesPlayer((player, commandArguments) -> {
                            player.sendMessage("Podczas tworzenia..");
                        })
                )
                .executes((sender, commandArguments) -> {
                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_HELP).send(sender);
                }).register();
    }

}
