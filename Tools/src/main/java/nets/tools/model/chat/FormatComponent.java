package nets.tools.model.chat;

import org.bukkit.entity.Player;

import java.util.function.Function;

public interface FormatComponent {

    String id();

    String display();

    Function<String, Player> dynamicDisplay();

    String description();

}
