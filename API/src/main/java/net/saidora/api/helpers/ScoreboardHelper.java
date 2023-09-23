package net.saidora.api.helpers;

import io.papermc.paper.adventure.AdventureComponent;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import net.saidora.api.extension.PlayerExtension;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardHelper {

    public static IChatBaseComponent buildComponent(String text){
        return new AdventureComponent(ComponentHelper.asComponent(text));
    }

    private PlayerExtension extension;

    private final ScoreboardObjective scoreboardObjective;

    public ScoreboardHelper(PlayerExtension player){
        this.extension = player;
        Scoreboard scoreboard = new Scoreboard();
        this.scoreboardObjective = scoreboard.a("scap", IScoreboardCriteria.a, buildComponent(""),
                IScoreboardCriteria.EnumScoreboardHealthDisplay.a);
    }

    public ScoreboardHelper withDisplayName(String displayName){
        this.displayName = displayName;
        return this;
    }

    public ScoreboardHelper withLines(List<String> lines){
        this.lines = lines;
        return this;
    }


    private String displayName = "";
    private List<String> lines = new ArrayList<>();

    public void send(){
        scoreboardObjective.a(buildComponent(displayName));

        PacketPlayOutScoreboardObjective remove = new PacketPlayOutScoreboardObjective(scoreboardObjective, 1);
        PacketPlayOutScoreboardObjective create = new PacketPlayOutScoreboardObjective(scoreboardObjective, 0);
        PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, scoreboardObjective);

        extension.sendPacket(remove);
        extension.sendPacket(create);
        extension.sendPacket(display);

        int size = lines.size() - 1;
        for (int i = size; i >= 0; i--) {
            extension.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.a, "scap",
                    buildComponent(lines.get(size-i)).getString(), i));
        }

    }
}
