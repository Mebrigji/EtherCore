package net.saidora.api.helpers;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ListHelper {

    public static <T> void forEachWithCondition(List<T> list, Function<T, Boolean> function, Consumer<T> consumer){
        list.stream().filter(function::apply).forEach(consumer);
    }

    public static List<String> suggest(Collection<String> arguments, String context){
        return arguments.stream().filter(s -> s.regionMatches(true, 0, context, 0, context.length())).toList();
    }

    public static <K, V> List<String> mapToList(Map<K, V> map, BiFunction<K, V, String> function){
        List<String> list = new ArrayList<>();
        map.forEach((k, v) -> list.add(function.apply(k, v)));
        return list;
    }

    public static <T> Set<T> invokeWithLimit(Set<T> set, int maxCount, Consumer<T> consumer){
        int count = 0;
        Set<T> toRemove = new HashSet<>();
        for (T t : set) {
            if(count++ > maxCount) break;
            consumer.accept(t);
            toRemove.add(t);
        }
        return toRemove;
    }

}
