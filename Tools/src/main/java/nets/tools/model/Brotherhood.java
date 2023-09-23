package nets.tools.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Brotherhood extends Rank, Region{

    String tag();
    String name();

    String owner();
    void owner(String nickName);
    Optional<User> getOwner();

    Region hearth();


    List<String> members();
    List<User> getMembers();

    void addMember(User user);
    void removeMember(String nickName);

    Map<Permission, List<String>> permissions();

    List<Brotherhood> allies();
    void addAlly(Brotherhood brotherhood);
    void removeAlly(Brotherhood brotherhood);

    enum Permission {

        INVITE_MEMBERS,
        KICK_MEMBERS,
        ENLARGE_SIZE,

        OPEN_WAREHOUSE,
        ADD_ITEM_TO_WAREHOUSE,
        TAKE_ITEM_FROM_WAREHOUSE,

        CHANGE_PVP,

        ALLY_INVITE,
        ALLY_ACCEPT,
        ALLY_REMOVE,

        NPC_BLACKSMITH_OPEN_MENU,
        NPC_BLACKSMITH_TAKE_ENHANCERS,

        NPC_WIZARD_OPEN_MENU,
        NPC_WIZARD_USE_REGENERATION

    }
}
