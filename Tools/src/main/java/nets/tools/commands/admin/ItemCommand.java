package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemCommand {

    public void register(){
        new CommandTree("item")
                .executes((commandSender, commandArguments) -> {
                    commandSender.sendMessage("""
                            /item rename <text/clear> - Set custom item name or remove custom name.
                            /item lore <lore/clear> - Set custom item lore or remove lore. # Type %nl% to create new line.
                            /item more <amount> - Change amount of item
                            /item enchant <enchantment> (level) - Add or remove enchantment to item (When level is not set, then enchantment will be removed)
                            /item combine <itemStack> - Combine item in hand with the new what was set""");
                }).then(new LiteralArgument("rename")
                        .combineWith(new GreedyStringArgument("text"))
                        .executesPlayer((player, commandArguments) -> {
                            String name = (String) commandArguments.get(0);
                            ItemStack itemStack = player.getEquipment().getItem(EquipmentSlot.HAND);

                            if(itemStack.getType().isAir()){
                                ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ITEM_NAME_AIR).thenAccept(player::sendMessage);
                                return;
                            }

                            ItemMeta meta = itemStack.getItemMeta();
                            assert name != null;
                            if(name.isEmpty() || name.equals("clear")) {
                                meta.displayName(null);
                                itemStack.setItemMeta(meta);
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ITEM_NAME_CLEAR).send(player);
                                return;
                            }
                            Component oldName = meta.hasDisplayName() ? meta.displayName() : Component.text("Doesn't have custom name");
                            ComponentHelper.futureComponent(name).thenAccept(component -> {
                                meta.displayName(component);
                                itemStack.setItemMeta(meta);
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ITEM_NAME,
                                        Placeholder.component("old_name", oldName),
                                        Placeholder.component("new_name", component)).send(player);
                            });
                        })
                ).then(new LiteralArgument("lore")
                        .combineWith(new GreedyStringArgument("lore"))
                        .executesPlayer((player, commandArguments) -> {
                            String lore = (String) commandArguments.get(0);
                            ItemStack itemStack = player.getEquipment().getItem(EquipmentSlot.HAND);

                            if(itemStack.getType().isAir()){
                                ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ITEM_LORE_AIR).thenAccept(player::sendMessage);
                                return;
                            }
                            ItemMeta meta = itemStack.getItemMeta();
                            if(lore.isEmpty() || lore.equals("clear")){
                                meta.lore(new ArrayList<>());
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ITEM_LORE_CLEAR).send(player);
                            } else {
                                String[] args = lore.split("%nl%");
                                meta.lore(ComponentHelper.asComponents(args));
                                itemStack.setItemMeta(meta);
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ITEM_LORE).send(player);
                            }
                        })
                ).then(new LiteralArgument("enchant")
                        .combineWith(new EnchantmentArgument("enchantmentType"))
                        .executesPlayer((player, commandArguments) -> {
                            Enchantment enchantment = (Enchantment) commandArguments.get(0);
                            assert enchantment != null;
                            ItemStack itemStack = player.getEquipment().getItem(EquipmentSlot.HAND);

                            if (itemStack.getType().isAir()) {
                                ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ENCHANT_AIR).thenAccept(player::sendMessage);
                                return;
                            }
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if(itemMeta.getEnchantLevel(enchantment) == 0){
                                ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ENCHANT_NOT_FOUND).thenAccept(player::sendMessage);
                                return;
                            }
                            itemMeta.removeEnchant(enchantment);
                            itemStack.setItemMeta(itemMeta);NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ENCHANT_REMOVE, Placeholder.parsed("enchantment", enchantment.getKey().getKey()), Placeholder.parsed("level", String.valueOf(itemMeta.getEnchantLevel(enchantment)))).send(player);
                        }).then(new IntegerArgument("level").executesPlayer((player, commandArguments) -> {
                            Enchantment enchantment = (Enchantment) commandArguments.get(0);
                            int level = (int) commandArguments.get(1);
                            assert enchantment != null;
                            ItemStack itemStack = player.getEquipment().getItem(EquipmentSlot.HAND);

                            if (itemStack.getType().isAir()) {
                                ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ENCHANT_AIR).thenAccept(player::sendMessage);
                                return;
                            }

                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if(itemMeta.getEnchantLevel(enchantment) == 0){
                                itemMeta.addEnchant(enchantment, level, true);
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ENCHANT_APPLY, Placeholder.parsed("enchantment", enchantment.getKey().getKey()), Placeholder.parsed("level", String.valueOf(level))).send(player);
                            } else if(itemMeta.getEnchantLevel(enchantment) == level){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ENCHANT_ALREADY, Placeholder.parsed("enchantment", enchantment.getKey().getKey()), Placeholder.parsed("level", String.valueOf(itemMeta.getEnchantLevel(enchantment)))).send(player);
                            } else {
                                itemMeta.addEnchant(enchantment, level, true);
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ENCHANT_EDIT, Placeholder.parsed("enchantment", enchantment.getKey().getKey()), Placeholder.parsed("new_level", String.valueOf(level)), Placeholder.parsed("old_level", String.valueOf(itemMeta.getEnchantLevel(enchantment)))).send(player);
                            }
                            itemStack.setItemMeta(itemMeta);
                        }))
                ).then(new LiteralArgument("more")
                        .executesPlayer((player, commandArguments) -> {
                            ItemStack itemStack = player.getEquipment().getItem(EquipmentSlot.HAND);
                            int amount = 64;

                            if (itemStack.getType().isAir()) {
                                ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ITEM_MORE_AIR).thenAccept(player::sendMessage);
                                return;
                            }

                            if(itemStack.getAmount() < amount) ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ITEM_MORE_INCREASE, Placeholder.parsed("new", String.valueOf(amount)), Placeholder.parsed("old", String.valueOf(itemStack.getAmount()))).thenAccept(player::sendMessage);
                            else if(itemStack.getAmount() == amount) ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ITEM_MORE_EQUAL, Placeholder.parsed("amount", String.valueOf(amount))).thenAccept(player::sendMessage);
                            else ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ITEM_MORE_DECREASE, Placeholder.parsed("new", String.valueOf(amount)), Placeholder.parsed("old", String.valueOf(itemStack.getAmount()))).thenAccept(player::sendMessage);

                            itemStack.setAmount(amount);
                        }).then(new IntegerArgument("amount").executesPlayer((player, commandArguments) -> {
                            ItemStack itemStack = player.getEquipment().getItem(EquipmentSlot.HAND);
                            int amount = (int) commandArguments.get(0);

                            if (itemStack.getType().isAir()) {
                                ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ITEM_MORE_AIR).thenAccept(player::sendMessage);
                                return;
                            }

                            if(itemStack.getAmount() < amount) ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ITEM_MORE_INCREASE, Placeholder.parsed("new", String.valueOf(amount)), Placeholder.parsed("old", String.valueOf(itemStack.getAmount()))).thenAccept(player::sendMessage);
                            else if(itemStack.getAmount() == amount) ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ITEM_MORE_EQUAL, Placeholder.parsed("amount", String.valueOf(amount))).thenAccept(player::sendMessage);
                            else ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_ITEM_MORE_DECREASE, Placeholder.parsed("new", String.valueOf(amount)), Placeholder.parsed("old", String.valueOf(itemStack.getAmount()))).thenAccept(player::sendMessage);

                            itemStack.setAmount(amount);
                        }))
                )
                .then(new LiteralArgument("type")
                        .combineWith(new ItemStackArgument("itemStack"))
                        .executesPlayer((player, commandArguments) -> {

                        })
                )
                .override();
    }

}
