package nets.tools.model;

import net.saidora.api.notifications.NotificationBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CreatorCommand {

    String name();

    List<String> lines();

    String permission();

    default Command toCommand(){
        Command command = new Command(name()) {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, String.join("\n", lines())).send(commandSender);
                return false;
            }
        };
        if(permission() != null) command.setPermission(permission());
        return command;
    }
}
