package nets.tools.events;

import net.saidora.api.events.PluginEvent;
import net.saidora.api.events.stereotype.EventHandler;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class PlayerCommandEvent implements PluginEvent<PlayerCommandEvent> {

    @EventHandler
    private static final Set<Consumer<PlayerCommandEvent>> events = new HashSet<>();

    private final Command command;
    private final Player player;

    private boolean cancelled;

    public PlayerCommandEvent(Command command, Player player) {
        this.command = command;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Command getCommand() {
        return command;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public PlayerCommandEvent getEvent() {
        return this;
    }

    @Override
    public Set<Consumer<PlayerCommandEvent>> getEventList() {
        return events;
    }
}
