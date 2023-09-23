package net.saidora.api.helpers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.saidora.api.ColorEnum;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ComponentHelper {

    public static Component asComponent(String context, TagResolver... tagResolvers){
        return MiniMessage.miniMessage().deserialize(ColorEnum.translateAlternateColorCodes(context), tagResolvers);
    }

    public static List<Component> asComponents(String... contexts){
        return Arrays.stream(contexts).map(ComponentHelper::asComponent).toList();
    }

    public static List<Component> asComponents(List<String> list, TagResolver... tagResolvers){
        return list.stream().map(s -> asComponent(s, tagResolvers)).collect(Collectors.toList());
    }

    public static Component asComponent(String context){
        return asComponent(context, StandardTags.rainbow(), StandardTags.gradient(), StandardTags.color());
    }

    public static CompletableFuture<Component> futureComponent(String context){
        CompletableFuture.supplyAsync(() -> context);
        return CompletableFuture.completedFuture(asComponent(context));
    }


    public static CompletableFuture<Component> futureComponent(String context, TagResolver... resolvers){
        return CompletableFuture.completedFuture(asComponent(context, resolvers));
    }

    public static CompletableFuture<List<Component>> futureComponents(String... context){
        return CompletableFuture.completedFuture(Arrays.stream(context).map(ComponentHelper::asComponent).collect(Collectors.toList()));
    }

    public static CompletableFuture<List<Component>> futureComponents(List<String> context, TagResolver... resolvers){
        return CompletableFuture.completedFuture(context.stream().map(s -> asComponent(s, resolvers)).collect(Collectors.toList()));
    }

    public static String asString(Component component){
        return MiniMessage.miniMessage().serialize(component);
    }

}
