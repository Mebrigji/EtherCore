package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import nets.tools.manager.UserManager;
import nets.tools.model.User;

public class VanishCommand {

    public void register(){
        new CommandTree("vanish").
                withAliases("v")
                .withPermission("ethercraft.command.admin.vanish")
                .then(new LiteralArgument("panel"))
                .then(new LiteralArgument("help"))
                .then(new BooleanArgument("enable"))
                .then(new PlayerArgument("target")
                        .withPermission("ethercraft.command.admin.vanish.target")
                        .executes((commandSender, commandArguments) -> {

                        })
                )
                .executesPlayer((player, commandArguments) -> {
                    UserManager.getInstance().getUser(player).map(User::asVanishedPlayer).ifPresent(vanishedPlayer -> {

                    });
                }).register();
    }

}
