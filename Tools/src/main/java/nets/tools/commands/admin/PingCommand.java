package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.entity.Player;

public class PingCommand {

    public void register(){
        new CommandTree("ping")
                .then(new PlayerArgument("target")
                        .withPermission("ethercraft.command.admin.ping")
                        .executes((commandSender, commandArguments) -> {
                            Player player = (Player) commandArguments.get("target");
                            PlayerExtension.getPlayerExtend(player, extension -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_PING_OTHER, Placeholder.parsed("player", player.getName()), Placeholder.parsed("ping", String.valueOf(extension.getPing()))).send(commandSender));
                        })
                )
                .executesPlayer((player, commandArguments) -> {
                    PlayerExtension.getPlayerExtend(player, extension -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_PING, Placeholder.parsed("player", player.getName()), Placeholder.parsed("ping", String.valueOf(extension.getPing()))).send(player));
                }).override();
    }


}
