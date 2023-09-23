package nets.tools.commands.player;

import dev.jorel.commandapi.CommandAPICommand;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HatCommand {

    public void register(){
        new CommandAPICommand("hat")
                .withAliases("czapka")
                .executesPlayer((player, commandArguments) -> {
                    ItemStack itemInHand = player.getEquipment().getItemInMainHand();
                    if(itemInHand.getType().isAir()){
                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_HAT_AIR).send(player);
                        return;
                    }
                    if(player.getEquipment().getHelmet() != null && !player.getEquipment().getHelmet().getType().isAir()){
                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_HAT_OCCUPIED).send(player);
                        return;
                    }
                    player.getEquipment().setHelmet(itemInHand);
                    player.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_HAT).send(player);
                }).register();
    }

}
