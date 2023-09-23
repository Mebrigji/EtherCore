package nets.tools.events;

import net.saidora.api.events.PluginEvent;
import net.saidora.api.events.stereotype.EventHandler;
import net.saidora.api.extension.PlayerExtension;
import nets.tools.tablist.TabList;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class TabListCreateEvent implements PluginEvent<TabListCreateEvent> {

    @EventHandler
    private static final Set<Consumer<TabListCreateEvent>> events = new HashSet<>();

    public TabListCreateEvent(PlayerExtension extension, TabList tabList) {
        this.extension = extension;
        this.tabList = tabList;
    }

    @Override
    public TabListCreateEvent getEvent() {
        return this;
    }

    @Override
    public Set<Consumer<TabListCreateEvent>> getEventList() {
        return events;
    }

    private final PlayerExtension extension;
    private final TabList tabList;

    public PlayerExtension getExtension() {
        return extension;
    }

    public TabList getTabList() {
        return tabList;
    }
}
