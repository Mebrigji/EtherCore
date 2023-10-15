package nets.tools.events;

import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.saidora.api.events.PluginEvent;
import net.saidora.api.events.stereotype.EventHandler;
import nets.tools.model.User;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerSendChatMessageEvent implements PluginEvent<PlayerSendChatMessageEvent> {

    @EventHandler
    private static final Set<Consumer<PlayerSendChatMessageEvent>> events = new HashSet<>();

    @Getter private final User user;
    @Getter private final Player player;
    @Getter private Function<Player, String> formatForViewer;
    @Getter private String message;

    private final List<TagResolver> resolverList = new ArrayList<>();

    @Getter private boolean cancelled;

    public PlayerSendChatMessageEvent(User user, Player player, Function<Player, String> formatForViewer, String message) {
        this.user = user;
        this.player = player;
        this.formatForViewer = formatForViewer;
        this.message = message;
    }

    public void addTag(TagResolver tagResolver){
        this.resolverList.add(tagResolver);
    }

    public List<TagResolver> getTags(){
        return this.resolverList;
    }

    public void setFormatForViewer(Function<Player, String> formatForViewer) {
        this.formatForViewer = formatForViewer;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public PlayerSendChatMessageEvent getEvent() {
        return this;
    }

    @Override
    public Set<Consumer<PlayerSendChatMessageEvent>> getEventList() {
        return events;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
