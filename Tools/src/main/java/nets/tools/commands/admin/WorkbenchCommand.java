package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandAPICommand;

public class WorkbenchCommand {

    public void register(){
        new CommandAPICommand("workbench")
                .withAliases("wb")
                .withPermission("ethercraft.command.admin.workbench")
                .executesPlayer((player, commandArguments) -> {
                    player.openWorkbench(player.getLocation(), true);
                })
                .register();
    }
}
