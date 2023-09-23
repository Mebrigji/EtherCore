package nets.tools.parsers;

import lombok.SneakyThrows;
import nets.tools.model.PlayerInventory;
import nets.tools.objects.PlayerInventoryController;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PlayerInventoryParser {

    @SneakyThrows
    public static String parsePlayerInventory(PlayerInventory playerInventory){
        if(playerInventory == null) return "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
        bukkitObjectOutputStream.writeObject(playerInventory.getHelmet());
        bukkitObjectOutputStream.writeObject(playerInventory.getChestPlate());
        bukkitObjectOutputStream.writeObject(playerInventory.getLeggings());
        bukkitObjectOutputStream.writeObject(playerInventory.getBoots());
        bukkitObjectOutputStream.writeObject(playerInventory.getMainHand());
        bukkitObjectOutputStream.writeObject(playerInventory.getSecondHand());
        bukkitObjectOutputStream.writeInt(playerInventory.getContents().size());
        for (ItemStack content : playerInventory.getContents()) {
            bukkitObjectOutputStream.writeObject(content);
        }
        bukkitObjectOutputStream.close();
        byteArrayOutputStream.close();
        return Base64Coder.encodeLines(byteArrayOutputStream.toByteArray());
    }

    @SneakyThrows
    public static PlayerInventory getPlayerInventory(String encodedLines){
        if(encodedLines == null || encodedLines.isEmpty()) return new PlayerInventoryController();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(encodedLines));
        BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);

        ItemStack helmet = (ItemStack) bukkitObjectInputStream.readObject(), chestPlate = (ItemStack) bukkitObjectInputStream.readObject(), leggings = (ItemStack) bukkitObjectInputStream.readObject(), boots = (ItemStack) bukkitObjectInputStream.readObject(), mainHand = (ItemStack) bukkitObjectInputStream.readObject(), secondHand = (ItemStack) bukkitObjectInputStream.readObject();
        List<ItemStack> contents = new ArrayList<>();
        int size = bukkitObjectInputStream.readInt();
        for (int i = 0; i < size; i++) {
            contents.add((ItemStack) bukkitObjectInputStream.readObject());
        }

        byteArrayInputStream.close();
        bukkitObjectInputStream.close();

        PlayerInventory playerInventory = new PlayerInventoryController();
        playerInventory.setHelmet(helmet);
        playerInventory.setChestPlate(chestPlate);
        playerInventory.setLeggings(leggings);
        playerInventory.setBoots(boots);
        playerInventory.setMainHand(mainHand);
        playerInventory.setSecondHand(secondHand);
        playerInventory.setContents(contents);
        return playerInventory;
    }

}
