package nets.tools.objects;

import net.saidora.api.helpers.MathHelper;
import nets.tools.manager.UserManager;
import nets.tools.model.Brotherhood;
import nets.tools.model.Region;
import nets.tools.model.User;
import org.bukkit.Location;

import java.util.*;

public class BrotherhoodController implements Brotherhood {

    private final String tag;
    private final String name;

    private String owner;
    private Region hearth;

    private int size;
    private Location center;
    private Location firstCorner, secondCorner;

    private List<String> members = new ArrayList<>();

    private Map<Permission, List<String>> permissionListMap = new HashMap<>();
    private List<Brotherhood> allies = new ArrayList<>();

    public BrotherhoodController(String tag, String name, Location location) {
        this.tag = tag;
        this.name = name;
        this.center = location;
    }

    @Override
    public String tag() {
        return tag;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String owner() {
        return owner;
    }

    @Override
    public void owner(String nickName) {
        this.owner = nickName;
    }

    @Override
    public Optional<User> getOwner() {
        return UserManager.getInstance().find(owner);
    }

    @Override
    public Region hearth() {
        return hearth;
    }

    @Override
    public List<String> members() {
        return members;
    }

    @Override
    public List<User> getMembers() {
        return members.stream().map(s -> UserManager.getInstance().find(s).orElse(null)).filter(Objects::nonNull).toList();
    }

    @Override
    public void addMember(User user) {
        members.add(user.getNickName());
    }

    @Override
    public void removeMember(String nickName) {
        members.remove(nickName);
    }

    @Override
    public Map<Permission, List<String >> permissions() {
        return permissionListMap;
    }

    @Override
    public List<Brotherhood> allies() {
        return allies;
    }

    @Override
    public void addAlly(Brotherhood brotherhood) {
        this.allies.add(brotherhood);
    }

    @Override
    public void removeAlly(Brotherhood brotherhood) {
        this.allies.remove(brotherhood);
    }

    @Override
    public int getPoints() {
        return getMembers().stream().mapToInt(User::getPoints).sum() / members.size();
    }

    @Override
    public void setPoints(int points) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getKills() {
        return getMembers().stream().mapToInt(User::getKills).sum();
    }

    @Override
    public void setKills(int kills) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDeaths() {
        return getMembers().stream().mapToInt(User::getDeaths).sum();
    }

    @Override
    public void setDeaths(int deaths) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAssists() {
        return getMembers().stream().mapToInt(User::getAssists).sum();
    }

    @Override
    public void setAssists(int assists) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public long getExperiencePoints() {
        return 0;
    }

    @Override
    public long getRequiredExperiencePoints() {
        return 0;
    }

    @Override
    public Location firstCorner() {
        return firstCorner;
    }

    @Override
    public void firstCorner(Location location) {
        this.firstCorner = location;
    }

    @Override
    public Location secondCorner() {
        return secondCorner;
    }

    @Override
    public void secondCorner(Location location) {
        this.secondCorner = location;
    }

    @Override
    public double getDistance(Location location) {
        return MathHelper.getDistance(location, firstCorner.getX(), firstCorner.getZ(), secondCorner.getX(), secondCorner.getZ());
    }

    @Override
    public boolean isInside(Location location) {
        return location.getX() >= firstCorner.getX() && location.getZ() >= firstCorner.getZ() && location.getX() <= secondCorner.getX() && location.getZ() <= secondCorner.getZ();
    }

    @Override
    public boolean isOutside(Location location) {
        return !isInside(location);
    }
}
