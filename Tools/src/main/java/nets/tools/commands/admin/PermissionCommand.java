package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.helpers.MathHelper;
import net.saidora.api.helpers.TimeHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.manager.GroupManager;
import nets.tools.manager.UserManager;
import nets.tools.model.Group;
import org.bukkit.OfflinePlayer;

public class PermissionCommand {

    public void register(){
        new CommandTree("permission")
                .withPermission("ethercraft.command.admin.permission")
                .executes((commandSender, commandArguments) -> {
                    commandSender.sendMessage("Jak nie wiesz, to nie wiesz :D");
                })
                .then(new LiteralArgument("user")
                        .then(new OfflinePlayerArgument("target")
                                .then(new LiteralArgument("parent")
                                        .then(createGroupArgument("group")
                                                .then(new LiteralArgument("set")
                                                        .executes((commandSender, commandArguments) -> {
                                                            OfflinePlayer offlinePlayer = (OfflinePlayer) commandArguments.get("target");
                                                            Group group = (Group) commandArguments.get("group");
                                                            UserManager.getInstance().find(offlinePlayer.getName()).ifPresentOrElse(user -> {
                                                                user.getAllGroups().clear();
                                                                user.getAllGroups().put(group, 0L);
                                                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Ranga użytkownika <name> została ustawiona na <group>", Placeholder.parsed("name", offlinePlayer.getName()), Placeholder.parsed("group", group.name())).send(commandSender);
                                                            }, () -> {
                                                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Podany użytkownik nie istnieje.").send(commandSender);
                                                            });
                                                        })
                                                ).then(new LiteralArgument("add")
                                                        .executes((commandSender, commandArguments) -> {
                                                            OfflinePlayer offlinePlayer = (OfflinePlayer) commandArguments.get("target");
                                                            Group group = (Group) commandArguments.get("group");
                                                            UserManager.getInstance().find(offlinePlayer.getName()).ifPresentOrElse(user -> {
                                                                user.getAllGroups().remove(group);
                                                                user.getAllGroups().put(group, 0L);
                                                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Ranga <group> została dodana użytkownikowi <name>", Placeholder.parsed("name", offlinePlayer.getName()), Placeholder.parsed("group", group.name())).send(commandSender);
                                                            }, () -> {
                                                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Podany użytkownik nie istnieje.").send(commandSender);
                                                            });
                                                        })
                                                ).then(new LiteralArgument("addtemp")
                                                        .combineWith(createTimeArgument("time"))
                                                        .executes((commandSender, commandArguments) -> {
                                                            OfflinePlayer offlinePlayer = (OfflinePlayer) commandArguments.get("target");
                                                            Group group = (Group) commandArguments.get("group");

                                                            UserManager.getInstance().find(offlinePlayer.getName()).ifPresentOrElse(user -> {
                                                                user.getAllGroups().remove(group);
                                                                user.getAllGroups().put(group, 0L);
                                                                TimeHelper timeHelper = (TimeHelper) commandArguments.get("time");
                                                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Dodano użytkownikowi <name> range <group> na <time>.", Placeholder.parsed("name", offlinePlayer.getName()), Placeholder.parsed("group", group.name()), Placeholder.parsed("time", timeHelper.remainingTime())).send(commandSender);
                                                            }, () -> {
                                                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, "Podany użytkownik nie istnieje.").send(commandSender);
                                                            });
                                                        })
                                                )
                                        )
                                )
                        )
                ).then(new LiteralArgument("group")
                        .then(new LiteralArgument("create")
                                .combineWith(new StringArgument("name"))
                                .executes((commandSender, commandArguments) -> {

                                })
                                .then(new IntegerArgument("priority")
                                        .executes((commandSender, commandArguments) -> {

                                        })
                                )
                        )
                        .then(createGroupArgument("group")
                        )
                )
                .override();
    }

    private Argument<Group> createGroupArgument(String nodeName){
        return new CustomArgument<>(new StringArgument(nodeName), customArgumentInfo -> GroupManager.getInstance().getGroupMap().getOrDefault(customArgumentInfo.input(), Group.DEFAULT)).includeSuggestions((suggestionInfo, suggestionsBuilder) -> {
            suggestionsBuilder.suggest("default");
            return suggestionsBuilder.buildFuture();
        });
    }

    private Argument<TimeHelper> createTimeArgument(String nodeName){
        return new CustomArgument<>(new StringArgument(nodeName), customArgumentInfo -> TimeHelper.parse(customArgumentInfo.input(), true)).includeSuggestions((suggestionInfo, suggestionsBuilder) -> {
            String arg = suggestionInfo.currentArg();
            if(arg.isEmpty()) return suggestionsBuilder.buildFuture();


            if(MathHelper.isInteger(String.valueOf(arg.charAt(arg.length()-1)))){
                suggestionsBuilder.suggest(suggestionInfo.currentArg() + "y", () -> "1y = jeden rok");
                suggestionsBuilder.suggest(suggestionInfo.currentArg() + "mo",() -> "1mo = jeden miesiąc");
                suggestionsBuilder.suggest(suggestionInfo.currentArg() + "w", () -> "1w = jeden tydzień");
                suggestionsBuilder.suggest(suggestionInfo.currentArg() + "d", () -> "1d = jeden dzień");
                suggestionsBuilder.suggest(suggestionInfo.currentArg() + "h", () -> "1h = jedna godzina");
                suggestionsBuilder.suggest(suggestionInfo.currentArg() + "m", () -> "1m = jedna minuta");
                suggestionsBuilder.suggest(suggestionInfo.currentArg() + "s", () -> "1s = jedna sekunda");
            } else {
                suggestionsBuilder.suggest(arg + "1y", () -> "1y = jeden rok");
                suggestionsBuilder.suggest(arg + "1mo", () -> "1mo = jeden miesiąc");
                suggestionsBuilder.suggest(arg + "1w", () -> "1w = jeden tydzień");
                suggestionsBuilder.suggest(arg + "1d", () -> "1d = jeden dzień");
                suggestionsBuilder.suggest(arg + "1h", () -> "1h = jedna godzina");
                suggestionsBuilder.suggest(arg + "1m", () -> "1m = jedna minuta");
                suggestionsBuilder.suggest(arg + "1s", () -> "1s = jedna sekunda");
            }

            return suggestionsBuilder.buildFuture();
        });
    }

}
