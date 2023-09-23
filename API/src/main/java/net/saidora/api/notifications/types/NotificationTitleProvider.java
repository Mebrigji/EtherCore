package net.saidora.api.notifications.types;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.TitlePart;
import net.saidora.api.ColorEnum;
import net.saidora.api.notifications.NotificationBuilder;
import net.saidora.api.notifications.NotificationProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import java.util.HashSet;
import java.util.Set;

public class NotificationTitleProvider implements NotificationProvider {
    private final NotificationBuilder notificationBuilder;

    private String context = "";
    private String splitter = "";
    private final Set<TagResolver> resolvers = new HashSet<>();

    public NotificationTitleProvider(NotificationBuilder notificationBuilder) {
        this.notificationBuilder = notificationBuilder;
    }

    @Override
    public void send(CommandSender sender) {
        String message = ColorEnum.translateAlternateColorCodes(context);
        if(splitter != null && !splitter.isEmpty()){
            boolean title = true;
            for (String s : message.split(splitter)) {
                if(title) {
                    sender.sendTitlePart(TitlePart.TITLE, getMessageBuilder().deserialize(s, resolvers.toArray(new TagResolver[]{})));
                    title = false;
                } else {
                    sender.sendTitlePart(TitlePart.SUBTITLE, getMessageBuilder().deserialize(s, resolvers.toArray(new TagResolver[]{})));
                    break;
                }
            }
        } else sender.sendTitlePart(TitlePart.TITLE, getMessageBuilder().deserialize(message, resolvers.toArray(new TagResolver[]{})));
    }

    @Override
    public void with(String target, Object value) {
        this.resolvers.add(Placeholder.parsed(target, String.valueOf(value)));
    }

    @Override
    public void splitter(String value) {
        this.splitter = value;
    }

    @Override
    public void context(String context) {
        this.context = context;
    }

    @Override
    public NotificationBuilder getBuilder() {
        return notificationBuilder;
    }

    @Override
    public void addTagResolver(TagResolver tagResolver) {
        resolvers.add(tagResolver);
    }

    @Override
    public MiniMessage getMessageBuilder() {
        return MiniMessage.builder().editTags(builder -> builder.resolvers(resolvers.toArray(new TagResolver[]{}))).build();
    }
}
