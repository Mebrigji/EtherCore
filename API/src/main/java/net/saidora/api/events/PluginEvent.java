package net.saidora.api.events;

import java.util.Set;
import java.util.function.Consumer;

public interface PluginEvent<T> {

    T getEvent();

    Set<Consumer<T>> getEventList();

    default void call(){
        getEventList().forEach(consumer -> consumer.accept(getEvent()));
    }

}
