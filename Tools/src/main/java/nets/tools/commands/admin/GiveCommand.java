package nets.tools.commands.admin;


import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand {

    public void register(){
        new CommandAPICommand("give")
                .withPermission("ethercraft.command.admin.give")
                .withArguments(new PlayerArgument("player"), new ItemStackArgument("item"))
                .withOptionalArguments(new IntegerArgument("amount"))
                .executes((commandSender, commandArguments) -> {
                    Player player = (Player) commandArguments.get("player");
                    ItemStack itemStack = (ItemStack) commandArguments.get("item");
                    commandArguments.getOptional("amount").map(o -> (Integer)o).ifPresent(integer -> itemStack.setAmount(integer));
                    player.getInventory().addItem(itemStack).forEach((integer, stack) -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
                    if(player.equals(commandSender)){
                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_GIVE_SELF, Placeholder.component("item", ComponentHelper.asComponent("[ITEM]").hoverEvent(itemStack.asHoverEvent()))).send(commandSender);
                    } else {
                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_GIVE, Placeholder.parsed("player", player.getName()), Placeholder.component("item", ComponentHelper.asComponent("[ITEM]").hoverEvent(itemStack.asHoverEvent()))).send(commandSender);
                    }
                })
                .override();
    }

}
