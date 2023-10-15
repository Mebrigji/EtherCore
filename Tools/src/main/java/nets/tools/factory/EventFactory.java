package nets.tools.factory;

import com.mongodb.client.model.Filters;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.EnumChatFormat;
import net.saidora.api.events.EventBuilder;
import net.saidora.api.events.list.PlayerInjectExtensionEvent;
import net.saidora.api.events.list.TaskEvent;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.notifications.NotificationBuilder;
import net.saidora.api.util.Tuple;
import nets.tools.Main;
import nets.tools.configuration.FormatConfiguration;
import nets.tools.db.MongodbConnector;
import nets.tools.events.PlayerCommandEvent;
import nets.tools.events.PlayerSendChatMessageEvent;
import nets.tools.events.TabListCreateEvent;
import nets.tools.events.UnknownCommandEvent;
import nets.tools.manager.ProgressManager;
import nets.tools.manager.UserManager;
import nets.tools.model.IndividualNameTag;
import nets.tools.model.TimedProgress;
import nets.tools.model.User;
import nets.tools.objects.IndividualNameTagController;
import nets.tools.objects.PlayerInventoryController;
import nets.tools.objects.UserController;
import nets.tools.objects.VanishedPlayerController;
import nets.tools.tablist.TabList;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class EventFactory {

    public void register(){

        MongodbConnector connector = Main.getInstance().getConnector();
        FormatConfiguration formatConfiguration = Main.getInstance().getFormatConfiguration();


        new EventBuilder<>(PlayerInjectExtensionEvent.class, event -> {
            Document document = connector.getMongoDatabase().getCollection("Users").find(Filters.eq("uuid", event.getPlayer().getUniqueId().toString())).limit(1).first();

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
            user.setBalance(connector.getBalance(user.getNickName()));

            if(user.getLastDeath() == 0) user.setLastDeath(System.currentTimeMillis());

            user.asController().setVanishedPlayer(new VanishedPlayerController(user));
            ((VanishedPlayerController)user.asVanishedPlayer()).setPlayer(event.getPlayer());

            TimedProgress progress = ProgressManager.getInstance().createTimed(TimeUnit.MINUTES.toMillis(5));

            progress.afterMeetingExpectations(() -> {
                //user.setBalance(user.getBalance() + 0.002);
                progress.requiredValue(System.currentTimeMillis() + progress.total());
            });

            user.sessionProgress(progress);
            user.add();

            IndividualNameTag nameTag = getIndividualNameTag(event, user);

            event.getExtension().addPersistentDataObject("nameTag", nameTag);

            TabList tabList = new TabList(event.getExtension(), Main.getInstance().getTabFactory());
            TabListCreateEvent createEvent = new TabListCreateEvent(event.getExtension(), tabList);
            createEvent.call();

            event.getExtension().addPersistentDataObject("tabList", createEvent.getTabList());

        });
        new EventBuilder<>(PlayerSendChatMessageEvent.class, event -> {
            User user = event.getUser();

            String format = formatConfiguration.formats.getOrDefault("default", "<guild> <points> <player>: <message>");

            String string = formatConfiguration.formats.get(user.getHighestGroup().name());
            if(string != null) format = string;

            String finalFormat = format;



            event.setFormatForViewer(player -> {
                var ref = new Object() {
                    String playerFormat = finalFormat;
                };

                ref.playerFormat = ref.playerFormat.replace("<points>", String.valueOf(user.getPoints()));

                user.getBrotherhood().ifPresent(brotherhood -> {
                    UserManager.getInstance().getUser(player).flatMap(User::getBrotherhood).ifPresentOrElse(brotherhood1 -> {
                        if(brotherhood.equals(brotherhood1)) ref.playerFormat = ref.playerFormat.replace("<guild>", formatConfiguration.GUILD_FORMAT_EXISTS_OWN.replace("<guild>", brotherhood.tag()));
                        else if(brotherhood.allies().contains(brotherhood1)) ref.playerFormat = ref.playerFormat.replace("<guild>", formatConfiguration.GUILD_FORMAT_EXISTS_ALLY.replace("<guild>", brotherhood.tag()));
                        else ref.playerFormat = ref.playerFormat.replace("<guild>", formatConfiguration.GUILD_FORMAT_EXISTS_ENEMY.replace("<guild>", brotherhood.tag()));
                    }, () -> ref.playerFormat = ref.playerFormat.replace("<guild>", formatConfiguration.GUILD_FORMAT_EXISTS_ENEMY.replace("<guild>", brotherhood.tag())));
                });

                ref.playerFormat = ref.playerFormat.replace("<guild>", formatConfiguration.GUILD_FORMAT_EMPTY);

                return ref.playerFormat;
            });

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
                user.setBalance(connector.getBalance(user.getNickName()));
            }));
        });

        new EventBuilder<>(UnknownCommandEvent.class, event -> {
            event.setCancelled(true);
            String commandLine = event.getCommandLine();
            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, """
                    Polecenie <command> nie istnieje.
                    Użyj -> <hover:show_text:"Kliknij, aby użyć polecenia &e/help"><click:run_command:"/help">&e/help</click></hover>&f, aby wyświetlić liste poleceń.""", Placeholder.parsed("command", event.getCommandLine()))
                    .send(event.getPlayer());
        });

        new EventBuilder<>(PlayerCommandEvent.class, event -> {
            Command command = event.getCommand();
            Player player = event.getPlayer();
            if(!command.testPermissionSilent(player)){
                player.sendMessage("Nie posiadasz uprawnien - " + command.getPermission());
                event.setCancelled(true);
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

}
