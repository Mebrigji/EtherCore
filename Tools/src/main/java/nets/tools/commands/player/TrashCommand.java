package nets.tools.commands.player;

import dev.jorel.commandapi.CommandAPICommand;
import net.saidora.api.builders.InventoryBuilder;
import net.saidora.api.helpers.ComponentHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Optional;

public class TrashCommand {

    public void register(){
        new CommandAPICommand("trash")
                .withAliases("kosz")
                .executesPlayer((player, commandArguments) -> {
                    InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(player, 4*9, "Kosz"));
                    inventoryBuilder.setItem(31, itemStack -> itemStack.setType(Material.LAVA_BUCKET), itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<!italic>&cSpal itemy")));
                    inventoryBuilder.click(inventoryClickEvent -> {
                        if(inventoryClickEvent.getSlot() == 31) {
                            for (int i = 0; i < inventoryClickEvent.getInventory().getSize(); i++) {
                                if(i == 31) continue;
                                inventoryBuilder.setItem(i, itemStack -> itemStack.setType(Material.AIR));
                            }
                            inventoryClickEvent.setCancelled(true);
                        }
                    });
                    inventoryBuilder.open(player);
                })
                .register();
    }

}
