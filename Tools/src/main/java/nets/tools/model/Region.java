package nets.tools.model;

import org.bukkit.Location;

public interface Region {

    Location firstCorner();
    void firstCorner(Location location);

    Location secondCorner();
    void secondCorner(Location location);

    double getDistance(Location location);

    boolean isInside(Location location);

    boolean isOutside(Location location);

}
