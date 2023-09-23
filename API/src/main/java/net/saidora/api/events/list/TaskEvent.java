package net.saidora.api.events.list;

import net.saidora.api.events.PluginEvent;
import net.saidora.api.events.stereotype.EventHandler;
import net.saidora.api.events.stereotype.EventLimiter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;

public class TaskEvent implements PluginEvent<TaskEvent> {

    @EventHandler
    @EventLimiter(size = 5)
    private static final Set<Consumer<TaskEvent>> events = new HashSet<>();

    private final Collection<? extends Player> playerList;

    public TaskEvent(Collection<? extends Player> playerList) {
        this.playerList = playerList;
    }

    private List<Consumer<Player>> execute = new ArrayList<>();

    public void execute(Consumer<Player> execute){
        this.execute.add(execute);
    }

    public Collection<? extends Player> getPlayerList() {
        return playerList;
    }

    public List<Consumer<Player>> getExecute() {
        return execute;
    }

    @Override
    public TaskEvent getEvent() {
        return this;
    }

    @Override
    public Set<Consumer<TaskEvent>> getEventList() {
        return events;
    }
}
