package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import net.saidora.api.notifications.NotificationBuilder;

public class TestCommand {

    public void register(){
        new CommandTree("test")
                .withPermission("ethercraft.command.admin.test")
                .then(new LiteralArgument("gradient")
                        .combineWith(new GreedyStringArgument("text"))
                        .executes((commandSender, commandArguments) -> {
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, (String) commandArguments.get(0)).send(commandSender);
                        })
                ).register();
    }
}
