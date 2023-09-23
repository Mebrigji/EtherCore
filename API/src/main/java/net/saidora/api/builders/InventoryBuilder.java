package net.saidora.api.builders;

import net.saidora.api.extension.PlayerExtension;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.Consumer;

public class InventoryBuilder {

    private final Inventory inventory;

    private Consumer<InventoryClickEvent> clickEventConsumer;
    private Consumer<InventoryCloseEvent> closeEventConsumer;

    private Runnable updater;

    public Inventory getInventory() {
        return inventory;
    }

    public Consumer<InventoryClickEvent> getClickEventConsumer() {
        return clickEventConsumer;
    }

    public void click(Consumer<InventoryClickEvent> clickEventConsumer) {
        this.clickEventConsumer = clickEventConsumer;
    }

    public Consumer<InventoryCloseEvent> getCloseEventConsumer() {
        return closeEventConsumer;
    }

    public void close(Consumer<InventoryCloseEvent> closeEventConsumer) {
        this.closeEventConsumer = closeEventConsumer;
    }

    public InventoryBuilder openEditor(Consumer<InventoryBuilder> consumer){
        consumer.accept(this);
        return this;
    }

    public void open(Player player){
        PlayerExtension playerExtension = PlayerExtension.getPlayerExtend(player);
        if(playerExtension == null) return;

        playerExtension.addPersistentDataObject("inventory", this);
        player.openInventory(inventory);
    }

    public void setItem(int slot, ItemStack itemStack){
        inventory.setItem(slot, itemStack);
    }

    public void setItem(int slot, Consumer<ItemStack> consumer){
        ItemStack stack = new ItemStack(Material.AIR);
        consumer.accept(stack);
        inventory.setItem(slot, stack);
    }

    public void setItem(int slot, Consumer<ItemStack> stackConsumer, Consumer<ItemMeta> metaConsumer){
        ItemStack stack = new ItemStack(Material.AIR);
        stackConsumer.accept(stack);
        ItemMeta meta = stack.getItemMeta();
        metaConsumer.accept(meta);
        stack.setItemMeta(meta);
        inventory.setItem(slot, stack);
    }

    public void setItem(List<Integer> slots, Consumer<ItemStack> stackConsumer, Consumer<ItemMeta> metaConsumer){
        ItemStack stack = new ItemStack(Material.AIR);
        stackConsumer.accept(stack);
        ItemMeta meta = stack.getItemMeta();
        metaConsumer.accept(meta);
        stack.setItemMeta(meta);
        for (int slot : slots) {
            inventory.setItem(slot, stack);
        }
    }

    public void setItem(List<Integer> slots, Consumer<ItemStack> consumer){
        ItemStack stack = new ItemStack(Material.AIR);
        consumer.accept(stack);
        for (int slot : slots) {
            inventory.setItem(slot, stack);
        }
    }

    public void setItem(int[] slots, Consumer<ItemStack> stackConsumer, Consumer<ItemMeta> metaConsumer){
        ItemStack stack = new ItemStack(Material.AIR);
        stackConsumer.accept(stack);
        ItemMeta meta = stack.getItemMeta();
        metaConsumer.accept(meta);
        stack.setItemMeta(meta);
        for (int slot : slots) {
            inventory.setItem(slot, stack);
        }
    }

    public void setItem(int[] slots, Consumer<ItemStack> consumer){
        ItemStack stack = new ItemStack(Material.AIR);
        consumer.accept(stack);
        for (int slot : slots) {
            inventory.setItem(slot, stack);
        }
    }

    public void addItem(ItemStack itemStack){
        inventory.addItem(itemStack);
    }

    public void addItem(Consumer<ItemStack> consumer){
        ItemStack stack = new ItemStack(Material.AIR);
        consumer.accept(stack);
        inventory.addItem(stack);
    }

    public void addItem(Consumer<ItemStack> itemStackConsumer, Consumer<ItemMeta> itemMetaConsumer){
        ItemStack stack = new ItemStack(Material.AIR);
        itemStackConsumer.accept(stack);

        ItemMeta meta = stack.getItemMeta();
        itemMetaConsumer.accept(meta);
        stack.setItemMeta(meta);

        inventory.addItem(stack);
    }

    public void addItem(ItemStack itemStack, Consumer<ItemStack> stackConsumer, Consumer<ItemMeta> metaConsumer){
        stackConsumer.accept(itemStack);
        ItemMeta meta = itemStack.getItemMeta();
        metaConsumer.accept(meta);
        itemStack.setItemMeta(meta);
        inventory.addItem(itemStack);
    }

    public InventoryBuilder(Inventory inventory) {
        this.inventory = inventory;
    }

    public Runnable getUpdater() {
        return updater;
    }

    public void setUpdater(Runnable updater) {
        this.updater = updater;
    }

    public void addItems(List<ItemStack> itemStackList) {
        inventory.addItem(itemStackList.toArray(new ItemStack[]{}));
    }

    public void then(Consumer<InventoryBuilder> consumer) {
        consumer.accept(this);
    }

    public void setItem(int slot, ItemStack itemStack, Consumer<ItemMeta> metaConsumer) {
        ItemMeta meta = itemStack.getItemMeta();
        metaConsumer.accept(meta);
        itemStack.setItemMeta(meta);
        inventory.setItem(slot, itemStack);
    }

    public void addItem(ItemStack itemStack, Consumer<ItemMeta> metaConsumer) {
        ItemMeta meta = itemStack.getItemMeta();
        metaConsumer.accept(meta);
        itemStack.setItemMeta(meta);
        inventory.addItem(itemStack);
    }

    public void addItem(int times, Consumer<ItemStack> stackConsumer, Consumer<ItemMeta> metaConsumer) {
        for (int i = 0; i < times; i++) {
            addItem(stackConsumer, metaConsumer);
        }
    }
}
