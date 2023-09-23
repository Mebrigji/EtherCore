package nets.tools.tablist;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.world.level.EnumGamemode;

public class TabListEntry {

    private String context;
    private EnumGamemode enumGameMode;
    private int ping;

    private GameProfile gameProfile;
    private String texture, signature;

    public TabListEntry(String context, EnumGamemode enumGameMode, int ping, GameProfile gameProfile, String texture, String signature) {
        this.context = context;
        this.enumGameMode = enumGameMode;
        this.ping = ping;
        this.gameProfile = gameProfile;
        this.texture = texture;
        this.signature = signature;

        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public EnumGamemode getEnumGameMode() {
        return enumGameMode;
    }

    public void setEnumGameMode(EnumGamemode enumGameMode) {
        this.enumGameMode = enumGameMode;
    }
}
