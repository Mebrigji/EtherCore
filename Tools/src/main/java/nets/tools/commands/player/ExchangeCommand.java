package nets.tools.commands.player;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.helpers.ExperienceHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExchangeCommand {

    public void register(){
        new CommandTree("exchange")
                .withAliases("zamien", "wymien")
                .executesPlayer((player, commandArguments) -> {
                    int amount = getAmountOfType(Material.COBBLESTONE, player);
                    int gainExp = amount / 4;
                    int finalAmount = gainExp * 4;
                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHANGE_MANY, Placeholder.parsed("amount", String.valueOf(finalAmount)), Placeholder.parsed("experience", String.valueOf(gainExp))).send(player);
                })
                .then(new IntegerArgument("amount")
                        .executesPlayer((player, commandArguments) -> {
                            int amount = (int) commandArguments.get(0);
                            int amountInInventory = getAmountOfType(Material.COBBLESTONE, player);

                            if(amountInInventory < amount){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHANGE_NOT_ENOUGH, Placeholder.parsed("amount", String.valueOf(amountInInventory)), Placeholder.parsed("required", String.valueOf(amount))).send(player);
                                return;
                            }

                            int gainExp = amount / 4;
                            int finalAmount = gainExp * 4;

                            player.getInventory().removeItem(new ItemStack(Material.COBBLESTONE, finalAmount));

                            ExperienceHelper.getInstance().start(experienceHelper -> {

                                int exp = experienceHelper.getTotalExperience(player);
                                experienceHelper.setTotalExperience(player, exp + gainExp);

                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_CHANGE, Placeholder.parsed("amount", String.valueOf(finalAmount)), Placeholder.parsed("exp", String.valueOf(gainExp))).send(player);
                            });
                        })
                ).register();
    }


    private int getAmountOfType(Material material, Player player){
        int amount = 0;
        for (ItemStack content : player.getInventory().getContents()) {
            if(content == null || !content.getType().equals(material)) continue;
            amount += content.getAmount();
        }
        return amount;
    }

}
