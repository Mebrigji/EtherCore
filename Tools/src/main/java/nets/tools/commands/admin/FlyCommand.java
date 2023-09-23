package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandAPICommand;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.entity.Player;

public class FlyCommand {

    public void register(){
        new CommandAPICommand("fly")
                .withPermission("ethercraft.command.admin.fly")
                .executesPlayer((player, commandArguments) -> {
                    player.setAllowFlight(!player.getAllowFlight());
                    NotificationBuilder
                            .of(NotificationBuilder.NotificationType.CHAT, player.getAllowFlight() ? Main.getInstance().getMessages().COMMAND_FLY_ON : Main.getInstance().getMessages().COMMAND_FLY_OFF)
                            .send(player);
                }).register();
    }

}
