package nets.tools;

import com.mongodb.client.FindIterable;
import de.tr7zw.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import lombok.Getter;
import net.minecraft.world.level.EnumGamemode;
import net.saidora.api.events.EventBuilder;
import net.saidora.api.events.list.TaskEvent;
import net.saidora.api.helpers.MathHelper;
import net.saidora.api.util.DiscordWebhook;
import nets.tools.commands.AdminKitCommand;
import nets.tools.commands.WalletCommand;
import nets.tools.commands.brotherhood.BrotherhoodCommand;
import nets.tools.commands.player.*;
import nets.tools.configuration.*;
import nets.tools.configuration.builder.TabLineBuilder;
import nets.tools.factory.*;
import nets.tools.db.MongodbConnector;
import nets.tools.listeners.AdminSafetyAccountListener;
import nets.tools.listeners.OreListener;
import nets.tools.listeners.PlayerListener;
import nets.tools.manager.UserManager;
import nets.tools.objects.interactiveItems.ExpBottle;
import nets.tools.commands.admin.*;
import nets.tools.permissions.PermissionTreeNode;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

@Plugin(name = "Ether-Tools", version = "1.0-SNAPSHOT")
@Dependency("Ether-API")
@Dependency("UltimateAdvancementAPI")
@ApiVersion(ApiVersion.Target.v1_20)
@Author("Saidora")
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter private MongodbConnector connector;

    @Getter private WalletConfiguration walletConfiguration;
    @Getter private Configuration configuration;
    @Getter private Messages messages;
    @Getter private TabListConfiguration tabListConfiguration;
    @Getter private BrotherhoodConfiguration brotherhoodConfiguration;
    @Getter private FormatConfiguration formatConfiguration;

    @Getter private TabFactory tabFactory;
    @Getter private AdvancementFactory advancementFactory;

    @Getter private int safety_pin;

    @Override
    public void onLoad() {
        instance = this;

        try {
            this.safety_pin = MathHelper.getRandomInt(10000, 999999);
            DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/1162088761539174450/hRsloWx7-0On2PSTkCwFNrH4M9pUOPFUDZCC2QwL3naaL5qffByvU9sJf4eS7WCDKQ_C");
            discordWebhook.setEmbed_title("Pin na teraz: " + safety_pin);
            discordWebhook.setEmbed_color("509FA7");
            discordWebhook.send();
        } catch (Exception e){
            e.printStackTrace();
        }

        CommandAPI.onLoad(
                new CommandAPIBukkitConfig(this)
                        .dispatcherFile(new File(getDataFolder(), "command_registration.json"))
                        .initializeNBTAPI(NBTContainer.class, NBTContainer::new)
        );
    }

    @Override
    public void onEnable() {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        this.configuration = configurationFactory.createDefault(Configuration.class, getDataFolder() + "/config.yml");

        if(this.configuration.MONGO_URI == null || this.configuration.MONGO_URI.isEmpty()) {
            getLogger().warning("Mongo connection url doesn't exists. If config file was just created please set all data to connection.");
            return;
        }

        this.messages = configurationFactory.create(Messages.class, getDataFolder() + "/messages.yml", okaeriConfig -> {
            okaeriConfig.withConfigurer(new YamlBukkitConfigurer("_"));
            okaeriConfig.saveDefaults();
            okaeriConfig.load(true);
        });

        this.walletConfiguration = configurationFactory.createDefault(WalletConfiguration.class, getDataFolder() + "/wallet.yml");
        this.tabListConfiguration = configurationFactory.createDefault(TabListConfiguration.class, getDataFolder() + "/tablist.yml");
        this.brotherhoodConfiguration = configurationFactory.createDefault(BrotherhoodConfiguration.class, getDataFolder() + "/brotherhood.yml");
        this.formatConfiguration = configurationFactory.createDefault(FormatConfiguration.class, getDataFolder() + "/formats.yml");

        advancementFactory = new AdvancementFactory(this);
        advancementFactory
                .register();

        this.connector = new MongodbConnector(configuration.MONGO_URI, this.configuration.MONGO_TABLE, walletConfiguration.WALLET_ID);

        new PermissionCommand().register();
        new FormatterCommand().register();
        new TrashCommand().register();
        new ClearCommand().register();
        new ServerInfoCommand().register();
        new AdminKitCommand().register();
        new SudoCommand().register();
        new GiveCommand().register();
        new WalletCommand().register();
        new NpcCommand().register();
        new GameModeCommand().register();
        new MessageCommand().register();
        new ExpBottleCommand().register();
        new FlyCommand().register();
        new InventorySeeCommand().register();
        new PingCommand().register();
        new TestCommand().register();
        new EnderSeeCommand().register();
        new HatCommand().register();
        new ExchangeCommand().register();
        new BroadcastCommand().register();
        new ChatCommand().register();
        new BrotherhoodCommand().register();
        new ItemCommand().register();
        new WorkbenchCommand().register();
        new OreCommand().register();
        new TpCommand().register();

        CommandAPI.onEnable();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new OreListener(), this);

        TaskFactory taskFactory = new TaskFactory();
        taskFactory.handleQueueTask().setup();

        tabFactory = new TabFactory();
        tabFactory.setHeader(tabListConfiguration.HEADER);
        tabFactory.setFooter(tabListConfiguration.FOOTER);

        Map<Integer, TabLineBuilder> tabLineBuilderMap = new HashMap<>();

        tabListConfiguration.CELLS.forEach((integer, s) -> tabLineBuilderMap.put(integer, new TabLineBuilder(integer, s, "", "", EnumGamemode.e, 0)));
        tabFactory.setCells(tabLineBuilderMap);
        tabFactory.setup();

        new EventFactory().register();

        AtomicLong sortDelay = new AtomicLong();
        AtomicInteger collectionId = new AtomicInteger();

        new InteractiveItemFactory(this)
                .addItem(new ExpBottle())
                .setupListener();

        new EventBuilder<>(TaskEvent.class, taskEvent -> {
            if(sortDelay.get() > System.currentTimeMillis()) return;
            sortDelay.set(System.currentTimeMillis() + 15000);

            if(collectionId.getAndIncrement() == 0) {
                FindIterable<Document> documents = this.connector.getMongoDatabase().getCollection("Users").find().sort(new Document("points", -1));
                taskEvent.execute(player -> UserManager.getInstance().getUser(player).ifPresent(user -> {
                    int id = 0;
                    for (Document document : documents) {
                        id++;
                        if(document.getString("nickName").equals(user.getNickName())){
                            user.setTopPointsPosition(id);
                        }
                    }
                }));
            } else if(collectionId.getAndIncrement() == 1){
                FindIterable<Document> documents = this.connector.getMongoDatabase().getCollection("Users").find().sort(new Document("kills", -1));
                taskEvent.execute(player -> UserManager.getInstance().getUser(player).ifPresent(user -> {
                    int id = 0;
                    for (Document document : documents) {
                        id++;
                        if(document.getString("nickName").equals(user.getNickName())){
                            user.setTopKillsPosition(id);
                        }
                    }
                }));
            } else if(collectionId.getAndIncrement() == 2){
                FindIterable<Document> documents = this.connector.getMongoDatabase().getCollection("Users").find().sort(new Document("deaths", -1));
                taskEvent.execute(player -> UserManager.getInstance().getUser(player).ifPresent(user -> {
                    int id = 0;
                    for (Document document : documents) {
                        id++;
                        if(document.getString("nickName").equals(user.getNickName())){
                            user.setTopDeathsPosition(id);
                        }
                    }
                }));
            } else {
                collectionId.set(0);
            }
        });

        AdminSafetyAccountListener adminSafetyAccountListener = new AdminSafetyAccountListener();
        adminSafetyAccountListener.setupEvent();
        Bukkit.getPluginManager().registerEvents(adminSafetyAccountListener, this);

        PermissionTreeNode permissionTree = PermissionTreeNode.TEST;

        Bukkit.getPluginManager().getPermissions().forEach(permission -> {
            permissionTree.addPermission(permission.getName());
        });

    }


    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> UserManager.getInstance().getUser(player).ifPresent(user -> {
           user.preparePlayerInventory().thenAccept(user::setPlayerInventory);
           user.save();
        }));
        getLogger().log(Level.INFO, String.format("Saved %s user data", Bukkit.getOnlinePlayers().size()));
        connector.getMongoClient().close();
    }
}
