package nets.tools.manager;

import nets.tools.model.Group;

import java.util.LinkedHashMap;
import java.util.Map;

public class GroupManager {

    private static GroupManager instance;

    public static GroupManager getInstance() {
        if(instance == null) instance = new GroupManager();
        return instance;
    }

    private final Map<String, Group> groupMap = new LinkedHashMap<>(){{
        put("default", Group.DEFAULT);
    }};

    public Map<String, Group> getGroupMap() {
        return groupMap;
    }


}
