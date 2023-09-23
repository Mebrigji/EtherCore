package nets.tools.objects;

import io.papermc.paper.adventure.AdventureComponent;
import net.kyori.adventure.text.Component;

import net.minecraft.EnumChatFormat;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardTeam;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.util.Tuple;
import nets.tools.model.IndividualNameTag;
import nets.tools.model.User;
import org.bukkit.Bukkit;
import org.bukkit.block.Lectern;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class IndividualNameTagController implements IndividualNameTag {

    private static final Scoreboard scoreboard = new Scoreboard();
    private final Player owner;

    private boolean created;
    private ScoreboardTeam scoreboardTeam;

    private Function<Player, Component> suffix;
    private Function<Player, Tuple<Component, EnumChatFormat>> prefix;

    public IndividualNameTagController(Player player) {
        this.owner = player;
    }


    @Override
    public User owner() {
        return owner();
    }

    @Override
    public void initializer() {
        if(!created){
            created = true;
            scoreboardTeam = scoreboard.f(owner.getName());
            if(scoreboardTeam == null) scoreboardTeam = scoreboard.g(owner.getName());
            scoreboard.a(owner.getName(), scoreboardTeam);

            PlayerExtension.getPlayerExtend(owner, extension -> {
                extension.sendPacket(PacketPlayOutScoreboardTeam.a(scoreboardTeam, false));
                Bukkit.getOnlinePlayers().forEach(player -> {
                    ScoreboardTeam team = scoreboard.f(player.getName());
                    if(team == null) team = scoreboard.g(player.getName());
                    extension.sendPacket(PacketPlayOutScoreboardTeam.a(team, false));
                });
            });
        } else {
            update();
        }
    }

    @Override
    public void update() {
        ScoreboardTeam scoreboardTeam = scoreboard.f(owner.getName());
        if (scoreboardTeam == null || !created) {
            initializer();
            return;
        }

        if (!scoreboardTeam.g().contains(owner.getName())) {
            scoreboard.a(owner.getName(), scoreboardTeam);
        }

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (prefix != null) {
                Tuple<Component, EnumChatFormat> tuple = prefix.apply(p);
                prefix(tuple.getLeft());
                color(tuple.getRight());
            }
            if (suffix != null) suffix(suffix.apply(p));
            PlayerExtension.getPlayerExtend(p, extension -> extension.sendPacket(PacketPlayOutScoreboardTeam.a(scoreboardTeam, true)));
        });

    }

    @Override
    public void remove() {
        ScoreboardTeam scoreboardTeam = scoreboard.f(owner.getName());
        if (scoreboardTeam != null) {
            scoreboard.d(scoreboardTeam);
            Bukkit.getOnlinePlayers().forEach(player -> PlayerExtension.getPlayerExtend(player, extension -> extension.sendPacket(PacketPlayOutScoreboardTeam.a(scoreboardTeam))));
        }
    }

    @Override
    public void updatePrefix(Function<Player, Tuple<Component, EnumChatFormat>> function) {
        this.prefix = function;
    }

    @Override
    public void updateSuffix(Function<Player, Component> function) {
        this.suffix = function;
    }

    @Override
    public void updateScore(Function<Player, Tuple<Integer, Component>> function) {

    }


    private void prefix(Component component){
        scoreboardTeam.b(new AdventureComponent(component));
    }

    private void suffix(Component component){
        scoreboardTeam.c(new AdventureComponent(component));
    }

    private void color(EnumChatFormat color){
        scoreboardTeam.a(color);
    }

}
