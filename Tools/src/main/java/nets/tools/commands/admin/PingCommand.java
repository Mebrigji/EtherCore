package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.entity.Player;

public class PingCommand {

    public void register(){
        new CommandAPICommand("ping")
                .executesPlayer((player, commandArguments) -> {
                    PlayerExtension.getPlayerExtend(player, extension -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_PING, Placeholder.parsed("player", player.getName()), Placeholder.parsed("ping", String.valueOf(extension.getPing()))).send(player));
                })
                .withArguments(new PlayerArgument("target")
                        .withPermission("ethercraft.command.admin.ping")
                        .executes((commandSender, commandArguments) -> {
                            Player player = (Player) commandArguments.get(0);
                            PlayerExtension.getPlayerExtend(player, extension -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_PING_OTHER, Placeholder.parsed("player", player.getName()), Placeholder.parsed("ping", String.valueOf(extension.getPing()))).send(commandSender));
                        })).override();
    }


}
