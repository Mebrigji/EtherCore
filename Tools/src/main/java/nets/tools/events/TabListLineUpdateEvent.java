package nets.tools.events;

import net.saidora.api.events.PluginEvent;
import net.saidora.api.events.stereotype.EventHandler;
import net.saidora.api.extension.PlayerExtension;
import nets.tools.tablist.TabList;
import nets.tools.tablist.TabListEntry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class TabListLineUpdateEvent implements PluginEvent<TabListLineUpdateEvent> {

    @EventHandler
    private static final Set<Consumer<TabListLineUpdateEvent>> events = new HashSet<>();

    public TabListLineUpdateEvent(PlayerExtension extension, TabList tabList, int slot) {
        this.extension = extension;
        this.tabList = tabList;
        this.slot = slot;
    }

    @Override
    public TabListLineUpdateEvent getEvent() {
        return this;
    }

    @Override
    public Set<Consumer<TabListLineUpdateEvent>> getEventList() {
        return events;
    }

    private final PlayerExtension extension;
    private final TabList tabList;

    private int slot;

    private TabListEntry tabListEntry;

    public TabList getTabList() {
        return tabList;
    }

    public PlayerExtension getExtension() {
        return extension;
    }

    public TabListEntry getTabListEntry() {
        return tabListEntry;
    }

    public void setTabListEntry(TabListEntry tabListEntry) {
        this.tabListEntry = tabListEntry;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
