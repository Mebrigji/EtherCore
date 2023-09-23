package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ChatArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.saidora.api.helpers.ComponentHelper;
import nets.tools.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class BroadcastCommand {

    public void register(){
        new CommandAPICommand("broadcast")
                .withAliases("bc", "ogloszenie")
                .withPermission("ethercraft.command.admin.broadcast")
                .withArguments(new GreedyStringArgument("message"))
                .executes((commandSender, commandArguments) -> {
                    String text = (String) commandArguments.get(0);
                    ComponentHelper.futureComponent(text).thenAccept(component -> Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(component)));
                    Main.getInstance().getLogger().log(Level.INFO, "[BROADCAST] " + text);
        }).register();
    }

}
