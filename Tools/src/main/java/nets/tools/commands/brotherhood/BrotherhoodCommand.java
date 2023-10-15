package nets.tools.commands.brotherhood;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.builders.InventoryBuilder;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.manager.BrotherhoodManager;
import nets.tools.manager.UserManager;
import nets.tools.model.Brotherhood;
import nets.tools.model.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
                            } else if (tag.length() <= 1) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_TAG_TOO_SHORT).send(player);
                            } else if (tag.length() > 8) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_TAG_TOO_LONG).send(player);
                            } else if (!pattern.matcher(name).matches()) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_NAME_INVALID).send(player);
                            } else if (name.length() < 5) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_NAME_TOO_SHORT).send(player);
                            } else if (name.length() > 30) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_NAME_TOO_LONG).send(player);
                            } else if(BrotherhoodManager.getInstance().getBrotherhoodMap().containsKey(tag)) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_TAG_EXISTS).send(player);
                            } else {
                                Map<ItemStack, Integer> missingItems = hasRequiredItems(player);
                                if(missingItems.isEmpty()) {
                                    player.getInventory().removeItem(requiredItems().toArray(ItemStack[]::new));
                                    Brotherhood brotherhood = BrotherhoodManager.getInstance().createBrotherhood(player, tag, name);
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Stworzono bractwo.").send(player);
                                } else {
                                    var ref = new Object() {
                                        Component missingItemsComponent = Component.text("");
                                        int id = 0;
                                    };
                                    missingItems.forEach((itemStack, integer) -> ref.missingItemsComponent = ref.missingItemsComponent.append(ComponentHelper.asComponent(ref.id++ >= missingItems.size()-1 ? "&f<item-name> &cx<amount>" : "&f<item-name> &cx<amount>&7, ",
                                            Placeholder.component("item-name", itemStack.hasItemMeta() ?
                                                    itemStack.getItemMeta().hasDisplayName() ?
                                                            itemStack.getItemMeta().displayName() : ComponentHelper.asComponent(itemStack.getI18NDisplayName()) : ComponentHelper.asComponent(itemStack.getI18NDisplayName())),
                                            Placeholder.parsed("amount", String.valueOf(integer))).hoverEvent(itemStack.asHoverEvent())));
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_CREATE_MISSING_ITEMS, Placeholder.component("missing-items", ref.missingItemsComponent)).send(player);
                                }
                            }
                        })
                ).then(new LiteralArgument("sojusz")
                        .combineWith(new StringArgument("tag"))
                        .executesPlayer((player, commandArguments) -> {
                            String tag = (String) commandArguments.get("tag");
                            Brotherhood brotherhood = BrotherhoodManager.getInstance().getBrotherhoodMap().get(tag);
                            if(brotherhood == null){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Podane bractwo nie istnieje.").send(player);
                                return;
                            }
                            UserManager.getInstance().getUser(player).ifPresent(user -> {
                                user.getBrotherhood().ifPresentOrElse(brotherhood1 -> {
                                    brotherhood.getOwner().flatMap(User::getPlayer).ifPresentOrElse(p -> {
                                        brotherhood.addAlly(brotherhood1);
                                        brotherhood1.addAlly(brotherhood);
                                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Utworzono sojusz pomiędzy bractwami <first> <-> <second>", Placeholder.parsed("first", brotherhood.tag()), Placeholder.parsed("second", brotherhood1.tag())).sendMany(player, p);
                                    }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Lider bractwa musi być aktywny.").send(player));
                                }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Nie należysz do żadnego bractwa.").send(player));
                            });
                        })
                ).then(new LiteralArgument("wymagania")
                        .executesPlayer((player, commandArguments) -> {
                            int size = requiredItems().size();
                            InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(null, Math.max(9, (int) Math.ceil((double) size / 9) * 9), ComponentHelper.asComponent("Wymagane przedmioty")));
                            for (ItemStack requirement : requiredItems()) {
                                inventoryBuilder.addItem(requirement.clone(), itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("  <!italic>&fᴡʏᴍᴀɢᴀɴᴀ ɪʟᴏsᴄ: <#78BADE>" + requirement.getAmount())));
                            }
                            inventoryBuilder.click(event -> event.setCancelled(true));
                            inventoryBuilder.open(player);
                        })
                ).then(new LiteralArgument("usun")
                        .combineWith(new StringArgument("tag"))
                        .executesPlayer((player, commandArguments) -> {
                            String tag = (String) commandArguments.get("tag");
                            Brotherhood brotherhood = BrotherhoodManager.getInstance().getBrotherhoodMap().get(tag);
                            if(brotherhood == null){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Podane bractwo nie istnieje").send(player);
                                return;
                            }
                            UserManager.getInstance().getUser(player).ifPresent(user -> {
                                Brotherhood userBrotherhood = user.getBrotherhood().orElse(null);
                                if(userBrotherhood == null || !userBrotherhood.owner().equals(brotherhood.owner())){
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Nie jesteś mistrzem bractwa.").send(player);
                                    return;
                                }
                                user.setBrotherhood(null);
                                BrotherhoodManager.getInstance().getBrotherhoodMap().remove(tag);
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Bractwo zostało rozwiązane.\nZwrócono 30% wymaganych przedmiotów do założenia bractwa. ").send(player);
                                requiredItems().forEach(itemStack -> {
                                    int amount = itemStack.getAmount();
                                    itemStack.setAmount((int) (amount * 0.3));
                                    player.getInventory().addItem(itemStack).forEach((integer, itemStack1) -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack1));
                                });
                            });
                        })
                )
                .executes((sender, commandArguments) -> {
                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getBrotherhoodConfiguration().COMMAND_HELP).send(sender);
                }).register();
    }

    private Map<ItemStack, Integer> hasRequiredItems(Player player){
        Map<ItemStack, Integer> missingItems = new LinkedHashMap<>();
        for (ItemStack itemStack : requiredItems()) {
            int amount = getAmountOfType(itemStack.getType(), player);
            if(amount >= itemStack.getAmount()) continue;
            missingItems.put(itemStack, itemStack.getAmount() - amount);
        }
        return missingItems;
    }

    private int getAmountOfType(Material material, Player player){
        int amount = 0;
        for (ItemStack content : player.getInventory().getContents()) {
            if(content == null || !content.getType().equals(material)) continue;
            amount += content.getAmount();
        }
        return amount;
    }

    private List<ItemStack> requiredItems(){
        List<ItemStack> clone = new ArrayList<>();
        Main.getInstance().getBrotherhoodConfiguration().REQUIREMENTS.forEach(itemStack -> clone.add(itemStack.clone()));
        return clone;
    }

}
