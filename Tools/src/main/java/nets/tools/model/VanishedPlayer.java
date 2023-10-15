package nets.tools.model;

import org.bukkit.entity.Player;

import java.util.List;

public interface VanishedPlayer {

    boolean isEnabled();
    void setEnabled(boolean enabled);

    Player getPlayer();

    List<Flag> getEnabledFlags();

    enum Flag {

        BLOCK_BREAK,
        BLOCK_PLACE,

        PVP,
        PVE,

        BUCKET_FILL,
        BUCKET_EMPTY,

        ITEM_PICKUP,
        ITEM_DROP,

        SHOOT,
        INTERACTION_WITH_BLOCKS,

    }

}
