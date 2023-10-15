package net.saidora.api.helpers;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathHelper {

    private static final Random random = new Random();

    public static double getDistance(Location location, double minX, double minZ, double maxX, double maxZ){
        double x = location.getX();
        double z = location.getZ();

        double distWest = Math.abs(minX - x);
        double distEast = Math.abs(maxX - x);

        double distNorth = Math.abs(minZ - z);
        double distSouth = Math.abs(maxZ - z);

        double distX = Math.min(distWest, distEast);
        double distZ = Math.min(distNorth, distSouth);

        return Math.min(distX, distZ);
    }

    public static double getRandomDouble(double min, double max){
        return random.nextDouble() * (max - min) + min;
    }

    public static int getRandomInt(int min, int max){
        return random.nextInt(max - min + 1) + min;
    }

    public static Float getRandomFloat(float min, float max){
        return random.nextFloat() * (max - min) + min;
    }

    public static boolean getChance(double chance){
        return chance >= 100 || chance > getRandomDouble(0.0, 100.0D);
    }

    public static Integer getInteger(String context){
        try {
            return Integer.parseInt(context);
        } catch (NumberFormatException e){
            return 0;
        }
    }

    public static boolean isInteger(String context){
        try {
            Integer.parseInt(context);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static Double getDouble(String context){
        try {
            return Double.parseDouble(context);
        } catch (NumberFormatException e){
            return 0.0D;
        }
    }

    public static boolean isDouble(String context){
        try {
            Double.parseDouble(context);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static Long getLong(String context){
        try {
            return Long.parseLong(context);
        } catch (NumberFormatException e){
            return 0L;
        }
    }

    public static boolean isLong(String context){
        try {
            Long.parseLong(context);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static Float getFloat(String context){
        try {
            return Float.parseFloat(context);
        } catch (NumberFormatException e){
            return 0F;
        }
    }

    public static boolean isFloat(String context){
        try {
            Float.parseFloat(context);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static List<Block> getNearbyBlocks(Location location, int radius){
        List<Block> blocks = new ArrayList<>();
        int x = (int) location.getX();
        int y = (int) location.getY();
        int z = (int) location.getZ();

        for (int i = x - (radius / 2); i < x + (radius / 2); i++) {
            for (int j = y - (radius / 2); j < y+ (radius / 2) ; j++) {
                for (int k = z- (radius / 2); k < z + (radius / 2); k++) {
                    blocks.add(location.getWorld().getBlockAt(i, j, k));
                }
            }
        }

        return blocks;
    }

    public static List<Block> getNearbyBlocksWithIgnoringY(Location location, int radius) {

        List<Block> blocks = new ArrayList<>();
        int x = location.getBlockX();
        int z = location.getBlockZ();

        for (int i = x - (radius / 2); i < x + (radius / 2); i++) {
            for (int k = z - (radius / 2); k < z + (radius / 2); k++) {

                Block block = location.getWorld().getBlockAt(i, location.getBlockY(), k);
                blocks.add(block);

            }
        }
        return blocks;
    }

}
