package nets.tools.model;

public interface Rank {

    int getPoints();
    void setPoints(int points);

    int getKills();
    void setKills(int kills);

    int getDeaths();
    void setDeaths(int deaths);

    int getAssists();
    void setAssists(int assists);

    int getLevel();
    void setLevel(int level);

    long getExperiencePoints();
    long getRequiredExperiencePoints();

}
