package nets.tools.manager;

import nets.tools.model.Kit;
import nets.tools.model.User;
import nets.tools.objects.KitController;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class KitManager {

    private static KitManager instance;

    public static KitManager getInstance() {
        if(instance == null) instance = new KitManager();
        return instance;
    }

    private final Map<String, Kit> kitMap = new LinkedHashMap<>();

    public Map<String, Kit> getKitMap() {
        return kitMap;
    }

    public Kit createKit(String id){
        return kitMap.computeIfAbsent(id, KitController::new);
    }

    public long getDelay(Player player, Kit kit){
        User user = UserManager.getInstance().getUser(player).orElse(null);
        if(user == null) return 0;
        return user.getKitDelay(kit);
    }

    public void updateDelay(Player player, Kit kit){
        UserManager.getInstance().getUser(player).ifPresent(user -> user.updateKitDelay(kit));
    }
}
