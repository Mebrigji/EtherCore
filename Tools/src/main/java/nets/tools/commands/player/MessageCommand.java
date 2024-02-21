package nets.tools.commands.player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.helpers.ComponentHelper;
import nets.tools.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageCommand {

    public void register(){
        new CommandTree("msg")
                .withAliases("message", "tell", "wiadomosc", "r", "odpisz")
                .then(new PlayerArgument("target")
                        .combineWith(new GreedyStringArgument("message"))
                        .executes((commandSender, commandArguments) -> {
                            Player player = (Player) commandArguments.get("target");
                            String message = (String) commandArguments.get("message");
                            Component text = Component.text(message);
                            ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_MESSAGE_FORMAT_SENDER, Placeholder.parsed("player", player.getName()), Placeholder.component("message", text)).thenAccept(commandSender::sendMessage);
                            ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_MESSAGE_FORMAT_RECEIVER, Placeholder.parsed("player", commandSender.getName()), Placeholder.component("message", text)).thenAccept(player::sendMessage);

                            PlayerExtension.getPlayerExtend(player, playerExtension -> {

                                if(commandSender instanceof Player) playerExtension.addPersistentDataObject("reply", commandSender.getName());
                            });
                        })
                )
                .then(new GreedyStringArgument("message")
                        .executesPlayer((player, commandArguments) -> {
                            String message = (String) commandArguments.get("message");
                            assert message != null;
                            PlayerExtension.getPlayerExtend(player, sender -> {
                                String targetNickName = sender.getPersistentDataObject("reply", String.class);

                                Player exactPlayer;
                                if(targetNickName == null || (exactPlayer = Bukkit.getPlayerExact(targetNickName)) == null){
                                    ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_MESSAGE_REPLY_NOT_FOUND).thenAccept(player::sendMessage);
                                    return;
                                }

                                Component text = Component.text(message);
                                PlayerExtension.getPlayerExtend(exactPlayer, extension -> {

                                    ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_MESSAGE_FORMAT_SENDER, Placeholder.parsed("player", targetNickName), Placeholder.component("message", text)).thenAccept(player::sendMessage);
                                    ComponentHelper.futureComponent(Main.getInstance().getMessages().COMMAND_MESSAGE_FORMAT_RECEIVER, Placeholder.parsed("player", player.getName()), Placeholder.component("message", text)).thenAccept(exactPlayer::sendMessage);
                                    extension.addPersistentDataObject("reply", player.getName());
                                });
                            });
                        })
                )
                .override();
    }
}
