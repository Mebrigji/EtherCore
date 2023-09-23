package net.saidora.api.helpers;

import org.bukkit.entity.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ExperienceHelper {

    private static ExperienceHelper instance;

    public static ExperienceHelper getInstance() {
        if(instance == null) instance = new ExperienceHelper();
        return instance;
    }

    public void start(Consumer<ExperienceHelper> consumer){
        consumer.accept(this);
    }

    public void setTotalExperience(Player player, int exp){
        if(exp < 0) return;
        player.setExp(0);
        player.setTotalExperience(0);
        player.setLevel(0);

        int amount = exp;
        while (amount > 0){
            int expAtLevel = getExpAtLevel(player.getLevel());
            amount -= expAtLevel;
            if(amount >= 0) player.giveExp(expAtLevel);
            else {
                amount += expAtLevel;
                player.giveExp(amount);
                amount = 0;
            }
        }
    }

    public int getTotalExperience(Player player){
        int exp = (int) (getExpAtLevel(player.getLevel()) * player.getExp());
        int level = player.getLevel();
        while (level > 0){
            level--;
            exp += getExpAtLevel(level);
        }
        if(exp < 0) exp = Integer.MAX_VALUE;
        return exp;
    }

    public int getExpToNextLevel(Player player){
        return getExpAtLevel(player.getLevel()) - (int) (getExpAtLevel(player.getLevel()) * player.getExp());
    }

    public int getExpToNextLevel(int level, int exp){
        return getExpAtLevel(level) - (getExpAtLevel(level) * exp);
    }

    public int getExpToLevel(int level){
        int currentLevel = 0;
        int exp = 0;
        while (currentLevel < level){
            exp += getExpAtLevel(level);
            currentLevel++;
        }
        if(exp < 0) exp = Integer.MAX_VALUE;
        return exp;
    }

    public int expToLevel(int exp){
        int level = 0;
        while(getExpAtLevel(level) < exp){
            exp -= getExpAtLevel(level++);
        }
        return level;
    }

    public int getExpAtLevel(int level){
        if(level <= 15) return (2 * level) + 7;
        else if(level <= 30) return (5 * level) - 38;
        else return (9 * level) - 158;
    }
}
