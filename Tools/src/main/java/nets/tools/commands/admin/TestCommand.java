package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.minecraft.server.commands.CommandGamemode;
import net.saidora.api.helpers.ListHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.permissions.PermissionTreeNode;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestCommand {

    public void register(){
        new CommandTree("test")
                .withPermission("ethercraft.command.admin.test")
                .then(createPermissionArgument("permission")
                        .executes((commandSender, commandArguments) -> {

                        }))
                .then(new LiteralArgument("gradient")
                        .combineWith(new GreedyStringArgument("text"))
                        .executes((commandSender, commandArguments) -> {
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, (String) commandArguments.get(0)).send(commandSender);
                        })
                ).register();
    }

    private Argument<String> createPermissionArgument(String nodeName){
        return new CustomArgument<>(new StringArgument(nodeName), customArgumentInfo -> "").includeSuggestions((suggestionInfo, suggestionsBuilder) -> {
            if(suggestionInfo.currentArg().isEmpty()) Bukkit.getPluginManager().getPermissions().stream().map(Permission::getName).toList().forEach(suggestionsBuilder::suggest);
            else suggestCompletions(suggestionInfo.currentArg(), Bukkit.getPluginManager().getPermissions().stream().map(Permission::getName).toList()).forEach(suggestionsBuilder::suggest);
            return suggestionsBuilder.buildFuture();
        });
    }

    //public static List<String> suggestCompletions(String input, List<String> commandList) {
    //    List<String> suggestions = new ArrayList<>();
//
    //    for (String command : commandList) {
    //        if (command.startsWith(input)) {
    //            int indexOfDot = command.indexOf('.', input.length());
    //            if (indexOfDot != -1) {
    //                String suggestion = command.substring(0, indexOfDot);
    //                if (!suggestions.contains(suggestion)) {
    //                    suggestions.add(suggestion);
    //                }
    //            }
    //        }
    //    }
//
    //    return suggestions;
    //}


    public static List<String> suggestCompletions(String input, List<String> commandList) {
        Set<String> suggestions = new HashSet<>();

        for (String command : commandList) {
            if (command.startsWith(input) && command.length() > input.length()) {
                int indexOfDot = command.indexOf('.', input.length());
                if (indexOfDot != -1) {
                    String suggestion = command.substring(0, indexOfDot);
                    suggestions.add(suggestion);
                }
            } else if (input.startsWith(command) && input.length() > command.length()) {
                int indexOfDot = input.indexOf('.', command.length());
                if (indexOfDot != -1) {
                    String suggestion = input.substring(0, indexOfDot);
                    suggestions.add(suggestion);
                }
            }
        }

        return new ArrayList<>(suggestions);
    }
}
