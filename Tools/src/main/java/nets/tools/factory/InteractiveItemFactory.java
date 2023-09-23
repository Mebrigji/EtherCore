package nets.tools.factory;

import net.saidora.api.helpers.ItemHelper;
import nets.tools.model.InteractiveItem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashSet;
import java.util.Set;

public class InteractiveItemFactory implements Listener {

    private final JavaPlugin javaPlugin;


    private Set<InteractiveItem> interactiveItemSet = new LinkedHashSet<>();

    public InteractiveItemFactory(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public InteractiveItemFactory addItem(InteractiveItem item){
        interactiveItemSet.add(item);
        return this;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void handlePlayerInteractEvent(PlayerInteractEvent event){
        if(event.getItem() != null && event.getAction().isRightClick()){
            ItemStack itemStack = event.getItem();

            ItemHelper.edit(itemStack).editNbtTagCompound(nbtItem -> {

                if(nbtItem.getOrNull("interactiveItem", String.class) != null){
                    interactiveItemSet.stream().filter(interactiveItem -> interactiveItem.isSimilar(itemStack)).forEach(interactiveItem -> interactiveItem.handleInteractEvent().accept(event));
                }
            });
        }
    }

    public void setupListener(){
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }
}
