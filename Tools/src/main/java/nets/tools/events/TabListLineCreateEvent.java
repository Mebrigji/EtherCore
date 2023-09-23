package nets.tools.events;

import net.minecraft.world.level.EnumGamemode;
import net.saidora.api.events.PluginEvent;
import net.saidora.api.events.stereotype.EventHandler;
import net.saidora.api.extension.PlayerExtension;
import nets.tools.tablist.TabList;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class TabListLineCreateEvent implements PluginEvent<TabListLineCreateEvent> {

    @EventHandler
    private static final Set<Consumer<TabListLineCreateEvent>> events = new HashSet<>();

    public TabListLineCreateEvent(PlayerExtension extension, TabList tabList, int slot) {
        this.extension = extension;
        this.tabList = tabList;
        this.slot = slot;
    }

    @Override
    public TabListLineCreateEvent getEvent() {
        return this;
    }

    @Override
    public Set<Consumer<TabListLineCreateEvent>> getEventList() {
        return events;
    }

    private final PlayerExtension extension;
    private final TabList tabList;

    private int slot;

    private String context, texture, signature;
    private EnumGamemode enumGamemode;
    private int ping;

    public PlayerExtension getExtension() {
        return extension;
    }

    public TabList getTabList() {
        return tabList;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public EnumGamemode getEnumGamemode() {
        return enumGamemode;
    }

    public void setEnumGamemode(EnumGamemode enumGamemode) {
        this.enumGamemode = enumGamemode;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
