package net.saidora.api.helpers;

import lombok.SneakyThrows;
import net.minecraft.world.IInventory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;

public class ReflectionHelper {

    public static String getVersion(){
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static Class<?> getCraftBukkitClass(String path){
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + path);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Class<?> getMinecraftClass(String path){
        try {
            return Class.forName("net.minecraft." + path);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Field getField(Class<?> clazz, String fieldName){
        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);
            if(declaredField.getName().equals(fieldName)) return declaredField;
        }
        return null;
    }

    public static void setFieldValue(Field field, Object target, Object value){
        Objects.requireNonNull(field, "Field");
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName) {
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            declaredMethod.setAccessible(true);
            if(declaredMethod.getName().equals(methodName)) return declaredMethod;
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... args){
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            declaredMethod.setAccessible(true);
            if(declaredMethod.getName().equals(methodName) && Arrays.equals(declaredMethod.getParameterTypes(), args))
                return declaredMethod;
        }
        return null;
    }

    public static Object invoke(Field field, Object object){
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static Object invoke(Method method, Object object, Object... args){
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    @SneakyThrows
    public static <T> T newInstance(Class<T> clazz){
        return clazz.newInstance();
    }

    @SneakyThrows
    public static <T> T newInstance(Constructor<T> constructor, Object... args){
        return constructor.newInstance(args);
    }

    public static boolean isStatic(Method method){
        method.setAccessible(true);
        return Modifier.isStatic(method.getModifiers());
    }

    public static boolean isStatic(Field field) {
        field.setAccessible(true);
        return Modifier.isStatic(field.getModifiers());
    }

    public static <T> Constructor<?> getConstructor(Class<T> clazz, Class<?>... params) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            constructor.setAccessible(true);
            if(Arrays.equals(constructor.getParameterTypes(), params))
                return constructor;
        }
        return null;
    }

    private static final Class<?> CraftInventoryClass = getCraftBukkitClass("inventory.CraftInventory");
    private static final Class<?> MinecraftInventoryClass = getCraftBukkitClass("inventory.CraftInventoryCustom").getDeclaredClasses()[0];

    private static Field IInventoryField;
    private static Field titleField;

    static {
        IInventoryField = getField(CraftInventoryClass, "inventory");
        titleField = getField(MinecraftInventoryClass, "title");
    }

    public static String getInventoryTitle(Inventory inventory){
        if(CraftInventoryClass == null || MinecraftInventoryClass == null || IInventoryField == null || titleField == null){
            return "";
        }

        Object inv = CraftInventoryClass.cast(inventory);
        try {
            IInventory iInventory = (IInventory) IInventoryField.get(inv);
            Object minecraftInventory = MinecraftInventoryClass.cast(iInventory);
            return (String) titleField.get(minecraftInventory);
        } catch (Exception e) {
            return "";
        }
    }

}
