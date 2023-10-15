package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class GameModeCommand {

    public void register(){
        new CommandTree("gamemode")
                .withAliases("gm")
                .withPermission("ethercraft.command.admin.gamemode")
                .then(createGameModeArgument("gameMode")
                        .executesPlayer((player, commandArguments) -> {
                            GameMode gameMode = player.getGameMode();
                            GameMode mode = (GameMode) commandArguments.get("gameMode");
                            if(gameMode.equals(mode)){
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_GAMEMODE_ALREADY).send(player);
                                return;
                            }
                            player.setGameMode(mode);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_GAMEMODE_CHANGED, Placeholder.parsed("old", gameMode.name()), Placeholder.parsed("new", mode.name())).send(player);
                        })
                        .then(new PlayerArgument("player")
                                .executes((commandSender, commandArguments) -> {
                                    GameMode gameMode = (GameMode) commandArguments.get("gameMode");
                                    Player player = (Player) commandArguments.get("player");
                                    GameMode mode = player.getGameMode();
                                    player.setGameMode(gameMode);
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_GAMEMODE_FORCE, Placeholder.parsed("player", player.getName()), Placeholder.parsed("old", mode.name()), Placeholder.parsed("new", gameMode.name())).send(commandSender);
                                })
                        )
                )
                .override();
    }

    private final Map<String, GameMode> mappings = new LinkedHashMap<>(){{
        put("spectator", GameMode.SPECTATOR);
        put("3", GameMode.SPECTATOR);

        put("adventure", GameMode.ADVENTURE);
        put("2", GameMode.ADVENTURE);

        put("creative", GameMode.CREATIVE);
        put("1", GameMode.CREATIVE);

        put("survival", GameMode.SURVIVAL);
        put("0", GameMode.SURVIVAL);
    }};

    private Argument<GameMode> createGameModeArgument(String nodeName){
        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            GameMode gameMode = null;
            for (String s : mappings.keySet()) {
                if(info.input().equalsIgnoreCase(s)){
                    gameMode = mappings.get(s);
                    break;
                }
            }
            if(gameMode == null) throw CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("Wrong game type argument ").appendArgInput());
            return gameMode;
        }).includeSuggestions((suggestionInfo, suggestionsBuilder) -> suggestionsBuilder
                .suggest("creative")
                .suggest("survival")
                .suggest("adventure")
                .suggest("spectator")
                .buildFuture());
    }

}
