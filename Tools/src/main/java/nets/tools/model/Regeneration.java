package nets.tools.model;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.Map;

public interface Regeneration {

    Brotherhood ownedBy();

    Map<Location, BlockState> toRegeneration();
    void add(Block block);

}
