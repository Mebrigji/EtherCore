package nets.tools.model;

import net.kyori.adventure.text.Component;
import net.minecraft.EnumChatFormat;
import net.saidora.api.util.Tuple;
import org.bukkit.entity.Player;

import java.util.function.Function;

public interface IndividualNameTag {

    User owner();

    void initializer();

    void update();

    void remove();

    void updatePrefix(Function<Player, Tuple<Component, EnumChatFormat>> function);

    void updateSuffix(Function<Player, Component> function);

    void updateScore(Function<Player, Tuple<Integer, Component>> function);

}
