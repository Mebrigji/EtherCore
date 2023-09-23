package net.saidora.api.notifications;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface NotificationProvider {

    void send(CommandSender player);

    void with(String target, Object value);

    void splitter(String value);

    void context(String context);

    NotificationBuilder getBuilder();

    void addTagResolver(TagResolver tagResolver);

    MiniMessage getMessageBuilder();

}
