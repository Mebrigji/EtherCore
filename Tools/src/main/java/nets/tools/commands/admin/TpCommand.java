package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TpCommand {

    public void register() {
        new CommandTree("tp")
                .withAliases("tpa", "teleport", "tppos")
                .withPermission("ethercraft.command.admin.teleport")
                .then(new PlayerArgument("to")
                        .executesPlayer((player, commandArguments) -> {
                            Player to = (Player) commandArguments.get(0);
                            player.teleport(to);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_TELEPORT_TO_PLAYER, Placeholder.parsed("player", to.getName())).send(player);
                        })
                )
                .then(new PlayerArgument("who")
                        .combineWith(new PlayerArgument("to"))
                        .executes((commandSender, commandArguments) -> {
                            Player to = (Player) commandArguments.get("to"), who = (Player) commandArguments.get("who");
                            who.teleport(to, PlayerTeleportEvent.TeleportCause.COMMAND);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_TELEPORT_TARGET_TO_PLAYER, Placeholder.parsed("target", who.getName()), Placeholder.parsed("player", to.getName())).send(commandSender);
                        })
                )
                .then(new LocationArgument("position", LocationType.PRECISE_POSITION)
                        .withPermission("ethercraft.command.admin.teleport.position")
                        .executesPlayer((player, commandArguments) -> {
                            Location location = (Location) commandArguments.get(0);
                            player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_TELEPORT_TO_LOCATION, Placeholder.parsed("location", String.format("{world=%s,x=%s,y=%s,z=%s}", location.getWorld().getName(), location.getX(), location.getY(), location.getZ()))).send(player);
                        }).then(new PlayerArgument("target")
                                .executes((commandSender, commandArguments) -> {
                                    Location location = (Location) commandArguments.get(0);
                                    Player player = (Player) commandArguments.get(1);
                                    player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_TELEPORT_TARGET_TO_LOCATION, Placeholder.parsed("target", player.getName()), Placeholder.parsed("location", String.format("{world=%s,x=%s,y=%s,z=%s}", location.getWorld().getName(), location.getX(), location.getY(), location.getZ()))).send(commandSender);
                                })))
                .override();
    }
}
