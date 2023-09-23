package nets.tools.events;

import net.saidora.api.events.PluginEvent;
import net.saidora.api.events.stereotype.EventHandler;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class OrePreGenerateEvent implements PluginEvent<OrePreGenerateEvent> {

    @EventHandler
    private static final Set<Consumer<OrePreGenerateEvent>> events = new LinkedHashSet<>();

    private final Location location;
    private Material what;
    private double chance;
    private int belowY;

    public OrePreGenerateEvent(Location location, Material what, double chance, int belowY) {
        this.location = location;
        this.what = what;
        this.chance = chance;
        this.belowY = belowY;
    }

    public Location getLocation() {
        return location;
    }

    public Material getWhat() {
        return what;
    }

    public void setWhat(Material what) {
        this.what = what;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public int getBelowY() {
        return belowY;
    }

    public void setBelowY(int belowY) {
        this.belowY = belowY;
    }

    @Override
    public OrePreGenerateEvent getEvent() {
        return this;
    }

    @Override
    public Set<Consumer<OrePreGenerateEvent>> getEventList() {
        return events;
    }
}
