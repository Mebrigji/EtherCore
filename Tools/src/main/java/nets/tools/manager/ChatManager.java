package nets.tools.manager;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private static ChatManager instance;

    public static ChatManager getInstance() {
        if(instance == null) instance = new ChatManager();
        return instance;
    }

    private boolean enabled;
    private double level;
    private String permission;

    private List<Flag> flags = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public List<Flag> getFlags() {
        return flags;
    }

    public boolean isFlagEnabled(Flag flag){
        return flags.contains(flag);
    }

    public void setFlags(List<Flag> flags) {
        this.flags = flags;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public enum Flag {

        KILLS,
        ITEMS,
        ENDER_CHEST,
        INVENTORY

    }
}
