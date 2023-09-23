package nets.tools.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.builders.InventoryBuilder;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.helpers.TimeHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.manager.KitManager;
import nets.tools.manager.UserManager;
import nets.tools.model.Kit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AdminKitCommand {

    public void register(){
        new CommandTree("adminkit")
                .withAliases("akit", "kit", "zestaw", "zestawy")
                .executesPlayer((player, commandArguments) -> {
                    int size = KitManager.getInstance().getKitMap().size();
                    InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(null, Math.max(9, (int) Math.ceil((double) size / 9) * 9), Component.text("Zestawy")));
                    KitManager.getInstance().getKitMap().forEach((s, kit) -> {
                        inventoryBuilder.setItem(kit.slot(), kit.icon(), itemMeta -> {
                            if(kit.displayName() != null && !kit.displayName().isEmpty()) itemMeta.displayName(ComponentHelper.asComponent(kit.displayName()));
                            if(kit.lore() != null && !kit.lore().isEmpty()) itemMeta.lore(ComponentHelper.asComponents(kit.lore()));
                        });
                    });
                    inventoryBuilder.click(event -> event.setCancelled(true));
                    inventoryBuilder.open(player);
                })
                .then(new LiteralArgument("create")
                        .combineWith(new StringArgument("id"), new ItemStackArgument("icon"), new IntegerArgument("slot"))
                        .executes((commandSender, commandArguments) -> {
                            String id = (String) commandArguments.get("id");
                            if (KitManager.getInstance().getKitMap().containsKey(id)) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_CREATE_ALREADY, Placeholder.parsed("kit", id)).send(commandSender);
                                return;
                            }
                            ItemStack itemStack = (ItemStack) commandArguments.get("icon");
                            int slot = (int) commandArguments.get("slot");
                            Kit kit = KitManager.getInstance().createKit(id);
                            kit.icon(itemStack);
                            kit.slot(slot);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_CREATE, Placeholder.parsed("kit", id), Placeholder.parsed("slot", String.valueOf(slot)), Placeholder.component("icon", Component.text("[ITEM]").hoverEvent(itemStack.asHoverEvent()))).send(commandSender);
                        })
                )
                .then(new LiteralArgument("enable")
                        .then(new BooleanArgument("enable")
                                .then(new LiteralArgument("all")
                                        .executes((commandSender, commandArguments) -> {
                                            boolean enable = (boolean) commandArguments.get("enable");
                                            KitManager.getInstance().getKitMap().forEach((s, kit) -> {
                                                kit.enabled(enable);
                                            });
                                            if(enable) NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_ENABLE_ALL_ENABLED).send(commandSender);
                                            else NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_ENABLE_ALL_DISABLED).send(commandSender);
                                        })
                                )
                                .then(createKitArgument("kit")
                                        .executes((commandSender, commandArguments) -> {
                                            Kit kit = (Kit) commandArguments.get("kit");
                                            boolean enable = (boolean) commandArguments.get("enable");
                                            if(enable && kit.enabled()){
                                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_ENABLE_ALREADY_ENABLED, Placeholder.parsed("kit", kit.id())).send(commandSender);
                                            } else if(!enable && !kit.enabled()){
                                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_ENABLE_ALREADY_DISABLED, Placeholder.parsed("kit", kit.id())).send(commandSender);
                                            } else if(enable){
                                                kit.enabled(true);
                                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_ENABLE_ENABLED, Placeholder.parsed("kit", kit.id())).send(commandSender);
                                            } else {
                                                kit.enabled(false);
                                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_ENABLE_DISABLED, Placeholder.parsed("kit", kit.id())).send(commandSender);
                                            }
                                        })
                                )
                        )
                ).then(new LiteralArgument("delete")
                        .combineWith(createKitArgument("kit"))
                        .executes((commandSender, commandArguments) -> {
                            Kit kit = (Kit) commandArguments.get("kit");
                            KitManager.getInstance().getKitMap().remove(kit.id());
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_DELETE, Placeholder.parsed("kit", kit.id())).send(commandSender);
                        })
                )
                .then(new LiteralArgument("preview")
                        .combineWith(createKitArgument("kit"))
                        .executesPlayer((player, commandArguments) -> {
                            Kit kit = (Kit) commandArguments.get("kit");
                            InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(null, InventoryType.HOPPER, Component.text("Podgląd zestawu")));

                            inventoryBuilder.setItem(2, kit.icon(), itemMeta -> {
                                if(kit.displayName() != null && !kit.displayName().isEmpty()) itemMeta.displayName(ComponentHelper.asComponent(kit.displayName()));
                                if(kit.lore() != null && !kit.lore().isEmpty()) itemMeta.lore(ComponentHelper.asComponents(kit.lore()));
                            });

                            inventoryBuilder.click(event -> event.setCancelled(true));
                            inventoryBuilder.open(player);
                        }).then(new LiteralArgument("inventory")
                                .executesPlayer((player, commandArguments) -> {
                                    Kit kit = (Kit) commandArguments.get("kit");
                                    InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(null, Math.max(9, (int) Math.ceil((double) kit.items().size() / 9) * 9), Component.text("Podgląd itemów")));
                                    kit.items().forEach(inventoryBuilder::addItem);
                                    inventoryBuilder.click(event -> event.setCancelled(true));
                                    inventoryBuilder.open(player);
                                })
                        )
                )
                .then(new LiteralArgument("grant")
                        .combineWith(createKitArgument("kit"), new OfflinePlayerArgument("player"))
                        .executes((commandSender, commandArguments) -> {
                            OfflinePlayer offlinePlayer = (OfflinePlayer) commandArguments.get("player");
                            Kit kit = (Kit) commandArguments.get("kit");
                            UserManager.getInstance().find(offlinePlayer.getName()).ifPresentOrElse(user -> {
                                user.getPlayer().ifPresentOrElse(player -> {
                                    kit.items().forEach(itemStack -> player.getInventory().addItem(itemStack));
                                }, () -> {
                                    user.getPlayerInventory().getContents().addAll(kit.items());
                                });
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_GRANT, Placeholder.parsed("kit", kit.id()), Placeholder.parsed("player", user.getNickName()));
                            }, () -> {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_GRANT_USER_NOT_FOUND).send(commandSender);
                            });
                        })
                )
                .then(new LiteralArgument("edit")
                        .then(createKitArgument("kit")
                                .then(new LiteralArgument("permission")
                                        .then(new LiteralArgument("clear")
                                                .executes((commandSender, commandArguments) -> {
                                                    Kit kit = (Kit) commandArguments.get("kit");
                                                    kit.permission(null);
                                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_EDIT_PERMISSION_CLEAR, Placeholder.parsed("kit", kit.id())).send(commandSender);
                                                })
                                        )
                                        .then(new StringArgument("permission")
                                                .executes((commandSender, commandArguments) -> {
                                                    Kit kit = (Kit) commandArguments.get("kit");
                                                    String permission = (String) commandArguments.get("permission");
                                                    kit.permission(permission);
                                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_EDIT_PERMISSION_SET, Placeholder.parsed("permission", permission), Placeholder.parsed("kit", kit.id())).send(commandSender);
                                                })
                                        ))
                                .then(new LiteralArgument("items").executesPlayer((player, commandArguments) -> {
                                    Kit kit = (Kit) commandArguments.get("kit");

                                    InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(null, 5*9, Component.text("Edytowanie przedmiotów w zestawie.")));

                                    for (ItemStack item : kit.items()) {
                                        inventoryBuilder.addItem(item);
                                    }

                                    inventoryBuilder.close(event -> {
                                        kit.items().addAll(Arrays.stream(event.getInventory().getContents()).filter(Objects::nonNull).toList());

                                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_EDIT_ITEMS, Placeholder.parsed("items", String.valueOf(kit.items().size())), Placeholder.parsed("kit", kit.id())).send(player);
                                    });

                                    inventoryBuilder.open(player);
                                }))
                                .then(new LiteralArgument("delay")
                                        .then(new LiteralArgument("clear")
                                                .executes((commandSender, commandArguments) -> {
                                                    Kit kit = (Kit) commandArguments.get("kit");
                                                    kit.delay(0);
                                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_EDIT_DELAY_CLEAR, Placeholder.parsed("kit", kit.id())).send(commandSender);
                                                })
                                        )
                                        .then(new LongArgument("delay")
                                                .executes((commandSender, commandArguments) -> {
                                                    Kit kit = (Kit) commandArguments.get("kit");
                                                    long delay = (long) commandArguments.get("delay");
                                                    kit.delay(delay);
                                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_EDIT_DELAY_SET, Placeholder.parsed("kit", kit.id()), Placeholder.parsed("time", new TimeHelper(delay).parse())).send(commandSender);
                                                })
                                        ))
                                .then(new LiteralArgument("icon")
                                        .then(new ItemStackArgument("item").executes((commandSender, commandArguments) -> {
                                            Kit kit = (Kit) commandArguments.get("kit");
                                            ItemStack itemStack = (ItemStack) commandArguments.get("item");
                                            kit.icon(itemStack);
                                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_EDIT_ICON, Placeholder.parsed("kit", kit.id())).send(commandSender);
                                        }))
                                )
                                .then(new LiteralArgument("slot")
                                        .then(new IntegerArgument("slot").executes((commandSender, commandArguments) -> {
                                            Kit kit = (Kit) commandArguments.get("kit");
                                            int slot = (int) commandArguments.get("slot");
                                            kit.slot(slot);
                                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_EDIT_SLOT, Placeholder.parsed("slot", String.valueOf(slot)), Placeholder.parsed("kit", kit.id())).send(commandSender);
                                        }))
                                )
                                .then(new LiteralArgument("displayName")
                                        .then(new GreedyStringArgument("text").executes((commandSender, commandArguments) -> {
                                            Kit kit = (Kit) commandArguments.get("kit");
                                            String displayName = (String) commandArguments.get("text");
                                            kit.displayName(displayName);
                                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_EDIT_DISPLAY_NAME, Placeholder.parsed("kit", kit.id()), Placeholder.parsed("displayname", displayName)).send(commandSender);
                                        }))
                                )
                                .then(new LiteralArgument("lore")
                                        .then(new LiteralArgument("clear")
                                                .executes((commandSender, commandArguments) -> {
                                                    Kit kit = (Kit) commandArguments.get("kit");
                                                    kit.lore(new ArrayList<>());
                                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_EDIT_LORE_CLEAR, Placeholder.parsed("kit", kit.id())).send(commandSender);
                                                })
                                        )
                                        .then(new GreedyStringArgument("lore")
                                                .executes((commandSender, commandArguments) -> {
                                                    Kit kit = (Kit) commandArguments.get("kit");
                                                    List<String> lore = Arrays.stream(((String)commandArguments.get("lore")).split("%nl%")).toList();
                                                    kit.lore(lore);
                                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ADMIN_KIT_EDIT_LORE_SET, Placeholder.parsed("kit", kit.id()), Placeholder.parsed("lore", String.join("\n", lore))).send(commandSender);
                                                })
                                        )
                                )

                        )
                )
                .register();
    }

    private Argument<Kit> createKitArgument(String nodeName){
        return new CustomArgument<>(new StringArgument(nodeName), info -> KitManager.getInstance().getKitMap().get(info.input().toLowerCase())).includeSuggestions((suggestionInfo, suggestionsBuilder) -> {
            KitManager.getInstance().getKitMap().forEach((s, kit) -> suggestionsBuilder.suggest(s));
            return suggestionsBuilder.buildFuture();
        });
    }

}
