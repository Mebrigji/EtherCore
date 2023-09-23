package nets.tools.tablist;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import io.papermc.paper.adventure.AdventureComponent;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.world.level.EnumGamemode;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.helpers.ReflectionHelper;
import nets.tools.configuration.builder.TabLineBuilder;
import nets.tools.events.TabListLineCreateEvent;
import nets.tools.events.TabListLineUpdateEvent;
import nets.tools.factory.TabFactory;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

public class TabList {

    protected static final Field dataInfoField = ReflectionHelper.getField(ClientboundPlayerInfoUpdatePacket.class, "b");
    protected static final String UUID_FORMAT = "00000000-0000-%s-0000-000000000000";


    public static int DEFAULT_SIZE = 80;

    private final UUID uuid;
    private final TabFactory tabFactory;
    private PlayerExtension extension;

    private TabListEntry[] entries = new TabListEntry[80];
    private int size = DEFAULT_SIZE;

    private boolean firstPacket = true;

    private Function<PlayerExtension, String> header = ex -> new Date().toString(), footer = ex -> new Date().toString();

    private Map<String, Function<PlayerExtension, String>> placeholdersMap = new HashMap<>();

    public TabList(UUID uuid, TabFactory tabFactory) {
        this.uuid = uuid;
        this.tabFactory = tabFactory;
    }

    public TabList(PlayerExtension extension, TabFactory tabFactory){
        this.uuid = extension.getPlayer().getUniqueId();
        this.extension = extension;
        this.tabFactory = tabFactory;
    }

    public PlayerExtension getExtension() {
        return extension;
    }

    public void setExtension(PlayerExtension extension) {
        this.extension = extension;
    }

    public void setPlaceholdersMap(Map<String, Function<PlayerExtension, String>> placeholdersMap) {
        this.placeholdersMap = placeholdersMap;
    }

    public static int getDefaultSize() {
        return DEFAULT_SIZE;
    }

    public static void setDefaultSize(int defaultSize) {
        DEFAULT_SIZE = defaultSize;
    }

    public UUID getUuid() {
        return uuid;
    }

    public TabListEntry[] getEntries() {
        return entries;
    }

    public void setEntries(TabListEntry[] entries) {
        this.entries = entries;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFirstPacket() {
        return firstPacket;
    }

    public void setFirstPacket(boolean firstPacket) {
        this.firstPacket = firstPacket;
    }

    public Map<String, Function<PlayerExtension, String>> getPlaceholdersMap() {
        return placeholdersMap;
    }

    public Function<PlayerExtension, String> getHeader() {
        return header;
    }

    public void setHeader(Function<PlayerExtension, String> header) {
        this.header = header;
    }

    public Function<PlayerExtension, String> getFooter() {
        return footer;
    }

    public void setFooter(Function<PlayerExtension, String> footer) {
        this.footer = footer;
    }

    private ClientboundPlayerInfoUpdatePacket createPlayerInfoPacket(EnumSet<ClientboundPlayerInfoUpdatePacket.a> actions, List<ClientboundPlayerInfoUpdatePacket.b> entries) {
        ClientboundPlayerInfoUpdatePacket playerInfoPacket =
                new ClientboundPlayerInfoUpdatePacket(actions, Collections.emptyList());

        ReflectionHelper.setFieldValue(dataInfoField, playerInfoPacket, entries);

        return playerInfoPacket;
    }

    @SneakyThrows
    public void create() {
        try {
            PacketPlayOutPlayerListHeaderFooter listHeaderFooter = new PacketPlayOutPlayerListHeaderFooter(prepare(header.apply(extension)), prepare(footer.apply(extension)));
            extension.sendPacket(listHeaderFooter);

            List<ClientboundPlayerInfoUpdatePacket.b> addPlayerList = new ArrayList<>(), updatePlayerList = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                TabListEntry entry = entries[i];

                if (entry == null) {
                    String paddedIdentifier = StringUtils.leftPad(String.valueOf(i), 2, '0');
                    GameProfile gameProfile = new GameProfile(UUID.fromString(String.format(UUID_FORMAT, paddedIdentifier)), "EtherTab-" + paddedIdentifier);

                    TabLineBuilder builder = tabFactory.getCells().get(i);

                    if (builder == null) builder = new TabLineBuilder(i, " ", "", "", EnumGamemode.a, 0);

                    String context = builder.context() == null || builder.context().isEmpty() ? " " : builder.context();

                    TabListLineCreateEvent tabListLineCreateEvent = new TabListLineCreateEvent(extension, this, i);

                    tabListLineCreateEvent.setContext(context);
                    tabListLineCreateEvent.setEnumGamemode(builder.gameMode());
                    tabListLineCreateEvent.setTexture(builder.texture());
                    tabListLineCreateEvent.setSignature(builder.signature());
                    tabListLineCreateEvent.setPing(builder.ping());

                    tabListLineCreateEvent.call();

                    ProfileProperty property = extension.getPlayer().getPlayerProfile().getProperties().stream().filter(profileProperty -> profileProperty.getName().equals("textures")).findFirst().orElse(null);

                    if (builder.texture() != null && !builder.texture().isEmpty() && builder.signature() != null && !builder.signature().isEmpty()) {
                        tabListLineCreateEvent.setTexture(builder.texture());
                        tabListLineCreateEvent.setSignature(builder.signature());
                    } else if (property != null && property.getValue() != null && property.getSignature() != null) {
                        tabListLineCreateEvent.setTexture(property.getValue());
                        tabListLineCreateEvent.setSignature(property.getSignature());
                    } else {
                        tabListLineCreateEvent.setTexture("ewogICJ0aW1lc3RhbXAiIDogMTYxMTA3MDE4MzU0NiwKICAicHJvZmlsZUlkIiA6ICJlMjI0ZTVlY2UyOTk0MDA1YWUyMjNiMGY3N2E1NzcxNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNSEZfT2FrTG9nIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ViZTFkYTAxZjE3NGQ0NjIzOTE2NmRjNWZiNGEwYWY4OGI5ZWUzOGI3ZGE5MThmMzNkODM4YmNhODViYTdmM2MiCiAgICB9CiAgfQp9");
                        tabListLineCreateEvent.setSignature("FiCcdqnEPPIwnaYgsNfLumKY4clEzCc4THRdDiiZN/0HRPIu0FGMa+QhroqIk/+mCKO2xW3ONnGzf69J3Pg4nl6aAjf2MdJEqXK+jD2FnKdz/qbthD70eppLMjxHnWCpbvqPnV2g9JsufZfSo6hVeW4MSTiG/9sksMPwX06SxybwEw6GXjOa1G78UjzclyVrClto/DXR2mx/i0saksfLOqCUvIJtIppho45nlUvzos5CSl4rqnUwIxrg12b1YEq3iljySEBDiNyNKSWCudv2J6ry+IRv12yszuGXxo48bsdeM4ZrdarScz5TOpztqeU+9Kap/iSFsYvysiKjgYDLZTKxyw85Ahvypn7H4ViYhRbas2C3/Yq12jzvAGMq47XQKlG7tYZ8q/2Ha03RIX9MU3y8aLYCk/OFmgfUNgCa3s72yiC6HkDNezQYEooLtvO3Vu7eEAmWF8am+AY2Id2aL80K1EDKa2IoNsjA/AvDbViwHeNLDMvrzlbD6E8ueM+BzeG+vjYXU+Vx66JuClNCEQqs1iKVecjdnr6GKbMorK5aseXrHDspKkdt32iqPyIczFjBmXdVMDj8LKfMvb1lI3YChU7ULerdwJHVn5OBbTxlJA0D47dQBgQ+ZamG44r6ltc9Ck5WCjpfKwf0LlH4/nDEERG6jFzo7lsxLjb0fPk=");
                    }

                    entry = new TabListEntry(tabListLineCreateEvent.getContext(), tabListLineCreateEvent.getEnumGamemode(), tabListLineCreateEvent.getPing(), gameProfile, tabListLineCreateEvent.getTexture(), tabListLineCreateEvent.getSignature());
                    entries[i] = entry;
                } else {

                    TabListLineUpdateEvent updateEvent = new TabListLineUpdateEvent(extension, this, i);
                    updateEvent.setTabListEntry(entry);
                    updateEvent.call();

                }

                ClientboundPlayerInfoUpdatePacket.b playerInfoData = new ClientboundPlayerInfoUpdatePacket.b(entry.getGameProfile().getId(), entry.getGameProfile(), true, entry.getPing(), entry.getEnumGameMode(), prepare(entry.getContext().isEmpty() ? " " : entry.getContext()), null);

                if (firstPacket) addPlayerList.add(playerInfoData);

                updatePlayerList.add(playerInfoData);
            }

            firstPacket = false;

            ClientboundPlayerInfoUpdatePacket addPlayerPacket = createPlayerInfoPacket(EnumSet.of(ClientboundPlayerInfoUpdatePacket.a.a, ClientboundPlayerInfoUpdatePacket.a.c, ClientboundPlayerInfoUpdatePacket.a.d, ClientboundPlayerInfoUpdatePacket.a.e), addPlayerList);
            extension.sendPacket(addPlayerPacket);

            ClientboundPlayerInfoUpdatePacket updatePlayerPacket = createPlayerInfoPacket(EnumSet.of(ClientboundPlayerInfoUpdatePacket.a.e, ClientboundPlayerInfoUpdatePacket.a.f), updatePlayerList);
            extension.sendPacket(updatePlayerPacket);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private AdventureComponent prepare(String context){
        return new AdventureComponent(ComponentHelper.asComponent(apply(context)));
    }

    private String apply(String context){

        var ref = new Object(){
            String con = context;
        };

        placeholdersMap.forEach((s, playerExtensionStringFunction) -> ref.con = ref.con.replace("<" + s + ">", playerExtensionStringFunction.apply(extension)));

        return ref.con.isEmpty() ? " " : ref.con;
    }

    public void remove(){
        if(firstPacket) return;

        firstPacket = true;

        PacketPlayOutPlayerListHeaderFooter packetPlayOutPlayerListHeaderFooter = new PacketPlayOutPlayerListHeaderFooter(new AdventureComponent(Component.empty()), new AdventureComponent(Component.empty()));
        extension.sendPacket(packetPlayOutPlayerListHeaderFooter);

        extension.sendPacket(new ClientboundPlayerInfoRemovePacket(Arrays.stream(entries).map(tabListEntry -> tabListEntry.getGameProfile().getId()).toList()));
    }

}
