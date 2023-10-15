package nets.tools.objects;

import nets.tools.model.User;
import nets.tools.model.VanishedPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishedPlayerController implements VanishedPlayer {

    private final User user;
    private Player player;
    private boolean enabled;

    private List<Flag> flags = new ArrayList<>();

    public VanishedPlayerController(User user) {
        this.user = user;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setFlags(List<Flag> flags) {
        this.flags = flags;
    }

    @Override
    public List<Flag> getEnabledFlags() {
        return flags;
    }
}
