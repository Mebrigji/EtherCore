package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.builders.InventoryBuilder;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.manager.UserManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EnderSeeCommand {

    public void register(){
        new CommandAPICommand("endersee")
                .withPermission("ethercraft.command.admin.endersee")
                .withArguments(new OfflinePlayerArgument("target"))
                .executesPlayer((player, commandArguments) -> {
                    OfflinePlayer offlinePlayer = (OfflinePlayer) commandArguments.get(0);
                    assert offlinePlayer != null;
                    UserManager.getInstance().find(offlinePlayer.getName()).ifPresentOrElse(user -> {
                        InventoryBuilder inventoryBuilder = new InventoryBuilder(user.getEnderChest());
                        inventoryBuilder.close(event -> user.add());
                        inventoryBuilder.open(player);

                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ENDERSEE, Placeholder.parsed("player", offlinePlayer.getName())).send(player);
                    }, () -> ComponentHelper.futureComponent("&c[✖] &7Użytkownik o tej nazwie nie został odnaleziony w bazie danych").thenAccept(player::sendMessage));
                }).register();
    }

}
