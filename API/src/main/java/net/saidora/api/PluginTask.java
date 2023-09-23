package net.saidora.api;

import java.util.concurrent.TimeUnit;

public record PluginTask(long interval, Runnable runnable) {

    public void setup(){
        try {
            API.getInstance().getScheduledExecutorService().scheduleAtFixedRate(runnable, interval(), interval(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
