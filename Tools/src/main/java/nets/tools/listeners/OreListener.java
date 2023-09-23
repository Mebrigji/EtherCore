package nets.tools.listeners;

import net.saidora.api.helpers.MathHelper;
import nets.tools.events.OrePreGenerateEvent;
import nets.tools.manager.OreManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class OreListener implements Listener {


    @EventHandler(ignoreCancelled = true)
    public void onFromTo(BlockFromToEvent event){
        Block toBlock = event.getToBlock();
        Material toBlockType = toBlock.getType();
        Block fromBlock = event.getBlock();

        if (generatesCobble(fromBlock.getType(), toBlock) || generatesCobble(toBlockType, fromBlock)) {
            if(hasAdjacentLava(toBlock)) {
                OreManager.getInstance().getList().forEach(ore -> {
                    OrePreGenerateEvent orePreGenerateEvent = new OrePreGenerateEvent(toBlock.getLocation(), ore.what(), ore.chance(), ore.y());
                    orePreGenerateEvent.call();
                    if (MathHelper.getChance(orePreGenerateEvent.getChance()) && event.getToBlock().getY() <= orePreGenerateEvent.getBelowY()) {
                        event.setCancelled(true);
                        event.getToBlock().setType(orePreGenerateEvent.getWhat());
                    }
                });
            }
        }
    }

    public boolean hasAdjacentLava(Block block) {
        for (BlockFace face : faces) {
            Block adjacentBlock = block.getRelative(face);
            if (adjacentBlock.getType() == Material.LAVA) {
                return true;
            }
        }
        return false;
    }

    private final BlockFace[] faces = new BlockFace[]{
            BlockFace.SELF,
            BlockFace.UP,
            BlockFace.DOWN,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    public boolean generatesCobble(Material type, Block b){
        Material mirror = (type == Material.WATER ? Material.LAVA : Material.WATER);
        for (BlockFace face : faces){
            Block r = b.getRelative(face, 1);
            if (r.getType() == mirror){
                return true;
            }
        }
        return false;
    }

}
