package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.builders.InventoryBuilder;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.manager.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ChatCommand {

    public void register(){
        new CommandTree("chat")
                .withPermission("ethercraft.command.admin.chat")
                .then(new LiteralArgument("permission")
                        .combineWith(new StringArgument("permission"))
                        .executes((commandSender, commandArguments) -> {
                            String permission = (String) commandArguments.get(0);
                            if(permission != null && permission.equals("clear")) permission = null;
                            if(permission == null){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_PERMISSION_CLEAR).send(commandSender);
                                ChatManager.getInstance().setPermission(permission);
                                return;
                            }
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_PERMISSION_SET, Placeholder.parsed("old", ChatManager.getInstance().getPermission() + ""), Placeholder.parsed("new", permission)).send(commandSender);
                            ChatManager.getInstance().setPermission(permission);
                        })
                )
                .then(new LiteralArgument("on")
                        .executes((commandSender, commandArguments) -> {
                            if(ChatManager.getInstance().isEnabled()){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_ENABLE_ALREADY).send(commandSender);
                                return;
                            }
                            ChatManager.getInstance().setEnabled(true);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_ENABLE_GLOBAL).builder(notificationBuilder -> Bukkit.getOnlinePlayers().forEach(notificationBuilder::send));
                        })
                ).then(new LiteralArgument("off")
                        .executes((commandSender, commandArguments) -> {
                            if(!ChatManager.getInstance().isEnabled()){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_DISABLE_ALREADY).send(commandSender);
                                return;
                            }
                            ChatManager.getInstance().setEnabled(false);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_DISABLE_GLOBAL).builder(notificationBuilder -> Bukkit.getOnlinePlayers().forEach(notificationBuilder::send));
                        })
                ).then(new LiteralArgument("level")
                        .combineWith(new IntegerArgument("level"))
                        .executes((commandSender, commandArguments) -> {
                            int level = (int) commandArguments.get(0);
                            ChatManager.getInstance().setLevel(level);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_LEVEL_CHANGED, Placeholder.parsed("amount", String.valueOf(level))).builder(notificationBuilder -> Bukkit.getOnlinePlayers().forEach(notificationBuilder::send));
                        }))
                .then(new LiteralArgument("clear")
                        .executes((commandSender, commandArguments) -> {
                            int loops = 250;

                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_CLEAR, Placeholder.parsed("admin", commandSender.getName())).builder(notificationBuilder -> {
                                notificationBuilder.getProvider().with("loops", "\n".repeat(loops));
                                Bukkit.getOnlinePlayers().forEach(notificationBuilder::send);
                            });
                        }).then(new IntegerArgument("loops").executes((commandSender, commandArguments) -> {
                            int loops = (int) commandArguments.get(0);
                            if(loops > 1000) loops = 1000;
                            int finalLoops = loops;
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_CLEAR, Placeholder.parsed("admin", commandSender.getName())).builder(notificationBuilder -> {
                                notificationBuilder.getProvider().with("loops", "\n".repeat(finalLoops));
                                Bukkit.getOnlinePlayers().forEach(notificationBuilder::send);
                            });
                        }))
                )
                .then(new LiteralArgument("flags")
                        .executes((commandSender, commandArguments) -> {
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_FLAGS,
                                    Placeholder.component("items", parseComponent(ChatManager.Flag.ITEMS)),
                                    Placeholder.component("inventory", parseComponent(ChatManager.Flag.INVENTORY)),
                                    Placeholder.component("enderchest", parseComponent(ChatManager.Flag.ENDER_CHEST)),
                                    Placeholder.component("kills", parseComponent(ChatManager.Flag.KILLS))).send(commandSender);
                        }))
                .then(new LiteralArgument("flag")
                        .combineWith(parseFlagArgument("flag"), new BooleanArgument("enable"))
                        .executes((commandSender, commandArguments) -> {
                            ChatManager.Flag flag = (ChatManager.Flag) commandArguments.get("flag");
                            boolean enable = (boolean) commandArguments.get("enable");
                            if(ChatManager.getInstance().isFlagEnabled(flag) && enable){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_FLAG_ALREADY_ENABLED, Placeholder.parsed("flag", flag.name())).send(commandSender);
                            } else if(!ChatManager.getInstance().isFlagEnabled(flag) && !enable){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_FLAG_ALREADY_DISABLED, Placeholder.parsed("flag", flag.name())).send(commandSender);
                            } else if(enable){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_FLAG_ENABLED, Placeholder.parsed("flag", flag.name())).send(commandSender);
                            } else {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT_FLAG_DISABLED, Placeholder.parsed("flag", flag.name())).send(commandSender);
                            }
                            if(enable) ChatManager.getInstance().enableFlag(flag);
                            else ChatManager.getInstance().disableFlag(flag);
                        })
                )
                .then(new LiteralArgument("panel")
                        .executesPlayer((player, commandArguments) -> {
                            InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(null, 3 * 9, ComponentHelper.asComponent("Zarządzanie czatem")));

                            inventoryBuilder.setItem(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23, 24, 25, 26}, itemStack -> itemStack.setType(Material.GRAY_STAINED_GLASS_PANE), itemMeta -> itemMeta.displayName(Component.empty()));

                            ChatManager chatManager = ChatManager.getInstance();

                            inventoryBuilder.setItem(4, itemStack -> itemStack.setType(Material.PAPER), itemMeta -> {
                                itemMeta.displayName(ComponentHelper.asComponent("<!italic>&fZestawy ID flag"));
                                itemMeta.lore(ComponentHelper.asComponents("<!italic> &7- &fITEMS, INVENTORY, ENDER_CHEST, KILLS"));
                            });

                            if (chatManager.isEnabled())
                                inventoryBuilder.setItem(21, itemStack -> itemStack.setType(Material.GREEN_DYE), itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<!italic>&aᴄᴢᴀᴛ ᴊᴇsᴛ ᴡʟᴀᴄᴢᴏɴʏ")));
                            else
                                inventoryBuilder.setItem(21, itemStack -> itemStack.setType(Material.RED_DYE), itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<!italic>&cᴄᴢᴀᴛ ᴊᴇsᴛ ᴡʏʟᴀᴄᴢᴏɴʏ")));

                            inventoryBuilder.setItem(22, itemStack -> itemStack.setType(Material.DIAMOND_PICKAXE), itemMeta -> {
                                itemMeta.displayName(ComponentHelper.asComponent("<!italic><gradient:#0575CC:#66B0E9>ᴍɪɴɪᴍᴜᴍ ᴡʏᴋᴏᴘᴀɴʏᴄʜ ʙʟᴏᴋᴏᴡ ⛏</gradient>"));
                                itemMeta.lore(ComponentHelper.asComponents(List.of("  <!italic>&fAktualnie wymagana ilość: <#66B0E9><amount>"), Placeholder.parsed("amount", String.valueOf(chatManager.getLevel()))));
                            });

                            inventoryBuilder.setItem(9, itemStack -> itemStack.setType(Material.NAME_TAG), itemMeta -> {
                                itemMeta.displayName(ComponentHelper.asComponent("<!italic><gradient:#CBBF04:#E5DE6C>sᴛᴏsᴏᴡᴀɴɪᴇ ᴛᴀɢᴜ <ɪᴛᴇᴍ></gradient>"));
                                itemMeta.lore(ComponentHelper.asComponents(chatManager.isFlagEnabled(ChatManager.Flag.ITEMS) ? "  <!italic>&fAktywne: &aTak" : "  <!italic>&fAktywne: &cNie"));
                            });

                            inventoryBuilder.setItem(10, itemStack -> itemStack.setType(Material.CHEST), itemMeta -> {
                                itemMeta.displayName(ComponentHelper.asComponent("<!italic><gradient:#CBBF04:#E5DE6C>sᴛᴏsᴏᴡᴀɴɪᴇ ᴛᴀɢᴜ <ɪɴᴠᴇɴᴛᴏʀʏ></gradient>"));
                                itemMeta.lore(ComponentHelper.asComponents(chatManager.isFlagEnabled(ChatManager.Flag.INVENTORY) ? "  <!italic>&fAktywne: &aTak" : "  <!italic>&fAktywne: &cNie"));
                            });

                            inventoryBuilder.setItem(11, itemStack -> itemStack.setType(Material.ENDER_CHEST), itemMeta -> {
                                itemMeta.displayName(ComponentHelper.asComponent("<!italic><gradient:#CBBF04:#E5DE6C>sᴛᴏsᴏᴡᴀɴɪᴇ ᴛᴀɢᴜ <ᴇɴᴅᴇʀᴄʜᴇsᴛ></gradient>"));
                                itemMeta.lore(ComponentHelper.asComponents(chatManager.isFlagEnabled(ChatManager.Flag.ENDER_CHEST) ? "  <!italic>&fAktywne: &aTak" : "  <!italic>&fAktywne: &cNie"));
                            });

                            inventoryBuilder.setItem(12, itemStack -> itemStack.setType(Material.IRON_SWORD), itemMeta -> {
                                itemMeta.displayName(ComponentHelper.asComponent("<!italic><gradient:#D14503:#EE7F4A>ᴡɪᴀᴅᴏᴍᴏsᴄɪ ᴏᴅ sᴍɪᴇʀᴄɪ</gradient>"));
                                itemMeta.lore(ComponentHelper.asComponents(chatManager.isFlagEnabled(ChatManager.Flag.KILLS) ? "  <!italic>&fAktywne: &aTak" : "  <!italic>&fAktywne: &cNie"));
                            });

                            inventoryBuilder.click(event -> event.setCancelled(true));

                            inventoryBuilder.open(player);
                        }))
                .executes((sender, commandArguments) -> {
                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHAT).send(sender);
                }).register();
    }

    private Argument<ChatManager.Flag> parseFlagArgument(String nodeName){
        return new CustomArgument<>(new StringArgument(nodeName), customArgumentInfo -> Arrays.stream(ChatManager.Flag.values()).filter(flag -> flag.name().equalsIgnoreCase(customArgumentInfo.input())).findAny().orElseThrow(() -> CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("Wrong flag input ").appendArgInput()))).includeSuggestions((suggestionInfo, suggestionsBuilder) -> {
            for (ChatManager.Flag value : ChatManager.Flag.values()) {
                suggestionsBuilder.suggest(value.name().toLowerCase());
            }
            return suggestionsBuilder.buildFuture();
        });
    }

    private Component parseComponent(ChatManager.Flag flag){
        return ChatManager.getInstance().isFlagEnabled(flag) ? ComponentHelper.asComponent(Main.getInstance().getMessages().COMMAND_CHAT_FLAGS_FORMAT_ENABLED, Placeholder.parsed("flag-name", flag.name())) : ComponentHelper.asComponent(Main.getInstance().getMessages().COMMAND_CHAT_FLAGS_FORMAT_DISABLED, Placeholder.parsed("flag-name", flag.name()));
    }

}
