package nets.tools.model;

import org.bukkit.Material;

import java.util.List;

public interface Ore {

    Material what();

    String displayName();
    List<String> lore();

    double chance();
    void chance(double chance);

    int y();
    void y(int y);

}
