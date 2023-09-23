package net.saidora.api.db.helpers;

import lombok.SneakyThrows;
import net.minecraft.world.IInventory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionHelper {

    public static String getVersion(){
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static Class<?> getCraftBukkitClass(String classPath){
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + classPath);
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

    public static <T> Constructor<?> getConstructor(Class<T> clazz, Class<?>... args) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            constructor.setAccessible(true);
            if(Arrays.equals(constructor.getParameterTypes(), args)){
                return constructor;
            }
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params){
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            declaredMethod.setAccessible(true);
            if(declaredMethod.getName().equals(methodName) && Arrays.equals(declaredMethod.getParameterTypes(), params)){
                return declaredMethod;
            }
        }
        return null;
    }

    @SneakyThrows
    public static Object invoke(Method method, Object obj, Object... args){
        return method.invoke(obj, args);
    }

    @SneakyThrows
    public static <T> T newInstance(Constructor<T> constructor, Object... args) {
        return constructor.newInstance(args);
    }
}
