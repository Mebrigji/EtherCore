package net.saidora.api.helpers;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class StringHelper {

    public static <K, V> String mapToString(Map<K, V> map, BiFunction<K, V, String> function){
        StringBuilder builder = new StringBuilder();
        map.forEach((k, v) -> builder.append(function.apply(k, v)));
        return builder.toString();
    }

    public static <E> String listToString(Collection<E> collection, Function<E, String> function){
        StringBuilder builder = new StringBuilder();
        collection.forEach(e -> builder.append(function.apply(e)));
        return builder.toString();
    }

    public static String substring(String context, int length){
        return context.substring(length);
    }
}
