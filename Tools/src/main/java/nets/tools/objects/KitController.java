package nets.tools.objects;

import nets.tools.model.Kit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitController implements Kit {

    private final String id;

    private String displayName;
    private int slot;
    private ItemStack icon;

    private long delay;
    private String permission;
    private boolean enabled;

    private List<ItemStack> itemStacks = new ArrayList<>();
    private List<String> lore;

    public KitController(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public void displayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public List<String> lore() {
        return lore;
    }

    @Override
    public void lore(List<String> lore) {
        this.lore = lore;
    }

    @Override
    public int slot() {
        return slot;
    }

    @Override
    public void slot(int slot) {
        this.slot = slot;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public void icon(ItemStack itemStack) {
        this.icon = itemStack;
    }

    @Override
    public String permission() {
        return permission;
    }

    @Override
    public void permission(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public void enabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public long delay() {
        return delay;
    }

    @Override
    public void delay(long delay) {
        this.delay = delay;
    }

    @Override
    public List<ItemStack> items() {
        return itemStacks;
    }
}
