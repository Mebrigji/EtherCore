package nets.tools.model;

import net.kyori.adventure.bossbar.BossBar;
import nets.tools.objects.UserController;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface User extends QueueModel, Rank, Statistics{

    UUID getUuid();

    String getNickName();
    void setNickName(String nicKName);

    Optional<Player> getPlayer();
    void setPlayer(Player player);

    void setJoin(long date);
    long getJoin();

    void setQuit(long date);
    long getQuit();

    double getBalance();
    void setBalance(double balance);

    double parseDistance();

    int getTopPointsPosition();
    void setTopPointsPosition(int position);

    int getTopKillsPosition();
    void setTopKillsPosition(int position);

    int getTopDeathsPosition();
    void setTopDeathsPosition(int position);

    int getTopAssistsPosition();
    void setTopAssistsPosition(int position);

    TimedProgress sessionProgress();
    void sessionProgress(TimedProgress progress);

    long getLastDeath();
    void setLastDeath(long date);

    PlayerInventory getPlayerInventory();
    void setPlayerInventory(PlayerInventory playerInventory);
    CompletableFuture<PlayerInventory> preparePlayerInventory();

    Inventory getEnderChest();
    void setEnderChest(Inventory inventory);

    Optional<Brotherhood> getBrotherhood();
    void setBrotherhood(Brotherhood brotherhood);

    Optional<BossBar> getOverlay(int id);
    void createOverlay(int id, Consumer<BossBar> consumer);
    BossBar computeOverlay(int id, Consumer<BossBar> consumer);

    long getKitDelay(Kit kit);
    void updateKitDelay(Kit kit);

    void initialize(Player player);

    UserController asController();

}
