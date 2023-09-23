package nets.tools.factory;

import net.saidora.api.events.EventBuilder;
import net.saidora.api.extension.PlayerExtension;
import net.saidora.api.helpers.TimeHelper;
import nets.tools.configuration.builder.TabLineBuilder;
import nets.tools.events.TabListCreateEvent;
import nets.tools.model.Brotherhood;
import nets.tools.model.TimedProgress;
import nets.tools.model.User;
import nets.tools.tablist.TabList;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

public class TabFactory {

    private List<String> header = new ArrayList<>(), footer = new ArrayList<>();

    private Map<Integer, TabLineBuilder> cells = new HashMap<>();

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<String> getFooter() {
        return footer;
    }

    public void setFooter(List<String> footer) {
        this.footer = footer;
    }

    public Map<Integer, TabLineBuilder> getCells() {
        return cells;
    }

    public void setCells(Map<Integer, TabLineBuilder> cells) {
        this.cells = cells;
    }

    private String appendDigit(int i){
        return i <= 9 ? "0" + i : String.valueOf(i);
    }

    public void setup() {

        new EventBuilder<>(TabListCreateEvent.class, tabListCreateEvent -> {
            TabList tabList = tabListCreateEvent.getTabList();
            User user = tabList.getExtension().getPersistentDataObject("user", User.class);

            tabList.setHeader(extension -> String.join("\n&r", header));
            tabList.setFooter(extension -> String.join("\n&r", footer));

            Map<String, Function<PlayerExtension, String>> map = tabList.getPlaceholdersMap();

            map.put("playerName", extension -> extension.getPlayer().getName());
            map.put("ping", extension -> String.valueOf(extension.getPing()));

            DecimalFormat decimalFormat = new DecimalFormat("##.##");

            map.put("session-time", extension -> new TimeHelper(System.currentTimeMillis() - user.getJoin()).parse());

            map.put("stats_killed-entities", extension -> String.valueOf(user.getKilledEntities()));
            map.put("stats_killed-players", extension -> String.valueOf(user.getKilledPlayers()));
            map.put("stats_damage-given", extension -> decimalFormat.format(user.getDamageGiven()));
            map.put("stats_damage-taken", extension -> decimalFormat.format(user.getDamageTaken()));
            map.put("stats_traveled-distance", extension -> decimalFormat.format(user.parseDistance()) + "km");
            map.put("stats_time-spend", extension -> new TimeHelper(user.getTotalTimeSpend() + (System.currentTimeMillis() - user.getJoin())).parse());
            map.put("stats_blocks-mined", extension -> String.valueOf(user.getBlocksMined()));
            map.put("stats_blocks-placed", extension -> String.valueOf(user.getBlocksPlaced()));

            map.put("stats_lastdeath", extension -> new TimeHelper(user.getLastDeath()).timestamp());

            map.put("stats_sessionprogress", extension -> {
                TimedProgress timedProgress = user.sessionProgress();
                double progress = timedProgress.getProgress();
                if(progress >= 100.0){
                    timedProgress.afterMeetingExpectations().run();
                }
                return decimalFormat.format(progress);
            });

            map.put("rank_user-points", extension -> String.valueOf(user.getPoints()));
            map.put("rank_user-kills", extension -> String.valueOf(user.getKills()));
            map.put("rank_user-deaths", extension -> String.valueOf(user.getDeaths()));

            map.put("kd_ratio", extension -> {
                if (user.getDeaths() == 0) return String.valueOf(user.getKills());
                else if (user.getKills() == 0) return "-" + user.getDeaths();
                else return decimalFormat.format((user.getKills() * 1.0 / user.getDeaths()));
            });

            map.put("balance", extension -> decimalFormat.format(user.getBalance()));

            map.put("hour", extension -> appendDigit(LocalDateTime.now(ZoneId.of("GMT+1")).getHour()));
            map.put("minute", extension -> appendDigit(LocalDateTime.now(ZoneId.of("GMT+1")).getMinute()));
            map.put("second", extension -> appendDigit(LocalDateTime.now(ZoneId.of("GMT+1")).getSecond()));
            map.put("day", extension -> appendDigit(LocalDateTime.now(ZoneId.of("GMT+1")).getDayOfMonth()));
            map.put("month", extension -> appendDigit(LocalDateTime.now(ZoneId.of("GMT+1")).getMonthValue()));
            map.put("year", extension -> appendDigit(LocalDateTime.now(ZoneId.of("GMT+1")).getYear()));

            map.put("server_online", extension -> String.valueOf(Bukkit.getOnlinePlayers().size()));
            map.put("server_average_ping", extension -> {
                int size = Bukkit.getOnlinePlayers().size(), totalPing = Bukkit.getOnlinePlayers().stream().map(PlayerExtension::getPlayerExtend).filter(Objects::nonNull).mapToInt(PlayerExtension::getPing).sum();
                return String.valueOf(size != 0 ? totalPing / size : 0);
            });

            map.put("brotherhood_info-name", extension -> user.getBrotherhood().map(Brotherhood::name).orElse("---"));
            map.put("brotherhood_info-owner", extension -> user.getBrotherhood().map(Brotherhood::owner).orElse("---"));
            map.put("brotherhood_info-members-online", extension -> user.getBrotherhood().map(brotherhood -> String.valueOf(brotherhood.getMembers().stream().filter(u -> u.getPlayer().isPresent()).count())).orElse("0"));
            map.put("brotherhood_info-members-total", extension -> user.getBrotherhood().map(brotherhood -> String.valueOf(brotherhood.members().size())).orElse("0"));
            map.put("rank_brotherhood-points", extension -> user.getBrotherhood().map(brotherhood -> String.valueOf(brotherhood.getPoints())).orElse("0"));
            map.put("rank_brotherhood-kills", extension -> user.getBrotherhood().map(brotherhood -> String.valueOf(brotherhood.getKills())).orElse("0"));
            map.put("rank_brotherhood-deaths", extension -> user.getBrotherhood().map(brotherhood -> String.valueOf(brotherhood.getDeaths())).orElse("0"));

        });
    }
}
