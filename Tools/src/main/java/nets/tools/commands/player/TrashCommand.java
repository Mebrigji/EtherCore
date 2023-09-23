package nets.tools.commands.player;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;

public class TrashCommand {

    public void register(){
        new CommandAPICommand("trash")
                .withAliases("kosz")
                .executesPlayer((player, commandArguments) -> {
                    player.openInventory(Bukkit.createInventory(player, 4*9, "Kosz"));
                })
                .register();
    }

}
