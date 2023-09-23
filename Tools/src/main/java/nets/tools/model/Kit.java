package nets.tools.model;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Kit {

    String id();

    String displayName();
    void displayName(String displayName);

    List<String> lore();
    void lore(List<String> lore);

    int slot();
    void slot(int slot);

    ItemStack icon();
    void icon(ItemStack itemStack);

    String permission();
    void permission(String permission);

    boolean enabled();
    void enabled(boolean enabled);

    long delay();
    void delay(long delay);

    List<ItemStack> items();

}
