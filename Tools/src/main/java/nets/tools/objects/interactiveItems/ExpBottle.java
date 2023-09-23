package nets.tools.objects.interactiveItems;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.nbt.NBTTagCompound;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.helpers.ExperienceHelper;
import net.saidora.api.helpers.ItemHelper;
import nets.tools.model.InteractiveItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;

@Getter
public class ExpBottle implements InteractiveItem {

    private static final String ID = "exp-bottle";
    public static final ExpBottle DEFAULT = new ExpBottle();

    public ItemStack prepareItem(int experience, String owner){
        return ItemHelper.edit(new ItemStack(Material.EXPERIENCE_BOTTLE))
                .editItemMeta(ItemMeta.class, itemMeta -> {
                    itemMeta.displayName(ComponentHelper.asComponent("<gradient:#EADB44:#CCCF00>✨ ʙᴜᴛᴇʟᴋᴀ ᴢ ᴅᴏsᴡɪᴀᴅᴄᴢᴇɴɪᴇᴍ</gradient>"));
                    itemMeta.lore(ComponentHelper.asComponents(Arrays.asList(" ",
                            "  <!italic>&fᴢᴀᴡᴀʀᴛᴇ ᴅᴏsᴡɪᴀᴅᴄᴢᴇɴɪᴇ: <gradient:#25D002:#75F05C><amount></gradient>  ",
                            "  <!italic>&fᴡ ᴘʀᴢᴇʟɪᴄᴢᴇɴɪᴜ ɴᴀ ᴘᴏᴢɪᴏᴍ: <gradient:#E8AD03:#EEA73A><level></gradient>  ",
                            "  <!italic>&fᴡʟᴀsᴄɪᴄɪᴇʟ: <gradient:#01B4C9:#68E6F6><owner></gradient>",
                            " "), Placeholder.parsed("amount", String.valueOf(experience)), Placeholder.parsed("level", String.valueOf(ExperienceHelper.getInstance().expToLevel(experience))), Placeholder.parsed("owner", owner)));
                })
                .editNbtTagCompound(nbtItem -> {
                    nbtItem.setString("interactiveItem", ID);
                    nbtItem.setInteger("exp-value", experience);
                })
                .getItemStack();
    }

    @Override
    public ItemStack getItem() {
        return ItemHelper.edit(new ItemStack(Material.EXPERIENCE_BOTTLE))
                .editItemMeta(ItemMeta.class, itemMeta -> itemMeta.displayName(ComponentHelper.asComponent("<gradient:#EADB44:#CCCF00>✨ ʙᴜᴛᴇʟᴋᴀ ᴢ ᴅᴏsᴡɪᴀᴅᴄᴢᴇɴɪᴇᴍ</gradient>")))
                .editNbtTagCompound(nbtItem -> nbtItem.setString("interactiveItem", ID))
                .getItemStack();
    }

    @Override
    public boolean isSimilar(ItemStack itemStack) {
        String id = ItemHelper.edit(itemStack).getCompoundValue("interactiveItem", String.class);
        return id != null && id.equals(ID);
    }

    @Override
    public <V> V get(NBTItem compound, String key, Class<? extends V> type) {
        return compound.getOrNull(key, type);
    }

    @Override
    public <V> void add(NBTItem compound, String key, V value) {
        compound.setObject(key, value);
    }

    @Override
    public Consumer<PlayerInteractEvent> handleInteractEvent() {
        return event -> {
            if(event.getAction().isRightClick()) {
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);

                Player player = event.getPlayer();
                int exp = ItemHelper.edit(event.getItem()).getCompoundValue("exp-value", Integer.class);
                player.giveExp(exp, false);

                player.getInventory().removeItem(ItemHelper.edit(event.getItem()).editBukkitItemStack(itemStack -> itemStack.setAmount(1)).getItemStack());
            }
        };
    }
}
