package nets.tools.objects;

import nets.tools.model.PlayerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerInventoryController implements PlayerInventory {

    private ItemStack helmet, chestPlate, leggings, boots, mainHand, secondHand;
    private List<ItemStack> contents = new ArrayList<>();

    @Override
    public ItemStack getHelmet() {
        return helmet;
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    @Override
    public ItemStack getChestPlate() {
        return chestPlate;
    }

    @Override
    public void setChestPlate(ItemStack chestPlate) {
        this.chestPlate = chestPlate;
    }

    @Override
    public ItemStack getLeggings() {
        return leggings;
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    @Override
    public ItemStack getBoots() {
        return boots;
    }

    @Override
    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    @Override
    public ItemStack getMainHand() {
        return mainHand;
    }

    @Override
    public void setMainHand(ItemStack mainHand) {
        this.mainHand = mainHand;
    }

    @Override
    public ItemStack getSecondHand() {
        return secondHand;
    }

    @Override
    public void setSecondHand(ItemStack secondHand) {
        this.secondHand = secondHand;
    }

    @Override
    public List<ItemStack> getContents() {
        return contents;
    }

    @Override
    public void setContents(List<ItemStack> contents) {
        this.contents = contents;
    }

    @Override
    public void clear() {
        this.helmet = null;
        this.chestPlate = null;
        this.leggings = null;
        this.boots = null;
        this.contents.clear();
    }
}
