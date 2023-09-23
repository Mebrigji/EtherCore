package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.notifications.NotificationBuilder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.function.Predicate;

public class ServerInfoCommand {

    public void register(){
        new CommandTree("server-info")
                .withPermission("ethercraft.command.admin.*")
                .executes((commandSender, commandArguments) -> {
                    commandSender.sendMessage(" ");
                    printServer(commandSender);
                    printRam(commandSender);
                    printThreads(commandSender);
                    Bukkit.getWorlds().forEach(world -> printMap(world, commandSender));
                })
                .then(new LiteralArgument("ram")
                        .executes((commandSender, commandArguments) -> {
                            printRam(commandSender);
                        })
                )
                .then(new LiteralArgument("map")
                        .combineWith(new WorldArgument("world"))
                        .executes((commandSender, commandArguments) -> {
                            printMap((World) commandArguments.get("world"), commandSender);
                        })
                )
                .then(new LiteralArgument("server")
                        .executes((commandSender, commandArguments) -> {
                            printServer(commandSender);
                        })
                )
                .then(new LiteralArgument("threads")
                        .executes((commandSender, commandArguments) -> {
                            printThreads(commandSender);
                        })
                )
                .register();
    }

    private void printServer(CommandSender commandSender){
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int players = Bukkit.getOnlinePlayers().size();
        int maxPlayers = Bukkit.getMaxPlayers();
        int totalPlayers = Bukkit.getOfflinePlayers().length;
        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, """
                &7== &3Serwer:
                 &7- &fDostępne rdzenie: &a<available-processors>
                 &7- &fGracze: &a<players>&2/<max-players>
                 &7- &fZarejestrowani gracze: &a<total-players>""",
                Placeholder.parsed("available-processors", String.valueOf(availableProcessors)),
                Placeholder.parsed("players", String.valueOf(players)),
                Placeholder.parsed("max-players", String.valueOf(maxPlayers)),
                Placeholder.parsed("total-players", String.valueOf(totalPlayers)),
                Placeholder.parsed("tps", Arrays.toString(Bukkit.getServer().getTPS())))
                .send(commandSender);
    }

    private void printThreads(CommandSender commandSender){
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long full = 0L;
        StringBuilder stringBuilder = new StringBuilder("&7== &6Wątki:");
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            long cpuTime = threadMXBean.getThreadCpuTime(thread.getId());
            full += cpuTime;
            if(cpuTime > 0L){
                long l = (cpuTime * 100L) / full;
                if(l > 0.0D){
                    stringBuilder.append("\n &7- &fThread=").append(thread.getName()).append(", &7").append(l).append("%");
                }
            }
        }
        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, stringBuilder.toString()).send(commandSender);
    }

    private void printRam(CommandSender commandSender){
        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, """
                &7== &cRam:
                 &7- &fUżywany: &b<used>mb.
                 &7- &fZatwierdzony: &a<total>mb.
                 &7- &fMaksymalny: &c<max>mb.
                 &7- &fWolny: &6<free>mb.""",
                Placeholder.parsed("used", String.valueOf(toMegaBytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()))),
                Placeholder.parsed("free", String.valueOf(toMegaBytes(Runtime.getRuntime().freeMemory()))),
                Placeholder.parsed("max", String.valueOf(toMegaBytes(Runtime.getRuntime().maxMemory()))),
                Placeholder.parsed("total", String.valueOf(toMegaBytes(Runtime.getRuntime().totalMemory())))
        ).send(commandSender);
    }

    private long toMegaBytes(long value){
        return value / 1024L / 1024L;
    }

    private void printMap(World world, CommandSender commandSender){
        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, """
                &7== &aŚwiat <world>:
                 &7- &fStworzenia: &c<entities>
                 &7- &fPrzedmioty: &7<items>
                 &7- &fStrzały: <arrows>
                 &7- &fŚnieżki: &b<snowballs>
                 &7- &fIlość wyrenderowanych chunków: &6<chunks>""",
                Placeholder.parsed("world", world.getName()),
                Placeholder.parsed("entities", String.valueOf(count(world, entity -> entity instanceof LivingEntity))),
                Placeholder.parsed("items", String.valueOf(count(world, entity -> entity instanceof Item))),
                Placeholder.parsed("arrows", String.valueOf(count(world, entity -> entity instanceof Arrow))),
                Placeholder.parsed("snowballs", String.valueOf(count(world, entity -> entity instanceof Snowball))),
                Placeholder.parsed("chunks", String.valueOf(world.getLoadedChunks().length))
        ).send(commandSender);
    }

    private long count(World world, Predicate<Entity> entityPredicate){
        return world.getEntities().stream().filter(entityPredicate).count();
    }
}
