package nets.tools.objects;

import nets.tools.model.Group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupController implements Group {

    private final String name;
    private String prefix, suffix, display;
    private int priority;

    private final Map<String, Boolean> permissionMap = new LinkedHashMap<>();
    private final List<Group> parents = new ArrayList<>();

    public GroupController(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String display() {
        if(display == null || display.isEmpty()) return name;
        return display;
    }

    @Override
    public void display(String display) {
        this.display = display;
    }

    @Override
    public String prefix() {
        return prefix;
    }

    @Override
    public void prefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String suffix() {
        return suffix;
    }

    @Override
    public void suffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public void priority(int priority) {
        this.priority = priority;
    }

    @Override
    public Map<String, Boolean> permissions() {
        return permissionMap;
    }

    @Override
    public void addPermission(String permission, boolean negative) {
        this.permissionMap.remove(permission);
        this.permissionMap.put(permission, negative);
    }

    @Override
    public List<Group> parents() {
        return parents;
    }

    @Override
    public void addParent(Group parent) {
        this.parents.remove(parent);
        this.parents.add(parent);
    }

    @Override
    public void removeParent(Group parent) {
        this.parents.remove(parent);
    }
}
