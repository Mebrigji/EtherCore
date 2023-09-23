package nets.tools.model;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface PlayerInventory {

    ItemStack getHelmet();
    void setHelmet(ItemStack itemStack);

    ItemStack getChestPlate();
    void setChestPlate(ItemStack itemStack);

    ItemStack getLeggings();
    void setLeggings(ItemStack itemStack);

    ItemStack getBoots();
    void setBoots(ItemStack boots);

    ItemStack getMainHand();
    void setMainHand(ItemStack itemStack);

    ItemStack getSecondHand();
    void setSecondHand(ItemStack itemStack);

    List<ItemStack> getContents();
    void setContents(List<ItemStack> itemStacks);

    void clear();

}
