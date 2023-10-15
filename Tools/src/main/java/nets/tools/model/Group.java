package nets.tools.model;

import nets.tools.objects.GroupController;

import java.util.List;
import java.util.Map;

public interface Group {

    Group DEFAULT = new GroupController("default");

    String name();

    String display();
    void display(String display);

    String prefix();
    void prefix(String prefix);

    String suffix();
    void suffix(String suffix);

    int priority();
    void priority(int priority);

    Map<String, Boolean> permissions();
    void addPermission(String permission, boolean negative);

    List<Group> parents();
    void addParent(Group parent);
    void removeParent(Group parent);

}
