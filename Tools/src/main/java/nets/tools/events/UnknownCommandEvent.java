package nets.tools.events;

import net.saidora.api.events.PluginEvent;
import net.saidora.api.events.stereotype.EventHandler;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class UnknownCommandEvent implements PluginEvent<UnknownCommandEvent> {

    @EventHandler
    private static final Set<Consumer<UnknownCommandEvent>> events = new HashSet<>();

    private final String commandLine;
    private final Player player;

    private boolean cancelled;

    public UnknownCommandEvent(String commandLine, Player player) {
        this.commandLine = commandLine;
        this.player = player;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public UnknownCommandEvent getEvent() {
        return this;
    }

    @Override
    public Set<Consumer<UnknownCommandEvent>> getEventList() {
        return events;
    }
}
