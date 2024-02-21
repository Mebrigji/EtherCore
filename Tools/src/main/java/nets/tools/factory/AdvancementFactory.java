package nets.tools.factory;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.FakeAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import lombok.Getter;
import nets.tools.manager.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancementFactory implements Listener {

    private AdvancementTab levelFirst, levelSecond, levelThird, admin;
    @Getter
    private final UltimateAdvancementAPI ultimateAdvancementAPI;

    public AdvancementFactory(JavaPlugin javaPlugin){
        this.ultimateAdvancementAPI = UltimateAdvancementAPI.getInstance(javaPlugin);
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
        this.levelFirst = ultimateAdvancementAPI.createAdvancementTab("ziemia");
        this.levelSecond = ultimateAdvancementAPI.createAdvancementTab("kosmos");
        this.levelThird = ultimateAdvancementAPI.createAdvancementTab("pieklo");
        this.admin = ultimateAdvancementAPI.createAdvancementTab("admin");
    }

    public void register(){

        RootAdvancement rootAdvancement = new RootAdvancement(levelFirst, "root", new AdvancementDisplay(Material.GRASS_BLOCK, "Wprowadzenie", AdvancementFrameType.TASK, true, false, 0, 1, "Zapoznaj się z poradnikiem oraz regulaminem serwera."), "textures/block/dirt.png");

        levelFirst.registerAdvancements(rootAdvancement,
                new BaseAdvancement("firstitem", new AdvancementDisplay(Material.CRAFTING_TABLE, "Stwórz swój pierwszy przedmiot", AdvancementFrameType.TASK, true, false, 1, 0, "Ukończysz po stworzeniu przedmiotu."), rootAdvancement),
                new BaseAdvancement("kitclaim", new AdvancementDisplay(Material.HOPPER, "Odbierz zestaw pod /kit", AdvancementFrameType.TASK, true, false, 2, 0, "Ukończysz po otrzymaniu zestawu."), rootAdvancement),
                new BaseAdvancement("chatunlock", new AdvancementDisplay(Material.PAPER, "Odblokuj czat", AdvancementFrameType.TASK, true, false, 3, 0, "Ukończysz po wykopaniu " + ChatManager.getInstance().getLevel() + " bloków."), rootAdvancement),
                new BaseAdvancement("createarmor", new AdvancementDisplay(Material.IRON_HELMET, "Stwórz żelazną zbroje", AdvancementFrameType.TASK, true, false, 4, 0, "Ukończysz po stworzeniu:\n - żelaznego hełmu\n - żelaznego napierśnika\n - żelaznych spodni\n - żelaznych butów"), rootAdvancement),
                new BaseAdvancement("rich", new AdvancementDisplay(Material.DIAMOND, "Bogactwo", AdvancementFrameType.GOAL, true, false, 1, 2, "Wykop pierwsze diamenty"), rootAdvancement),
                new BaseAdvancement("brotherhood", new AdvancementDisplay(Material.PAPER, "Stwórz lub dołącz do bractwa", AdvancementFrameType.GOAL, true, false, 2, 2, "Ukończysz po stworzeniu lub dołączeniu do bractwa."), rootAdvancement),
                new BaseAdvancement("discord", new AdvancementDisplay(Material.PAPER, "Połącz konto z discordem", AdvancementFrameType.CHALLENGE, true, false, 1, 3, "Połącz konto z naszym discordem\n -> https://dc.ethercraft.pl/"), rootAdvancement),
                new BaseAdvancement("marathon", new AdvancementDisplay(Material.DIAMOND_BOOTS, "Stań się maratończykiem", AdvancementFrameType.CHALLENGE, true, false, 2, 3, "Przebiegnij 100 kilometrów."), rootAdvancement),
                new BaseAdvancement("masochist", new AdvancementDisplay(Material.IRON_SHOVEL, "Masochista", AdvancementFrameType.CHALLENGE, true, false, 3, 3, "Bądź podczas antylogouta przez 10 minut bez przerwy."), rootAdvancement),
                new BaseAdvancement("curb", new AdvancementDisplay(Material.FEATHER, "Krawężnik", AdvancementFrameType.CHALLENGE, true, false, 4, 3, "Zgiń skacząc z wysokości 4 bloków."), rootAdvancement)
        );

        rootAdvancement = new RootAdvancement(levelSecond, "root", new AdvancementDisplay(Material.BEDROCK, "Ukończ poziom 1", AdvancementFrameType.GOAL, true, false, 0, 0, "Najpierw ukończ poziom 1."), "textures/block/end_stone.png");
        levelSecond.registerAdvancements(rootAdvancement);

        rootAdvancement = new RootAdvancement(levelThird, "root", new AdvancementDisplay(Material.BEDROCK, "Ukończ poziom 2", AdvancementFrameType.GOAL, true, false, 0, 0, "Najpierw ukończ poziom 2."), "textures/block/netherrack.png");
        levelThird.registerAdvancements(rootAdvancement);

        rootAdvancement = new RootAdvancement(admin, "root", new AdvancementDisplay(Material.DEBUG_STICK, "Dołącz do administracji", AdvancementFrameType.GOAL, true, true, 0, 0, "Gratulujemy dołączenia do administracji!"), "textures/block/anvil.png");
        admin.registerAdvancements(rootAdvancement,
                new BaseAdvancement("10praises", new AdvancementDisplay(Material.POPPY, "Zbierz 10 pochwał", AdvancementFrameType.GOAL, true, false, 0, 1, "Otrzymaj 10 pochwał od graczy"), rootAdvancement),
                new BaseAdvancement("50praises", new AdvancementDisplay(Material.POPPY, "Zbierz 50 pochwał", AdvancementFrameType.GOAL, true, false, 1, 1, "Otrzymaj 50 pochwał od graczy"), rootAdvancement),
                new BaseAdvancement("100praises", new AdvancementDisplay(Material.POPPY, "Zbierz 100 pochwał", AdvancementFrameType.GOAL, true, false, 2, 1, "Otrzymaj 100 pochwał od graczy"), rootAdvancement)

        );
    }

    @EventHandler
    public void handlePlayerLoadingCompletedEvent(PlayerLoadingCompletedEvent event){
        Player player = event.getPlayer();
        levelFirst.showTab(player);
        levelSecond.showTab(player);
        levelThird.showTab(player);
        if(player.hasPermission("ethercraft.admin")) admin.showTab(player);
    }

}
