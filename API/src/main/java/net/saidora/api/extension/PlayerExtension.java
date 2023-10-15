package net.saidora.api.extension;

import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.player.EntityHuman;
import net.saidora.api.events.list.PlayerInjectExtensionEvent;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.helpers.ExperienceHelper;
import net.saidora.api.helpers.ReflectionHelper;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerExtension extends Player.Spigot {

    private static final Class<?> CraftPlayerClass = ReflectionHelper.getCraftBukkitClass("entity.CraftPlayer");
    private static final Field spigotField = ReflectionHelper.getField(CraftPlayerClass, "spigot");

    private static final Method getHandle = ReflectionHelper.getMethod(CraftPlayerClass, "getHandle");

    public static PlayerExtension getPlayerExtend(Player player){
        if(player.spigot() instanceof PlayerExtension playerExtension) return playerExtension;
        else return null;
    }

    public static PlayerExtension getPlayerExtend(Player player, Consumer<PlayerExtension> extensionConsumer){
        if(player.spigot() instanceof PlayerExtension playerExtension) {
            extensionConsumer.accept(playerExtension);
            return playerExtension;
        }
        else return null;
    }
    private final Player player;
    private EntityPlayer entityPlayer;

    private Player.Spigot old;

    private final Map<String, Object> persistentDataContainer = new HashMap<>();

    public PlayerExtension(Player player){
        this.player = player;
        this.entityPlayer = (EntityPlayer) ReflectionHelper.invoke(getHandle, player);
        this.old = player.spigot();
    }

    public int getPing() {
        return entityPlayer.f;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void sendMessage(BaseComponent... components) {
        old.sendMessage(components);
    }

    @Override
    public void sendMessage(UUID sender, BaseComponent... components) {
        old.sendMessage(sender, components);
    }

    @Override
    public void sendMessage(ChatMessageType position, UUID sender, BaseComponent... components) {
        old.sendMessage(position, sender, components);
    }

    @Override
    public void sendMessage(BaseComponent component) {
        old.sendMessage(component);
    }

    @Override
    public void sendMessage(UUID sender, BaseComponent component) {
        old.sendMessage(sender, component);
    }

    @Override
    public void sendMessage(ChatMessageType position, BaseComponent... components) {
        old.sendMessage(position, components);
    }

    @Override
    public void sendMessage(ChatMessageType position, BaseComponent component) {
        old.sendMessage(position, component);
    }

    public void addPersistentDataObject(String key, Object object){
        persistentDataContainer.remove(key);
        persistentDataContainer.put(key, object);
    }

    @Override
    public void sendMessage(@NotNull ChatMessageType position, @Nullable UUID sender, @NotNull BaseComponent component) {
        old.sendMessage(position, sender, component);
    }

    @Override
    public void setCollidesWithEntities(boolean collides) {
        old.setCollidesWithEntities(collides);
    }

    @Override
    public @NotNull Set<Player> getHiddenPlayers() {
        return old.getHiddenPlayers();
    }

    @Override
    public @NotNull InetSocketAddress getRawAddress() {
        return old.getRawAddress();
    }

    @Override
    public boolean getCollidesWithEntities() {
        return old.getCollidesWithEntities();
    }

    @Override
    public void respawn() {
        old.respawn();
    }

    public boolean containsPersistentDataObject(String key){
        return persistentDataContainer.containsKey(key);
    }

    public <T> T getPersistentDataObject(String key, Class<T> type){
        if(!persistentDataContainer.containsKey(key)) return null;
        Object object = persistentDataContainer.get(key);
        return (T) object;
    }

    public <T> T getPersistentDataObjectOrCompute(String key, Class<T> type, Function<Player, T> function){
        T t = getPersistentDataObject(key, type);
        if(t == null){
            t = function.apply(player);
            addPersistentDataObject(key, t);
        }
        return t;
    }

    public <T> T getPersistentDataObjectOrCompute(String key, T object){
        T t = (T) getPersistentDataObject(key, object.getClass());

        if(t == null){
            t = object;
            addPersistentDataObject(key, object);
        }
        return t;
    }

    public void removePersistentDataObject(String key){
        persistentDataContainer.remove(key);
    }

    public void inject(){
        ReflectionHelper.setFieldValue(spigotField, player, this);
        new PlayerInjectExtensionEvent(player, this).call();
    }

    public void title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.showTitle(Title.title(ComponentHelper.asComponent(title), ComponentHelper.asComponent(subtitle), Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeIn))));
    }

    public void title(String title, String subtitle){
        player.showTitle(Title.title(ComponentHelper.asComponent(title), ComponentHelper.asComponent(subtitle)));
    }

    public void asyncTeleport(Location location) {
        entityPlayer.c.teleport(location);
    }

    public void sendPacket(Packet<?> packet){
        if(entityPlayer == null){
            entityPlayer = (EntityPlayer) ReflectionHelper.invoke(getHandle, player);
        }
        if(entityPlayer != null && entityPlayer.c != null)
            entityPlayer.c.a(packet);
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public void setExperience(int experience){
        ExperienceHelper.getInstance().setTotalExperience(player, experience);
    }

    public void addExperience(int experience){
        ExperienceHelper.getInstance().start(experienceHelper -> {
            int totalExperience = experienceHelper.getTotalExperience(player);
            experienceHelper.setTotalExperience(player, experience + totalExperience);
        });
    }

    public void removeExperience(int experience){
        if(entityPlayer == null) return;
        addExperience(experience * -1);
    }

    public int getExperience() {
        return ExperienceHelper.getInstance().getTotalExperience(player);
    }

    public HumanEntity asHumanEntity(){
        return (HumanEntity) entityPlayer;
    }

    public void ifOnline(Runnable runnable) {
        if(player != null) runnable.run();
    }
}
