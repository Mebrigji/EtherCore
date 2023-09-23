package nets.tools;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import de.tr7zw.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.EnumChatFormat;
import net.minecraft.world.level.EnumGamemode;
import net.saidora.api.events.EventBuilder;
import net.saidora.api.events.list.PlayerInjectExtensionEvent;
import net.saidora.api.events.list.TaskEvent;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.helpers.TimeHelper;
import net.saidora.api.util.Tuple;
import nets.tools.commands.AdminKitCommand;
import nets.tools.commands.WalletCommand;
import nets.tools.commands.brotherhood.BrotherhoodCommand;
import nets.tools.commands.player.*;
import nets.tools.configuration.*;
import nets.tools.configuration.builder.TabLineBuilder;
import nets.tools.events.TabListCreateEvent;
import nets.tools.factory.*;
import nets.tools.db.MongodbConnector;
import nets.tools.listeners.OreListener;
import nets.tools.listeners.PlayerListener;
import nets.tools.manager.ProgressManager;
import nets.tools.manager.UserManager;
import nets.tools.objects.IndividualNameTagController;
import nets.tools.objects.PlayerInventoryController;
import nets.tools.objects.UserController;
import nets.tools.objects.interactiveItems.ExpBottle;
import nets.tools.tablist.TabList;
import nets.tools.commands.admin.*;
import nets.tools.model.IndividualNameTag;
import nets.tools.model.TimedProgress;
import nets.tools.model.User;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

@Plugin(name = "Ether-Tools", version = "1.0-SNAPSHOT")
@Dependency("Ether-API")
@ApiVersion(ApiVersion.Target.v1_20)
@Author("Saidora")
public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Getter private MongodbConnector connector;

    @Getter private WalletConfiguration walletConfiguration;
    @Getter private Configuration configuration;
    @Getter private Messages messages;
    @Getter private TabListConfiguration tabListConfiguration;
    @Getter private BrotherhoodConfiguration brotherhoodConfiguration;


    @Override
    public void onLoad() {
        instance = this;
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

        this.connector = new MongodbConnector(configuration.MONGO_URI, this.configuration.MONGO_TABLE, walletConfiguration.WALLET_ID);

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

        TabFactory tabFactory = new TabFactory();
        tabFactory.setHeader(tabListConfiguration.HEADER);
        tabFactory.setFooter(tabListConfiguration.FOOTER);

        Map<Integer, TabLineBuilder> tabLineBuilderMap = new HashMap<>();

        tabListConfiguration.CELLS.forEach((integer, s) -> tabLineBuilderMap.put(integer, new TabLineBuilder(integer, s, "", "", EnumGamemode.e, 0)));
        tabFactory.setCells(tabLineBuilderMap);
        tabFactory.setup();

        new EventBuilder<>(PlayerInjectExtensionEvent.class, event -> {
            Document document = this.connector.getMongoDatabase().getCollection("Users").find(Filters.eq("uuid", event.getPlayer().getUniqueId().toString())).limit(1).first();

            User user;

            if (document == null) {
                user = new UserController(event.getPlayer().getUniqueId());
                user.setPoints(500);
                user.setJoin(System.currentTimeMillis());
                user.setPlayerInventory(new PlayerInventoryController());
                user.setEnderChest(Bukkit.createInventory(null, 4 * 9, ComponentHelper.asComponent("<gradient:#740DCA:#B778EC>⚛ ᴡɪᴇʟᴏᴡʏᴍɪᴀʀᴏᴡᴀ sᴋʀᴢʏɴɪᴀ</gradient>")));
                user.setLastDeath(System.currentTimeMillis());
            } else {
                user = UserManager.getInstance().loadUser(document);
            }

            user.setJoin(System.currentTimeMillis());
            user.initialize(event.getPlayer());
            user.setBalance(this.connector.getBalance(user.getNickName()));

            if(user.getLastDeath() == 0) user.setLastDeath(System.currentTimeMillis());

            TimedProgress progress = ProgressManager.getInstance().createTimed(TimeUnit.MINUTES.toMillis(5));

            progress.afterMeetingExpectations(() -> {
                user.setBalance(user.getBalance() + 0.001);
                progress.requiredValue(System.currentTimeMillis() + progress.total());
            });

            user.sessionProgress(progress);
            user.add();

            IndividualNameTag nameTag = getIndividualNameTag(event, user);

            event.getExtension().addPersistentDataObject("nameTag", nameTag);

            TabList tabList = new TabList(event.getExtension(), tabFactory);
            TabListCreateEvent createEvent = new TabListCreateEvent(event.getExtension(), tabList);
            createEvent.call();

            event.getExtension().addPersistentDataObject("tabList", createEvent.getTabList());

        });

        AtomicLong tabListDelay = new AtomicLong();
        new EventBuilder<>(TaskEvent.class, taskEvent -> {
            if(tabListDelay.get() > System.currentTimeMillis()) return;
            tabListDelay.set(System.currentTimeMillis() + 500);

            taskEvent.execute(player -> {
                PlayerExtension.getPlayerExtend(player, extension -> {

                    TabList tabList = extension.getPersistentDataObject("tabList", TabList.class);
                    if(tabList != null) tabList.create();

                    IndividualNameTag individualNameTag = extension.getPersistentDataObject("nameTag", IndividualNameTag.class);
                    if(individualNameTag != null) individualNameTag.initializer();

                });
            });
        });

        AtomicLong updateDelay = new AtomicLong();
        new EventBuilder<>(TaskEvent.class, taskEvent -> {
            if(updateDelay.get() > System.currentTimeMillis()) return;
            updateDelay.set(System.currentTimeMillis() + 5000);

            taskEvent.execute(player -> UserManager.getInstance().getUser(player).ifPresent(user -> {
                user.setBalance(this.connector.getBalance(user.getNickName()));
            }));
        });

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
    }

    @NotNull
    private IndividualNameTag getIndividualNameTag(PlayerInjectExtensionEvent event, User user) {
        IndividualNameTag nameTag = new IndividualNameTagController(event.getPlayer());
        nameTag.updatePrefix(player -> {
            AtomicReference<Component> componentReference = new AtomicReference<>(Component.empty());
            AtomicReference<EnumChatFormat> colorReference = new AtomicReference<>(EnumChatFormat.h);

            user.getBrotherhood().ifPresent(brotherhood -> {
                UserManager.getInstance().getUser(player).flatMap(User::getBrotherhood).ifPresentOrElse(brotherhood1 -> {
                    if(brotherhood.equals(brotherhood1)) {
                        componentReference.set(ComponentHelper.asComponent("<#54C506>[<tag>] ", Placeholder.parsed("tag", (brotherhood.owner().equals(user.getNickName()) ? "⭐ " : "") + brotherhood.tag())));
                        colorReference.set(EnumChatFormat.k);
                    }
                    else if(brotherhood.allies().contains(brotherhood1)) {
                        componentReference.set(ComponentHelper.asComponent("<#E8AD10>[<tag>] ", Placeholder.parsed("tag", (brotherhood.owner().equals(user.getNickName()) ? "⭐ " : "") + brotherhood.tag())));
                        colorReference.set(EnumChatFormat.o);
                    }
                    else {
                        componentReference.set(ComponentHelper.asComponent("<#E82A10>[<tag>] ", Placeholder.parsed("tag", (brotherhood.owner().equals(user.getNickName()) ? "⭐ " : "") + brotherhood.tag())));
                        colorReference.set(EnumChatFormat.e);
                    }
                }, () -> {
                    componentReference.set(ComponentHelper.asComponent("<#E82A10>[<tag>] ", Placeholder.parsed("tag", (brotherhood.owner().equals(user.getNickName()) ? "⭐ " : "") + brotherhood.tag())));
                    colorReference.set(EnumChatFormat.e);
                });
            });

            return new Tuple<>() {
                @Override
                public Component getLeft() {
                    return componentReference.get();
                }

                @Override
                public EnumChatFormat getRight() {
                    return colorReference.get();
                }
            };
        });
        return nameTag;
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
