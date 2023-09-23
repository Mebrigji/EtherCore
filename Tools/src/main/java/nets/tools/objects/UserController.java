package nets.tools.objects;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.helpers.ComponentHelper;
import nets.tools.Main;
import nets.tools.manager.QueueManager;
import nets.tools.model.*;
import nets.tools.parsers.InventoryParser;
import nets.tools.parsers.PlayerInventoryParser;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class UserController implements User {

    private final UUID uuid;
    private String nickName;

    private Player player;
    private Brotherhood brotherhood;

    private int points = 500, kills, deaths, assists;
    private double balance;

    private long lastDeath;

    private int pos_points, pos_kills, pos_deaths, pos_assists, level;

    private long quit, join, total, blocksMined, blocksPlaced, logoutEscapes, killedEntities, killedPlayers, licked_enchanted_golden_apples;
    private double distance, regeneratedHealth, damageGiven, damageTaken;

    private PlayerInventory playerInventory;
    private Inventory enderChest;

    private final Map<Integer, BossBar> overlayMap  = new HashMap<>();

    private TimedProgress sessionProgress;

    public UserController(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    @Override
    public void setNickName(String nicKName) {
        this.nickName = nicKName;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public void setKills(int kills) {
        this.kills = kills;
    }

    @Override
    public int getDeaths() {
        return deaths;
    }

    @Override
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @Override
    public int getAssists() {
        return assists;
    }

    @Override
    public void setAssists(int assists) {
        this.assists = assists;
    }

    @Override
    public void setJoin(long date) {
        this.join = date;
    }

    @Override
    public long getJoin() {
        return join;
    }

    @Override
    public void setQuit(long date) {
        this.quit = date;
    }

    @Override
    public long getQuit() {
        return quit;
    }

    @Override
    public void setTotalTimeSpend(long timeSpend) {
        this.total = timeSpend;
    }

    @Override
    public long getBlocksMined() {
        return blocksMined;
    }

    @Override
    public void setBlocksMined(long blocksMined) {
        this.blocksMined = blocksMined;
    }

    @Override
    public long getBlocksPlaced() {
        return blocksPlaced;
    }

    @Override
    public void setBlocksPlaced(long blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }

    @Override
    public double getDamageGiven() {
        return damageGiven;
    }

    @Override
    public void setDamageGiven(double damageGiven) {
        this.damageGiven = damageGiven;
    }

    @Override
    public double getDamageTaken() {
        return damageTaken;
    }

    @Override
    public void setDamageTaken(double damageTaken) {
        this.damageTaken = damageTaken;
    }

    @Override
    public long getKilledEntities() {
        return killedEntities;
    }

    @Override
    public void setKilledEntities(long entityCount) {
        this.killedEntities = entityCount;
    }

    @Override
    public long getKilledPlayers() {
        return killedPlayers;
    }

    @Override
    public void setKilledPlayers(long killedPlayers) {
        this.killedPlayers = killedPlayers;
    }

    @Override
    public long getLogoutEscapes() {
        return logoutEscapes;
    }

    @Override
    public void setLogoutEscapes(long escapes) {
        this.logoutEscapes = escapes;
    }

    @Override
    public double getRegeneratedHealth() {
        return regeneratedHealth;
    }

    @Override
    public void setRegeneratedHealth(double regeneratedHealth) {
        this.regeneratedHealth = regeneratedHealth;
    }

    @Override
    public long getLickedEnchantedGoldenApples() {
        return licked_enchanted_golden_apples;
    }

    @Override
    public void setLickedEnchantedGoldenApples(long lickedEnchantedGoldenApples) {
        this.licked_enchanted_golden_apples = lickedEnchantedGoldenApples;
    }

    @Override
    public long getTotalTimeSpend() {
        return this.total;
    }

    @Override
    public void setTraveledDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public double getTraveledDistance() {
        return distance;
    }

    public double parseDistance(){
        return distance / 1000.0;
    }

    @Override
    public int getTopPointsPosition() {
        return pos_points;
    }

    @Override
    public void setTopPointsPosition(int position) {
        this.pos_points = points;
    }

    @Override
    public int getTopKillsPosition() {
        return pos_kills;
    }

    @Override
    public void setTopKillsPosition(int position) {
        pos_kills = position;
    }

    @Override
    public int getTopDeathsPosition() {
        return pos_deaths;
    }

    @Override
    public void setTopDeathsPosition(int position) {
        pos_deaths = position;
    }

    @Override
    public int getTopAssistsPosition() {
        return pos_assists;
    }

    @Override
    public void setTopAssistsPosition(int position) {
        pos_assists = position;
    }

    @Override
    public TimedProgress sessionProgress() {
        return sessionProgress;
    }

    @Override
    public void sessionProgress(TimedProgress progress) {
        this.sessionProgress = progress;
    }

    @Override
    public long getLastDeath() {
        return lastDeath;
    }

    @Override
    public void setLastDeath(long date) {
        this.lastDeath = date;
    }

    @Override
    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    @Override
    public void setPlayerInventory(PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
    }

    @Override
    public CompletableFuture<PlayerInventory> preparePlayerInventory() {
        PlayerInventory inventory = new PlayerInventoryController();

        EntityEquipment equipment = player.getEquipment();
        inventory.setHelmet(equipment.getHelmet());
        inventory.setChestPlate(equipment.getChestplate());
        inventory.setLeggings(equipment.getLeggings());
        inventory.setBoots(equipment.getBoots());
        inventory.setMainHand(equipment.getItemInMainHand());
        inventory.setSecondHand(equipment.getItemInOffHand());
        inventory.setContents(Arrays.asList(player.getInventory().getContents()));

        return CompletableFuture.completedFuture(inventory);
    }

    @Override
    public Inventory getEnderChest() {
        if(enderChest == null) enderChest = Bukkit.createInventory(null, 4 * 9, ComponentHelper.asComponent("<gradient:#740DCA:#B778EC>⚛ ᴡɪᴇʟᴏᴡʏᴍɪᴀʀᴏᴡᴀ sᴋʀᴢʏɴɪᴀ</gradient>"));
        return enderChest;
    }

    @Override
    public void setEnderChest(Inventory inventory) {
        this.enderChest = inventory;
    }

    @Override
    public Optional<Brotherhood> getBrotherhood() {
        return Optional.ofNullable(brotherhood);
    }

    @Override
    public void setBrotherhood(Brotherhood brotherhood) {
        this.brotherhood = brotherhood;
    }

    @Override
    public Optional<BossBar> getOverlay(int id) {
        return Optional.ofNullable(overlayMap.get(id));
    }

    @Override
    public void createOverlay(int id, Consumer<BossBar> consumer) {
        overlayMap.computeIfAbsent(id, $ -> {
            BossBar bossBar = BossBar.bossBar(Component.empty(), 0, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            consumer.accept(bossBar);
            return bossBar;
        });
    }

    @Override
    public BossBar computeOverlay(int id, Consumer<BossBar> consumer) {
        return overlayMap.computeIfAbsent(id, $ -> {
            BossBar bossBar = BossBar.bossBar(Component.empty(), 0, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            consumer.accept(bossBar);
            return bossBar;
        });
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public long getExperiencePoints() {
        return Math.max(0, ((points + kills) - deaths) / 15);
    }

    @Override
    public long getRequiredExperiencePoints() {
        return (getLevel() + 1) * 1500L;
    }

    @Override
    public long getKitDelay(Kit kit) {
        return 0;
    }

    @Override
    public void updateKitDelay(Kit kit) {

    }

    @Override
    public void initialize(Player player) {
        PlayerExtension.getPlayerExtend(player, extension -> {
            extension.addPersistentDataObject("user", this);

            this.player = player;
            this.nickName = player.getName();
        });
    }

    @Override
    public UserController asController() {
        return this;
    }

    @Override
    public void save() {
        MongoCollection<Document> collection = Main.getInstance().getConnector().getMongoDatabase().getCollection("Users");
        collection.updateOne(new Document("uuid", uuid.toString()), Updates.combine(
                Updates.set("nickName", nickName),
                Updates.set("rank", new Document()
                        .append("points", points)
                        .append("kills", kills)
                        .append("deaths", deaths)
                        .append("assists", assists)),
                Updates.set("time", new Document()
                        .append("join", join)
                        .append("quit", quit)
                        .append("lastDeath", lastDeath)),
                Updates.set("statistics", new Document()
                        .append("distance", distance)
                        .append("regeneratedHealth", regeneratedHealth)
                        .append("blocksMined", blocksMined)
                        .append("blocksPlaced", blocksPlaced)
                        .append("damageGiven", damageGiven)
                        .append("damageTaken", damageGiven)
                        .append("killedEntities", killedEntities)
                        .append("killedPlayers", killedPlayers)
                        .append("logoutEscapes", logoutEscapes)
                        .append("timeSpend", total)),
                Updates.set("data", new Document()
                        .append("playerInventory", PlayerInventoryParser.parsePlayerInventory(playerInventory))
                        .append("enderChest", InventoryParser.parseInventory(enderChest)))
        ), new UpdateOptions().upsert(true));
    }

    @Override
    public void add() {
        QueueManager.getInstance().add(this);
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
        Main.getInstance().getConnector().setBalance(nickName, balance);
    }
}
