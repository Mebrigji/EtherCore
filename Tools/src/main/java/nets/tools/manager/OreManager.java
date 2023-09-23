package nets.tools.manager;

import nets.tools.model.Ore;

import java.util.*;

public class OreManager {

    private static OreManager instance;

    public static OreManager getInstance() {
        if (instance == null) {
            instance = new OreManager();
        }
        return instance;
    }

    private final Set<Ore> list = new LinkedHashSet<>();

    public void addOre(Ore ore){
        list.add(ore);
    }

    public Set<Ore> getList() {
        return list;
    }
}
