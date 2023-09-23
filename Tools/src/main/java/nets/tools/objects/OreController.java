package nets.tools.objects;

import nets.tools.model.Ore;
import org.bukkit.Material;

import java.util.List;

public class OreController implements Ore {

    private Material what;
    private double chance;
    private int y;
    private String displayName;
    private List<String> lore;

    public OreController(Material what, double chance, int y, String displayName, List<String> lore) {
        this.what = what;
        this.chance = chance;
        this.y = y;
        this.displayName = displayName;
        this.lore = lore;
    }

    @Override
    public Material what() {
        return what;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public List<String> lore() {
        return lore;
    }

    @Override
    public double chance() {
        return chance;
    }

    @Override
    public void chance(double chance) {
        this.chance = chance;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public void y(int y) {
        this.y = y;
    }
}
