package nets.tools.commands.admin;

import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.saidora.api.helpers.ComponentHelper;
import nets.tools.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class BroadcastCommand {

    public void register(){
        new CommandTree("broadcast")
                .withAliases("bc", "ogloszenie")
                .withPermission("ethercraft.command.admin.broadcast")
                .then(new LiteralArgument("toast")
                        .combineWith(new ItemStackArgument("item"), new GreedyStringArgument("message"))
                        .executes((commandSender, commandArguments) -> {
                            UltimateAdvancementAPI api = Main.getInstance().getAdvancementFactory().getUltimateAdvancementAPI();
                            Bukkit.getOnlinePlayers().forEach(player -> api.displayCustomToast(player, (ItemStack) commandArguments.get("item"), (String) commandArguments.get("message"), AdvancementFrameType.TASK));
                        })
                )
                .then(new GreedyStringArgument("message")
                        .executes((commandSender, commandArguments) -> {
                            String text = (String) commandArguments.get(0);
                            ComponentHelper.futureComponent(text).thenAccept(component -> Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(component)));
                            Main.getInstance().getLogger().log(Level.INFO, "[BROADCAST] " + text);
                        })
                )
                .register();
    }

}
