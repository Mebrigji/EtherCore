package net.saidora.api.db.api;

public interface ItemSerializer<T> {

    boolean supports(Class<? extends T> type);

    T serialize(String s);

    String deserialize(T t);

}
