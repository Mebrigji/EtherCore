package net.saidora.api.notifications;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.saidora.api.notifications.types.NotificationActionBarProvider;
import net.saidora.api.notifications.types.NotificationChatProvider;
import net.saidora.api.notifications.types.NotificationTitleProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NotificationBuilder {

    public static NotificationBuilder of(NotificationType type){
        return new NotificationBuilder(type);
    }

    public static NotificationBuilder of(NotificationType type, String context){
        return new NotificationBuilder(type).editProvider(notificationProvider -> notificationProvider.context(context)).join();
    }

    public static NotificationBuilder of(NotificationType type, String context, TagResolver... tagResolvers){
        return new NotificationBuilder(type).editProvider(notificationProvider -> {
            notificationProvider.context(context);
            for (TagResolver tagResolver : tagResolvers) {
                notificationProvider.addTagResolver(tagResolver);
            }
        }).join();
    }

    private NotificationProvider provider;

    protected NotificationBuilder(NotificationType notificationType){
        switch (notificationType){
            case TITLE -> provider = new NotificationTitleProvider(this);
            case ACTION_BAR ->  provider = new NotificationActionBarProvider(this);
            default -> provider = new NotificationChatProvider(this);
        }
    }

    public NotificationProvider getProvider() {
        return provider;
    }

    public CompletableFuture<NotificationBuilder> editProvider(Consumer<NotificationProvider> providerConsumer){
        providerConsumer.accept(provider);
        return CompletableFuture.completedFuture(this);
    }

    public CompletableFuture<NotificationBuilder> builder(Consumer<NotificationBuilder> builderConsumer){
        builderConsumer.accept(this);
        return CompletableFuture.completedFuture(this);
    }

    public NotificationBuilder with(String target, Object value){
        return editProvider(p -> p.with(target, value)).join();
    }

    public void send(CommandSender player){
        provider.send(player);
    }

    public void sendMany(CommandSender... commandSenders){
        for (CommandSender commandSender : commandSenders) {
            provider.send(commandSender);
        }
    }

    public enum NotificationType {
        CHAT,
        ACTION_BAR,
        TITLE
    }
}
