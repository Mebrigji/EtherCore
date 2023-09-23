package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SudoCommand {

    public void register(){
        new CommandAPICommand("sudo")
                .withPermission("ethercraft.command.admin.sudo")
                .withArguments(new PlayerArgument("player"), new GreedyStringArgument("message"))
                .executes((commandSender, commandArguments) -> {
                    Player target = (Player) commandArguments.get("player");
                    String message = (String) commandArguments.get("message");
                    if(message.startsWith("c:")){
                        String command = message.replaceFirst("c:", "").trim();
                        Bukkit.dispatchCommand(target, command);
                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_SUDO_INVOKED_COMMAND, Placeholder.parsed("command", command), Placeholder.parsed("player", target.getName())).send(commandSender);
                    } else {
                        target.chat(message);
                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_SUDO_INVOKED_CHAT, Placeholder.parsed("message", message), Placeholder.parsed("player", target.getName())).send(commandSender);
                    }
                }).register();
    }

}
