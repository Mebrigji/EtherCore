package nets.tools.manager;

import nets.tools.model.Brotherhood;
import nets.tools.objects.BrotherhoodController;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BrotherhoodManager {

    private static BrotherhoodManager instance;

    public static BrotherhoodManager getInstance() {
        if(instance == null){
            instance = new BrotherhoodManager();
        }
        return instance;
    }

    private final Map<String, Brotherhood> brotherhoodMap = new HashMap<>();

    public Map<String, Brotherhood> getBrotherhoodMap() {
        return brotherhoodMap;
    }

    public Optional<Brotherhood> getBrotherhood(Location location){
        return brotherhoodMap.values().stream().filter(brotherhood -> brotherhood.isInside(location)).findFirst();
    }

    public Brotherhood createBrotherhood(Player player, String tag, String name) {
        UserManager.getInstance().getUser(player).ifPresent(user -> {
            Location location = player.getLocation();
            location.setY(10);
            Brotherhood brotherhood = new BrotherhoodController(tag, name, location);
            brotherhood.owner(player.getName());
            brotherhood.addMember(user);
            user.setBrotherhood(brotherhood);
            brotherhoodMap.put(tag, brotherhood);
        });
        return null;
    }
}
