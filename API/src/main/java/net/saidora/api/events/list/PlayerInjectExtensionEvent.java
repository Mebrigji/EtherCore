package net.saidora.api.events.list;

import net.saidora.api.events.PluginEvent;
import net.saidora.api.events.stereotype.EventHandler;
import net.saidora.api.extension.PlayerExtension;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class PlayerInjectExtensionEvent implements PluginEvent<PlayerInjectExtensionEvent> {

    @EventHandler
    private static final Set<Consumer<PlayerInjectExtensionEvent>> events = new HashSet<>();


    private final Player player;
    private final PlayerExtension extension;

    public PlayerInjectExtensionEvent(Player player, PlayerExtension extension) {
        this.player = player;
        this.extension = extension;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerExtension getExtension() {
        return extension;
    }

    @Override
    public PlayerInjectExtensionEvent getEvent() {
        return this;
    }

    @Override
    public Set<Consumer<PlayerInjectExtensionEvent>> getEventList() {
        return events;
    }
}
