package nets.tools.utils;

import nets.tools.manager.UserManager;
import nets.tools.model.User;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BukkitExtension {

    private static BukkitExtension instance;

    public static BukkitExtension getInstance() {
        if(instance == null) instance = new BukkitExtension();
        return instance;
    }

    public List<Player> getVisiblePlayers(){
        return getBukkitServer().getOnlinePlayers()
                .stream()
                .map(player -> UserManager.getInstance().getUser(player).orElse(null))
                .filter(Objects::nonNull)
                .filter(user -> user.asVanishedPlayer().isEnabled())
                .map(user -> user.getPlayer().orElse(null))
                .toList();
    }

    public Optional<User> getUser(String exactNickName){
        return UserManager.getInstance().find(exactNickName);
    }

    public Server getBukkitServer(){
        return Bukkit.getServer();
    }
}
