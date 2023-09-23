package net.saidora.api.helpers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemHelper {

    public static ItemHelper edit(ItemStack itemStack){
        return new ItemHelper(itemStack);
    }

    public static ItemHelper asPlayerHead(String owner){
        return new ItemHelper(new ItemStack(Material.PLAYER_HEAD)).editItemMeta(SkullMeta.class, skullMeta -> skullMeta.setOwner(owner));
    }

    public static ItemHelper asTexturedHead(String texture){
        return new ItemHelper(new ItemStack(Material.PLAYER_HEAD)).editItemMeta(SkullMeta.class, skullMeta -> {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", texture));

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        });
    }

    private ItemStack itemStack;

    private ItemHelper(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemHelper editNbtTagCompound(Consumer<NBTItem> consumer){
        NBTItem nbtItem = new NBTItem(itemStack);
        consumer.accept(nbtItem);
        this.itemStack = nbtItem.getItem();
        return this;
    }

    public ItemHelper editMinecraftItemStack(Consumer<net.minecraft.world.item.ItemStack> consumer){
        net.minecraft.world.item.ItemStack itemStack = net.minecraft.world.item.ItemStack.fromBukkitCopy(this.itemStack);
        consumer.accept(itemStack);
        this.itemStack = itemStack.asBukkitCopy();
        return this;
    }

    public ItemHelper editBukkitItemStack(Consumer<ItemStack> consumer){
        consumer.accept(this.itemStack);
        return this;
    }

    public <T extends ItemMeta> ItemHelper editItemMeta(Class<T> metaClass, Consumer<T> consumer){
        ItemMeta meta = this.itemStack.getItemMeta();
        try {
            T t = (T) meta;
            consumer.accept(t);
            this.itemStack.setItemMeta(t);
        } catch (ClassCastException ignored){}
        return this;
    }

    public boolean containsKey(String tag){
        NBTItem item = new NBTItem(itemStack);
        return item.hasTag(tag);
    }

    public <T> T getCompoundValue(String tag, Class<T> type){
        NBTItem item = new NBTItem(itemStack);
        return item.getOrNull(tag, type);
    }

    public void then(Consumer<ItemStack> stackConsumer){
        stackConsumer.accept(itemStack);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
