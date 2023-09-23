package nets.tools.commands.player;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.objects.interactiveItems.ExpBottle;
import org.bukkit.entity.Player;

public class ExpBottleCommand {

    public void register(){
        new CommandTree("expbottle")
                .withAliases("butelka")
                .executesPlayer((player, commandArguments) -> {
                    PlayerExtension.getPlayerExtend(player, playerExtension -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_EXP_BOTTLE_HAVE, Placeholder.parsed("amount", String.valueOf(playerExtension.getExperience()))).send(player));
                })
                .then(new IntegerArgument("experience")
                        .executesPlayer((player, commandArguments) -> {
                            PlayerExtension.getPlayerExtend(player, playerExtension -> {
                                int totalExperience = playerExtension.getExperience();
                                int amount = (int) commandArguments.get(0);
                                if (totalExperience < amount) {
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_EXP_BOTTLE_NOT_ENOUGH, Placeholder.parsed("have", String.valueOf(totalExperience)), Placeholder.parsed("required", String.valueOf(amount))).send(player);
                                    return;
                                }
                                if(player.getInventory().addItem(ExpBottle.DEFAULT.prepareItem(amount, player.getName())).isEmpty()) {
                                    playerExtension.removeExperience(amount);
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_EXP_BOTTLE_WITHDRAW, Placeholder.parsed("amount", String.valueOf(amount))).send(player);
                                } else NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_EXP_BOTTLE_FULL).send(player);
                            });
                        })
                )
                .then(new LiteralArgument("all")
                        .executesPlayer((player, commandArguments) -> {
                            PlayerExtension.getPlayerExtend(player, playerExtension -> {
                                int totalExperience = playerExtension.getExperience();
                                if(totalExperience == 0){
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_EXP_BOTTLE_TOTAL_ZERO).send(player);
                                    return;
                                }
                                if(player.getInventory().addItem(ExpBottle.DEFAULT.prepareItem(totalExperience, player.getName())).isEmpty()) {
                                    playerExtension.removeExperience(totalExperience);
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_EXP_BOTTLE_WITHDRAW, Placeholder.parsed("amount", String.valueOf(totalExperience))).send(player);
                                } else NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_EXP_BOTTLE_FULL).send(player);
                            });
                        })
                )
                .then(new LiteralArgument("create")
                        .withPermission("ethercraft.command.admin.expbottle")
                        .combineWith(new IntegerArgument("experience"))
                        .executesPlayer((player, commandArguments) -> {
                            int experience = (int) commandArguments.get("experience");
                            player.getInventory().addItem(ExpBottle.DEFAULT.prepareItem(experience, "server")).forEach((integer, itemStack) -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_EXP_BOTTLE_CREATED, Placeholder.parsed("exp", String.valueOf(experience)), Placeholder.parsed("player", player.getName())).send(player);
                        }).then(new PlayerArgument("player").executes((commandSender, commandArguments) -> {
                            Player player = (Player) commandArguments.get("player");
                            int experience = (int) commandArguments.get("experience");
                            player.getInventory().addItem(ExpBottle.DEFAULT.prepareItem(experience, "server")).forEach((integer, itemStack) -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_EXP_BOTTLE_CREATED, Placeholder.parsed("exp", String.valueOf(experience)), Placeholder.parsed("player", player.getName())).send(commandSender);
                        }))
                )
                .register();
    }

}
