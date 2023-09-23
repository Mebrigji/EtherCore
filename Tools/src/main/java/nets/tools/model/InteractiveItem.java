package nets.tools.model;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface InteractiveItem {

    ItemStack getItem();

    boolean isSimilar(ItemStack itemStack);

    <V> V get(NBTItem compound, String key, Class<? extends V> type);

    <V> void add(NBTItem compound, String key, V value);

    Consumer<PlayerInteractEvent> handleInteractEvent();

}
