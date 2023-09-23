package nets.tools.parsers;

import lombok.SneakyThrows;
import net.saidora.api.helpers.ReflectionHelper;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class InventoryParser {

    @SneakyThrows
    public static String parseInventory(Inventory inventory){
        if(inventory == null) return "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);

        bukkitObjectOutputStream.writeInt(inventory.getSize());
        bukkitObjectOutputStream.writeUTF(ReflectionHelper.getInventoryTitle(inventory));

        for (int i = 0; i < inventory.getSize(); i++) {
            bukkitObjectOutputStream.writeObject(inventory.getItem(i));
        }

        bukkitObjectOutputStream.close();
        byteArrayOutputStream.close();
        return Base64Coder.encodeLines(byteArrayOutputStream.toByteArray());
    }

    @SneakyThrows
    public static Inventory getInventory(String encodedLines){
        if(encodedLines == null || encodedLines.isEmpty()) return null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(encodedLines));
        BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);

        int size = bukkitObjectInputStream.readInt();

        Inventory inventory = Bukkit.createInventory(null, size, bukkitObjectInputStream.readUTF());
        for (int i = 0; i < size; i++) {
            ItemStack itemStack = (ItemStack) bukkitObjectInputStream.readObject();
            if(itemStack == null) continue;
            inventory.setItem(i, itemStack);
        }
        byteArrayInputStream.close();
        bukkitObjectInputStream.close();
        return inventory;
    }

}
