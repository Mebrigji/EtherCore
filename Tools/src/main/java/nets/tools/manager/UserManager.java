package nets.tools.manager;

import com.mongodb.client.model.Filters;
import net.saidora.api.extension.PlayerExtension;
import nets.tools.Main;
import nets.tools.model.User;
import nets.tools.objects.UserController;
import nets.tools.parsers.InventoryParser;
import nets.tools.parsers.PlayerInventoryParser;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class UserManager {

    private static UserManager instance;

    public static UserManager getInstance() {
        if(instance == null) instance = new UserManager();
        return instance;
    }

    public Optional<User> getUser(Player player){
        User user = null;
        PlayerExtension extension = PlayerExtension.getPlayerExtend(player);
        if(extension != null) user = extension.getPersistentDataObject("user", User.class);
        return Optional.ofNullable(user);
    }

    public User loadUser(Document document){
        User user = new UserController(UUID.fromString(document.getString("uuid")));
        user.setNickName(document.getString("nickName"));

        Document rank = (Document) document.get("rank");
        user.setPoints(rank.getInteger("points", 500));
        user.setKills(rank.getInteger("kills"));
        user.setDeaths(rank.getInteger("deaths"));
        user.setAssists(rank.getInteger("assists"));

        Document time = (Document) document.get("time");
        user.setJoin(time.getLong("join"));
        user.setQuit(time.getLong("quit"));
        user.setLastDeath(time.getLong("lastDeath"));

        Document statistics = (Document) document.get("statistics");
        user.setTraveledDistance(statistics.getDouble("distance"));
        user.setRegeneratedHealth(statistics.getDouble("regeneratedHealth"));
        user.setBlocksMined(statistics.getLong("blocksMined"));
        user.setBlocksPlaced(statistics.getLong("blocksPlaced"));
        user.setDamageGiven(statistics.getDouble("damageGiven"));
        user.setDamageTaken(statistics.getDouble("damageTaken"));
        user.setKilledEntities(statistics.getLong("killedEntities"));
        user.setKilledPlayers(statistics.getLong("killedPlayers"));
        user.setLogoutEscapes(statistics.getLong("logoutEscapes"));
        user.setTotalTimeSpend(statistics.getLong("timeSpend"));

        Document data = (Document) document.get("data");

        user.setPlayerInventory(PlayerInventoryParser.getPlayerInventory(data.getString("playerInventory")));
        user.setEnderChest(InventoryParser.getInventory(data.getString("enderChest")));
        return user;
    }

    public Optional<User> find(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player != null) return getUser(player);
        else {
            Document document = Main.getInstance().getConnector().getMongoDatabase().getCollection("Users").find(Filters.eq("nickName", playerName)).limit(1).first();
            return document == null ? Optional.empty() : Optional.ofNullable(loadUser(document));
        }
    }
}
